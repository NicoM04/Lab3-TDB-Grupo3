package com.example.demo.Controller;

import com.example.demo.DTO.*;
import com.example.demo.Entity.Pedido;
import com.example.demo.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@CrossOrigin("*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/create")
    public Pedido crearPedido(@RequestBody Pedido pedido) {
        return pedidoService.crearPedido(pedido);
    }

    @GetMapping("/getAll")
    public List<PedidoDTO> obtenerTodos(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "100") int size) {
        return pedidoService.obtenerTodos(page, size);
    }

    //Aún no se usa el frontend
    @GetMapping("/resumenCliente/{id}")
    public List<ResumenPedidoDTO> getResumenPorCliente(@PathVariable Integer id) {
        return pedidoService.obtenerResumenPorCliente(id);
    }

    // Obtener un pedido por ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<PedidoDTO> obtenerPorId(@PathVariable("id") Integer id) {
        PedidoDTO pedido = pedidoService.getById(id);
        if (pedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Si no se encuentra el pedido
        }
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public String actualizarPedido(@RequestBody Pedido pedido, @PathVariable Integer id) {
        return pedidoService.actualizarPedido(pedido, id);
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarPedido(@PathVariable Integer id) {
        pedidoService.eliminarPedido(id);
    }

    //---------------- PROCEDIMIENTOS---------------------

    //1) Registrar pedido completo
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarPedido(@RequestBody PedidoCompletoDTO pedido) {
        try {
            pedidoService.registrarPedido(pedido);
            return ResponseEntity.ok("Pedido registrado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar pedido: " + e.getMessage());
        }
    }

    //2) Confirmar pedido y descontar stock
    @PutMapping("/confirmarPedidoYDescontarStock/{id}")
    public ResponseEntity<String> confirmarPedido(@PathVariable("id") int id) {
        try {
            // Llamamos al servicio para confirmar el pedido y descontar el stock
            pedidoService.confirmarPedidoYDescontarStock(id);
            return ResponseEntity.ok("Pedido confirmado y stock descontado correctamente.");
        } catch (Exception e) {
            // Si ocurre un error, devolvemos un mensaje de error
            return ResponseEntity.status(500).body("Error al confirmar el pedido: " + e.getMessage());
        }
    }

    //3) Cambiar estado del pedido con validación
    @PutMapping("/cambiarEstadoPedido/{id}")
    public void cambiarEstadoPedido(@PathVariable int id, @RequestBody EstadoPedidoDTO dto) {
        pedidoService.cambiarEstadoPedido(id, dto.getNuevoEstado());
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosPorCliente(
            @PathVariable Integer idCliente,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<PedidoDTO> pedidos = pedidoService.obtenerPedidosPorCliente(idCliente, page, size);
        return ResponseEntity.ok(pedidos); // ✅ Devuelve 200 OK incluso si está vacío
    }

    @GetMapping("/masCercanos/{idEmpresa}")
    public ResponseEntity<List<Pedido>> obtenerPedidosMasCercanos(
            @PathVariable Integer idEmpresa,
            @RequestParam(defaultValue = "5") int limite) {
        List<Pedido> pedidos = pedidoService.getPedidosMasCercanos(idEmpresa, limite);
        return pedidos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pedidos);
    }

    //-------------------------- CONSULTAS LAB 2 -------------------------------

    //1) Encontrar los 5 puntos de entrega más cercanos a una empresa asociada.
    @GetMapping("/masCercanosEmpresa/{idEmpresa}")
    public ResponseEntity<List<PedidoCercanoDTO>> obtenerPedidosMasCercanosEmpresa(
            @PathVariable Integer idEmpresa,
            @RequestParam(defaultValue = "5") int limite) {

        List<PedidoCercanoDTO> pedidos = pedidoService.getPedidosMasCercanosEmpresa(idEmpresa, limite);

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();  // 204 si no hay resultados
        }
        return ResponseEntity.ok(pedidos);
    }

    //4) Identificar el punto de entrega más lejano desde cada empresa asociada.
    @GetMapping("/masLejanosPorEmpresa")
    public ResponseEntity<List<PedidoCercanoDTO>> obtenerPedidosMasLejanos() {
        List<PedidoCercanoDTO> pedidos = pedidoService.getPedidosMasLejanosPorEmpresa();
        return pedidos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pedidos);
    }

    //5) Listar todos los pedidos cuya ruta estimada cruce más de 2 zonas de reparto.
    @GetMapping("/pedidosConMasDeDosZonas")
    public ResponseEntity<List<PedidoRutaDTO>> obtenerPedidosConMasDeDosZonas() {
        List<PedidoRutaDTO> pedidos = pedidoService.getPedidosConMasDeDosZonas();
        return pedidos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pedidos);
    }


}
