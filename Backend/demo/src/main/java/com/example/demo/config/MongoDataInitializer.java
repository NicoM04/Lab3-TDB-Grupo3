package com.example.demo.config;

import com.example.demo.Entity.mongo.*;
import com.example.demo.Repository.mongo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class MongoDataInitializer implements CommandLineRunner {

    @Autowired private OpinionClienteRepository opinionClienteRepo;
    @Autowired private LogPedidoRepository logPedidoRepo;
    @Autowired private HistorialRepartidorRepository historialRepartidorRepo;
    @Autowired private NavegacionUsuarioRepository navegacionUsuarioRepo;

    @Override
    public void run(String... args) throws Exception {

        // Borra todas las colecciones para reiniciar datos
        opinionClienteRepo.deleteAll();
        logPedidoRepo.deleteAll();
        historialRepartidorRepo.deleteAll();
        navegacionUsuarioRepo.deleteAll();

        // Inserta opiniones
        List<OpinionCliente> opiniones = List.of(
                new OpinionCliente(null, "Muy buena atención, pero hubo una pequeña demora.", 4, parseDate("2024-12-12"), 1, 1),
                new OpinionCliente(null, "Servicio rápido y profesional. Recomiendo.", 5, parseDate("2025-05-14"), 2, 1),
                new OpinionCliente(null, "Hubo un error en el documento entregado.", 2, parseDate("2024-10-22"), 3, 2),
                new OpinionCliente(null, "Todo bien, pero tardó más de lo esperado.", 3, parseDate("2023-11-19"), 4, 3),
                new OpinionCliente(null, "Excelente experiencia. Repartidor muy amable.", 5, parseDate("2025-06-25"), 5, 4)
        );
        opinionClienteRepo.saveAll(opiniones);

        // Inserta logs de pedido
        List<LogPedido> logs = List.of(
                new LogPedido(null, 1, List.of(
                        new LogPedido.EstadoPedido("CREADO", parseDateTime("2025-05-14T09:00:00")),
                        new LogPedido.EstadoPedido("ASIGNADO", parseDateTime("2025-05-14T09:01:00")),
                        new LogPedido.EstadoPedido("EN_CAMINO", parseDateTime("2025-05-14T09:05:00")),
                        new LogPedido.EstadoPedido("ENTREGADO", parseDateTime("2025-05-14T09:09:00"))
                )),
                new LogPedido(null, 2, List.of(
                        new LogPedido.EstadoPedido("CREADO", parseDateTime("2020-08-14T12:00:00")),
                        new LogPedido.EstadoPedido("CANCELADO", parseDateTime("2020-08-14T12:02:00"))
                )),
                new LogPedido(null, 3, List.of(
                        new LogPedido.EstadoPedido("CREADO", parseDateTime("2021-07-09T08:30:00")),
                        new LogPedido.EstadoPedido("EN_CAMINO", parseDateTime("2021-07-09T08:40:00")),
                        new LogPedido.EstadoPedido("ENTREGADO", parseDateTime("2021-07-09T09:00:00"))
                )),
                new LogPedido(null, 4, List.of(
                        new LogPedido.EstadoPedido("CREADO", parseDateTime("2022-11-04T10:00:00")),
                        new LogPedido.EstadoPedido("EN_CAMINO", parseDateTime("2022-11-04T10:03:00")),
                        new LogPedido.EstadoPedido("ERROR_EN_RUTA", parseDateTime("2022-11-04T10:05:00")),
                        new LogPedido.EstadoPedido("CANCELADO", parseDateTime("2022-11-04T10:06:00"))
                )),
                new LogPedido(null, 5, List.of(
                        new LogPedido.EstadoPedido("CREADO", parseDateTime("2023-02-18T13:00:00")),
                        new LogPedido.EstadoPedido("EN_CAMINO", parseDateTime("2023-02-18T13:05:00")),
                        new LogPedido.EstadoPedido("ENTREGADO", parseDateTime("2023-02-18T13:30:00"))
                ))
        );
        logPedidoRepo.saveAll(logs);

        List<HistorialRepartidor> historial = List.of(
                // Ruta frecuente A → B → C (repetida 3 veces)
                new HistorialRepartidor(null, 1, List.of(
                        new HistorialRepartidor.Ubicacion(-70.6500, -33.5000, parseDateTime("2025-06-22T09:00:00")),
                        new HistorialRepartidor.Ubicacion(-70.6420, -33.4206, parseDateTime("2025-06-22T09:15:00")),
                        new HistorialRepartidor.Ubicacion(-70.6506, -33.4372, parseDateTime("2025-06-22T09:25:00"))
                )),
                new HistorialRepartidor(null, 2, List.of(
                        new HistorialRepartidor.Ubicacion(-70.6500, -33.5000, parseDateTime("2025-06-23T10:00:00")),
                        new HistorialRepartidor.Ubicacion(-70.6420, -33.4206, parseDateTime("2025-06-23T10:20:00")),
                        new HistorialRepartidor.Ubicacion(-70.6506, -33.4372, parseDateTime("2025-06-23T10:35:00"))
                )),
                new HistorialRepartidor(null, 3, List.of(
                        new HistorialRepartidor.Ubicacion(-70.6500, -33.5000, parseDateTime("2025-06-25T11:00:00")),
                        new HistorialRepartidor.Ubicacion(-70.6420, -33.4206, parseDateTime("2025-06-25T11:15:00")),
                        new HistorialRepartidor.Ubicacion(-70.6506, -33.4372, parseDateTime("2025-06-25T11:30:00"))
                )),

                // Ruta única distinta D → E → F (Repartidor 4)
                new HistorialRepartidor(null, 4, List.of(
                        new HistorialRepartidor.Ubicacion(-70.6100, -33.4700, parseDateTime("2025-06-26T08:00:00")),
                        new HistorialRepartidor.Ubicacion(-70.6200, -33.4800, parseDateTime("2025-06-26T08:15:00")),
                        new HistorialRepartidor.Ubicacion(-70.6300, -33.4900, parseDateTime("2025-06-26T08:30:00"))
                )),

                // Ruta única distinta G → H → I (Repartidor 5)
                new HistorialRepartidor(null, 5, List.of(
                        new HistorialRepartidor.Ubicacion(-70.7000, -33.5500, parseDateTime("2025-06-28T09:00:00")),
                        new HistorialRepartidor.Ubicacion(-70.7100, -33.5600, parseDateTime("2025-06-28T09:15:00")),
                        new HistorialRepartidor.Ubicacion(-70.7200, -33.5700, parseDateTime("2025-06-28T09:30:00"))
                ))
        );

        historialRepartidorRepo.saveAll(historial);


        // Inserta eventos navegación usuarios
        List<NavegacionUsuario> eventos = List.of(
                new NavegacionUsuario(null, 1, parseDateTime("2025-06-27T10:00:00"), "busqueda", "Buscó servicio notarial", List.of("urgente", "documento")),
                new NavegacionUsuario(null, 2, parseDateTime("2025-06-27T10:05:00"), "click", "Clic en courier premium", List.of("premium")),
                new NavegacionUsuario(null, 2, parseDateTime("2025-06-27T10:10:00"), "busqueda", "Revisión de certificados oficiales", List.of("educacion")),
                new NavegacionUsuario(null, 3, parseDateTime("2025-06-27T10:12:00"), "busqueda", "Trámites legales express", List.of("legal", "express")),
                new NavegacionUsuario(null, 4, parseDateTime("2025-06-27T10:20:00"), "click", "Servicio financiero", List.of("finanzas", "urgente"))
        );
        navegacionUsuarioRepo.saveAll(eventos);

        System.out.println("🟢 MongoDB poblado con datos realistas y listos para consultas agregadas.");
    }

    private static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            return new Date();
        }
    }

    private static Date parseDateTime(String datetime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(datetime);
        } catch (Exception e) {
            return new Date();
        }
    }
}
