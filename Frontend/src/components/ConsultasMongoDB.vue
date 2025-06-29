<template>
  <div class="consultas-container">
    <h2>Consultas</h2>


    <section class="consulta-section">
  <h3>Puntuación promedio de empresas</h3>
  <button @click="consultarPromediosPuntuacion" class="btn-consultar">Ver Puntuación Promedio</button>

  <table v-if="promediosPuntuacion.length">
    <thead>
      <tr>
        <th>Nombre Empresa</th>
        <th>Puntuación Promedio</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(p, index) in promediosPuntuacion" :key="index">
        <td>{{ p.nombre_empresa }}</td>
        <td>{{ p.promedioPuntuacion.toFixed(2) }}</td>
      </tr>
    </tbody>
  </table>
</section>


<section class="consulta-section">
  <h3>Opiniones con nombre de empresa</h3>
  <button @click="consultarOpinionesConNombre" class="btn-consultar">Ver Opiniones</button>

  <table v-if="opinionesConNombre.length">
    <thead>
      <tr>
        <th>Nombre empresa</th>
        <th>Opinión</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(op, index) in opinionesConNombre" :key="index">
        <td>{{ op.nombre_empresa }}</td>
        <td>{{ op.comentario }}</td>
      </tr>
    </tbody>
  </table>
</section>


<section class="consulta-section">
  <h3>Pedidos con más de 3 cambios en 10 minutos</h3>
  <button @click="consultarPedidosConMuchosCambios" class="btn-consultar">
    Consultar
  </button>
  <div v-if="pedidosConMuchosCambios !== null" class="resultado">
    Cantidad de pedidos con más de 3 cambios en 10 minutos: 
    <strong>{{ pedidosConMuchosCambios }}</strong>
  </div>
</section>


<section class="consulta-section">
  <h3>Ruta(s) más frecuente(s)</h3>
  <button @click="consultarRutasFrecuentes" class="btn-consultar">
    Ver Ruta Frecuente
  </button>

  <l-map
    v-if="rutasParaMostrar.length"
    :zoom="13"
    :center="centroMapa"
    style="height: 400px; margin: 20px auto; max-width:800px;"
  >
    <l-tile-layer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"/>

    <template v-for="(poly, idx) in rutasParaMostrar" :key="idx">
      <!-- Punto de PARTIDA con iconEmpresa -->
      <l-marker
        :lat-lng="poly[0]"
        :icon="iconEmpresa"
      >
        <l-popup>Ruta {{ idx+1 }} - Partida</l-popup>
      </l-marker>

      <!-- Punto de LLEGADA con iconPedido -->
      <l-marker
        :lat-lng="poly[poly.length-1]"
        :icon="iconPedido"
      >
        <l-popup>Ruta {{ idx+1 }} - Llegada</l-popup>
      </l-marker>

      <!-- Polilínea coloreada -->
      <l-polyline
        :lat-lngs="poly"
        :color="rutaColors[idx % rutaColors.length]"
        weight="5"
      />
    </template>
  </l-map>
</section>


<section class="consulta-section">
  <h3>Clientes sin compra tras navegación</h3>
  <button @click="consultarClientesSinCompra" class="btn-consultar">
    Ver Clientes
  </button>

  <!-- Si hay resultados, mostramos tabla de nombres -->
  <table v-if="clientesSinCompra.length">
    <thead>
      <tr>
        <th>#</th>
        <th>Nombre Cliente</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(nombre, idx) in clientesSinCompra" :key="idx">
        <td>{{ idx + 1 }}</td>
        <td>{{ nombre }}</td>
      </tr>
    </tbody>
  </table>

  <!-- Si no hay resultados, mensaje -->
  <div v-else class="resultado">
    No se encontraron clientes sin compra tras navegar.
  </div>
</section>










  </div>
</template>

<script>
import ClienteService from "@/services/Cliente.service";
import PedidoService from "@/services/Pedido.service";
import RepartidorService from "@/services/Repartidor.service";
import OpinionesService from "@/services/Opiniones.service";
import EmpresasService from "@/services/EmpresasAsociadas.service"

import { LMap, LTileLayer, LMarker, LPopup, LPolyline } from "@vue-leaflet/vue-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: new URL("leaflet/dist/images/marker-icon-2x.png", import.meta.url).href,
  iconUrl: new URL("leaflet/dist/images/marker-icon.png", import.meta.url).href,
  shadowUrl: new URL("leaflet/dist/images/marker-shadow.png", import.meta.url).href,
});

export default {
  name: "ConsultasCliente",
  components: {
    LMap,
    LTileLayer,
    LMarker,
    LPopup,
    LPolyline,
  },
  data() {
    const iconEmpresa = L.icon({
      iconUrl: new URL("leaflet/dist/images/marker-icon.png", import.meta.url).href,
      shadowUrl: new URL("leaflet/dist/images/marker-shadow.png", import.meta.url).href,
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });

    const iconPedido = L.icon({
      iconUrl: "https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png",
      shadowUrl: new URL("leaflet/dist/images/marker-shadow.png", import.meta.url).href,
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });

    return {
      clienteId: "",
      zonaCobertura: null,
      clientesLejanos: [],
      empresaIdCercanos: "",
      pedidosCercanos: [],
      pedidosLejanos: [],
      pedidosCruzanZonas: [],
      iconEmpresa,
      iconPedido,
      idRepartidorConsulta: "",
      ultimosMesesConsulta: "",
      distanciaTotal: null,



      promediosPuntuacion: [],
      opinionesConNombre: [],
      pedidosConMuchosCambios: null,
      rutasParaMostrar: [],
      centroMapa: [-33.45, -70.65],
      clientesSinCompra: [],
      rutaColors: ['blue', 'red', 'green', 'orange', 'purple', 'teal', 'magenta'],
      

    };
  },
  methods: {
    async consultarPromediosPuntuacion() {
        try {
            const token = localStorage.getItem("jwt");
            if (!token) {
            alert("No hay token disponible");
            return;
            }
            const res = await OpinionesService.getPromedioPuntuacion(token);
            const promedios = res.data;

            const promConNombre = await Promise.all(
            promedios.map(async (p) => {
                const empresaRes = await EmpresasService.getEmpresaById(p.empresa_id, token);
                return {
                promedioPuntuacion: p.promedioPuntuacion,
                empresa_id: p.empresa_id,
                nombre_empresa: empresaRes.data.nombre_empresa,
                };
            })
            );

            this.promediosPuntuacion = promConNombre;

        } catch (error) {
            console.error("Error al obtener puntuación promedio:", error);
            alert("Error al obtener puntuación promedio");
        }
    },


    async consultarOpinionesConNombre() {
        try {
            const token = localStorage.getItem("jwt");
            if (!token) {
            alert("No hay token disponible");
            return;
            }

            // Obtienes las opiniones
            const res = await OpinionesService.getOpinionesError(token);
            const opiniones = res.data;

            // Para cada opinión, obtienes el nombre de la empresa usando empresaId
            const opinionesConEmpresa = await Promise.all(
            opiniones.map(async (opinion) => {
                const empresaRes = await EmpresasService.getEmpresaById(opinion.empresaId, token);
                return {
                comentario: opinion.comentario,
                puntuacion: opinion.puntuacion,
                fecha: opinion.fecha,
                nombre_empresa: empresaRes.data.nombre_empresa,
                };
            })
            );

            this.opinionesConNombre = opinionesConEmpresa;
        } catch (error) {
            console.error("Error al obtener opiniones con nombre de empresa:", error);
            alert("Error al obtener opiniones");
        }
    },


    async consultarPedidosConMuchosCambios() {
        try {
            const token = localStorage.getItem("jwt");
            const res = await PedidoService.getPedidosConCambios3_10min(token);
            this.pedidosConMuchosCambios = res.data;
        } catch (error) {
            console.error("Error al obtener pedidos con muchos cambios:", error);
            alert("Error al obtener pedidos con muchos cambios");
        }
    },


    async consultarRutasFrecuentes() {
        try {
            const token = localStorage.getItem("jwt");
            const res = await RepartidorService.getRutasFrecuentes(token);
            const rutas = res.data;

            if (!rutas.length) {
            this.rutasParaMostrar = [];
            return;
            }

            const maxFreq = Math.max(...rutas.map(r => r.frecuencia));
            const rutasMax = rutas.filter(r => r.frecuencia === maxFreq);

            this.rutasParaMostrar = rutasMax.map(rutaObj =>
            rutaObj._id.map(pt => [pt.lng, pt.lat])
            );

            const firstPt = this.rutasParaMostrar[0][0];
            this.centroMapa = [ firstPt[0], firstPt[1] ];
        } catch (err) {
            console.error(err);
            this.rutasParaMostrar = [];
        }
    },



    async consultarClientesSinCompra() {
    try {
      const token = localStorage.getItem("jwt");
      // 1) obtiene array de IDs [3,1,2,...]
      const resIds = await ClienteService.getClienteNavegacion(token);
      const ids = resIds.data;

      // 2) por cada ID pide los datos y extrae el nombre
      const clientes = await Promise.all(
        ids.map(id =>
          ClienteService.getClienteById(id, token)
            .then(res => res.data.nombre_cliente)
        )
      );

      // 3) asigna el array de nombres para pintar
      this.clientesSinCompra = clientes;
    } catch (err) {
      console.error("Error al cargar clientes sin compra:", err);
      this.clientesSinCompra = [];
    }
  },












    
    parseUbicacion(ubicacion) {
      const match = ubicacion.match(/POINT\((-?\d+\.\d+) (-?\d+\.\d+)\)/);
      if (match) {
        const [, lon, lat] = match;
        return [parseFloat(lat), parseFloat(lon)];
      }
      return [0, 0];
    },
  },
};
</script>

<style scoped>
.consultas-container {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

h2 {
  margin-bottom: 30px;
  font-size: 1.8rem;
  text-align: center;
  color: #000000;
}

.consulta-section {
  margin-bottom: 40px;
  padding: 20px;
  background-color: #fdfdfd;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

h3 {
  margin-bottom: 15px;
  font-size: 1.4rem;
  color: #000000;
  border-left: 4px solid #2835a7;
  padding-left: 10px;
}

.input-group {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.input-consulta {
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  flex-grow: 1;
  max-width: 300px;
}

.btn-consultar {
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn-consultar:hover {
  background-color: #218838;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

thead {
  background-color: #28a745;
  color: #fff;
}

th{
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eaeaea;
} 
td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eaeaea;
  color: #000000;
}

tbody tr:hover {
  background-color: #f9f9f9;
  color: #000000;
}

.resultado {
  margin-top: 15px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 4px;
  border-left: 4px solid #17a2b8;
  color: #000000;
}
</style>