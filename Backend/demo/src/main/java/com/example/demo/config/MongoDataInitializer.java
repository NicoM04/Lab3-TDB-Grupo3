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

        List<OpinionCliente> opiniones = List.of(
                new OpinionCliente(null, "Muy buena atención, pero hubo una pequeña demora.", 4, parseDate("2025-05-14"), 1, 1), // Pedido 1 entrega 2025-05-14
                new OpinionCliente(null, "Servicio rápido y profesional. Recomiendo.", 5, parseDate("2020-08-16"), 1, 1), // Pedido 2 entrega 2020-08-16
                new OpinionCliente(null, "Hubo un error en el documento entregado.", 2, parseDate("2021-07-11"), 1, 3), // Pedido 3 entrega 2021-07-11
                new OpinionCliente(null, "Todo bien, pero tardó más de lo esperado.", 3, parseDate("2022-11-05"), 1, 4), // Pedido 4 entrega 2022-11-05
                new OpinionCliente(null, "Excelente experiencia. Repartidor muy amable.", 5, parseDate("2023-02-19"), 2, 5), // Pedido 5 entrega 2023-02-19
                new OpinionCliente(null, "Demora en la entrega del documento legal.", 3, parseDate("2024-01-11"), 3, 1), // Pedido 6 entrega 2024-01-11
                new OpinionCliente(null, "Error en el certificado académico entregado.", 1, parseDate("2025-05-14"), 2, 5), // Pedido 7 entrega pendiente, usaremos 2025-05-14 como referencia
                new OpinionCliente(null, "Rápido servicio para trámite judicial.", 5, parseDate("2019-04-04"), 4, 1), // Pedido 16 entrega 2019-04-04
                new OpinionCliente(null, "Problemas con el courier premium contratado.", 2, parseDate("2025-06-16"), 8, 2), // Pedido 11 entrega 2025-06-16
                new OpinionCliente(null, "Perfecta entrega de documentos financieros.", 5, parseDate("2021-09-30"), 2, 5)  // Pedido 9 entrega 2021-09-30
        );
        opinionClienteRepo.saveAll(opiniones);

        // Logs de pedido (fechas alineadas con SQL)
        List<LogPedido> logs = List.of(
                new LogPedido(null, 1, List.of(
                        new LogPedido.EstadoPedido("Pendiente", parseDateTime("2025-05-14T09:00:00")),
                        new LogPedido.EstadoPedido("Confirmado", parseDateTime("2025-05-14T09:01:00")),
                        new LogPedido.EstadoPedido("En reparto", parseDateTime("2025-05-14T09:05:00")),
                        new LogPedido.EstadoPedido("Entregado", parseDateTime("2025-05-14T09:09:00")),
                        new LogPedido.EstadoPedido("Finalizado", parseDateTime("2025-05-14T09:10:00"))
                )),
                new LogPedido(null, 2, List.of(
                        new LogPedido.EstadoPedido("Pendiente", parseDateTime("2020-08-14T12:00:00")),
                        new LogPedido.EstadoPedido("Confirmado", parseDateTime("2020-08-14T12:01:00")),
                        new LogPedido.EstadoPedido("En reparto", parseDateTime("2020-08-14T12:03:00")),
                        new LogPedido.EstadoPedido("Entregado", parseDateTime("2020-08-16T15:00:00")),
                        new LogPedido.EstadoPedido("Finalizado", parseDateTime("2020-08-16T15:05:00"))
                )),
                new LogPedido(null, 3, List.of(
                        new LogPedido.EstadoPedido("Pendiente", parseDateTime("2021-07-09T08:30:00")),
                        new LogPedido.EstadoPedido("Confirmado", parseDateTime("2021-07-09T08:35:00")),
                        new LogPedido.EstadoPedido("En reparto", parseDateTime("2021-07-09T08:40:00")),
                        new LogPedido.EstadoPedido("Entregado", parseDateTime("2021-07-09T09:00:00"))
                )),
                new LogPedido(null, 4, List.of(
                        new LogPedido.EstadoPedido("Pendiente", parseDateTime("2022-11-04T10:00:00")),
                        new LogPedido.EstadoPedido("Confirmado", parseDateTime("2022-11-04T10:01:00")),
                        new LogPedido.EstadoPedido("En reparto", parseDateTime("2022-11-04T10:03:00")),
                        new LogPedido.EstadoPedido("Error en ruta", parseDateTime("2022-11-04T10:05:00")),
                        new LogPedido.EstadoPedido("Cancelado", parseDateTime("2022-11-04T10:06:00"))
                )),
                new LogPedido(null, 5, List.of(
                        new LogPedido.EstadoPedido("Pendiente", parseDateTime("2023-02-18T13:00:00")),
                        new LogPedido.EstadoPedido("Confirmado", parseDateTime("2023-02-18T13:01:00")),
                        new LogPedido.EstadoPedido("En reparto", parseDateTime("2023-02-18T13:05:00")),
                        new LogPedido.EstadoPedido("Entregado", parseDateTime("2023-02-18T13:30:00")),
                        new LogPedido.EstadoPedido("Finalizado", parseDateTime("2023-02-18T13:35:00"))
                )),
                new LogPedido(null, 6, List.of(
                        new LogPedido.EstadoPedido("Pendiente", parseDateTime("2024-01-10T08:00:00")),
                        new LogPedido.EstadoPedido("Confirmado", parseDateTime("2024-01-10T08:01:00")),
                        new LogPedido.EstadoPedido("Retrasado", parseDateTime("2024-01-10T08:02:00")),
                        new LogPedido.EstadoPedido("En reparto", parseDateTime("2024-01-10T08:03:00")),
                        new LogPedido.EstadoPedido("Entregado", parseDateTime("2024-01-10T08:09:00")),
                        new LogPedido.EstadoPedido("Finalizado", parseDateTime("2024-01-10T08:10:00"))
                )),
                new LogPedido(null, 7, List.of(
                        new LogPedido.EstadoPedido("Pendiente", parseDateTime("2025-05-14T10:00:00")),
                        new LogPedido.EstadoPedido("Confirmado", parseDateTime("2025-05-14T10:01:00")),
                        new LogPedido.EstadoPedido("Preparación", parseDateTime("2025-05-14T10:02:00")),
                        new LogPedido.EstadoPedido("En reparto", parseDateTime("2025-05-14T10:05:00")),
                        new LogPedido.EstadoPedido("Entregado", parseDateTime("2025-05-14T10:09:00"))
                ))
        );

        logPedidoRepo.saveAll(logs);
        // Inserta historial de repartidores (relacionados con repartidores SQL 1-5)
        List<HistorialRepartidor> historial = List.of(
                // Ruta frecuente A → B → C (repetida 3 veces) para repartidor 1 (Carlos Medina)
                new HistorialRepartidor(null, 1, List.of(
                        // Desde empresa LegalPro (-70.6420, -33.4206) hasta cliente Ana Torres (-70.6506, -33.4372)
                        new HistorialRepartidor.Ubicacion(-70.6420, -33.4206, parseDateTime("2025-06-22T09:00:00")),
                        new HistorialRepartidor.Ubicacion(-70.6450, -33.4300, parseDateTime("2025-06-22T09:10:00")),
                        new HistorialRepartidor.Ubicacion(-70.6506, -33.4372, parseDateTime("2025-06-22T09:25:00"))
                )),
                new HistorialRepartidor(null, 1, List.of(
                        new HistorialRepartidor.Ubicacion(-70.6420, -33.4206, parseDateTime("2025-06-23T10:00:00")),
                        new HistorialRepartidor.Ubicacion(-70.6450, -33.4300, parseDateTime("2025-06-23T10:15:00")),
                        new HistorialRepartidor.Ubicacion(-70.6506, -33.4372, parseDateTime("2025-06-23T10:35:00"))
                )),
                new HistorialRepartidor(null, 1, List.of(
                        new HistorialRepartidor.Ubicacion(-70.6420, -33.4206, parseDateTime("2025-06-25T11:00:00")),
                        new HistorialRepartidor.Ubicacion(-70.6450, -33.4300, parseDateTime("2025-06-25T11:12:00")),
                        new HistorialRepartidor.Ubicacion(-70.6506, -33.4372, parseDateTime("2025-06-25T11:30:00"))
                )),

                // Ruta para repartidor 2 (María Flores)
                new HistorialRepartidor(null, 2, List.of(
                        // Desde Notaría San Martín (-70.7200, -33.4500) hasta cliente Luis Rojas (-70.6693, -33.4523)
                        new HistorialRepartidor.Ubicacion(-70.7200, -33.4500, parseDateTime("2025-06-26T08:00:00")),
                        new HistorialRepartidor.Ubicacion(-70.7000, -33.4550, parseDateTime("2025-06-26T08:15:00")),
                        new HistorialRepartidor.Ubicacion(-70.6693, -33.4523, parseDateTime("2025-06-26T08:30:00"))
                )),

                // Ruta para repartidor 5 (Andrés Castillo)
                new HistorialRepartidor(null, 5, List.of(
                        // Desde Corporación Educativa (-70.6167, -33.4010) hasta cliente Lucía Díaz (-70.5455, -33.4160)
                        new HistorialRepartidor.Ubicacion(-70.6167, -33.4010, parseDateTime("2025-06-28T09:00:00")),
                        new HistorialRepartidor.Ubicacion(-70.6000, -33.4100, parseDateTime("2025-06-28T09:15:00")),
                        new HistorialRepartidor.Ubicacion(-70.5700, -33.4150, parseDateTime("2025-06-28T09:25:00")),
                        new HistorialRepartidor.Ubicacion(-70.5455, -33.4160, parseDateTime("2025-06-28T09:30:00"))
                ))
        );
        historialRepartidorRepo.saveAll(historial);

        // Inserta eventos navegación usuarios (relacionados con clientes SQL)
        List<NavegacionUsuario> eventos = List.of(
                // Eventos para clientes que realizaron pedidos
                new NavegacionUsuario(null, 1, parseDateTime("2025-05-13T10:00:00"), "busqueda", "Buscó servicio notarial", List.of("urgente", "documento")),
                new NavegacionUsuario(null, 1, parseDateTime("2025-05-13T10:30:00"), "click", "Clic en servicio express", List.of("express")),

                // Eventos para clientes que buscaron pero no concretaron pedidos (para consulta 5)
                new NavegacionUsuario(null, 6, parseDateTime("2025-06-27T10:00:00"), "busqueda", "Buscó servicio notarial", List.of("urgente", "documento")),
                new NavegacionUsuario(null, 6, parseDateTime("2025-06-27T10:05:00"), "click", "Clic en courier premium", List.of("premium")),
                new NavegacionUsuario(null, 7, parseDateTime("2025-06-27T10:10:00"), "busqueda", "Revisión de certificados oficiales", List.of("educacion")),
                new NavegacionUsuario(null, 8, parseDateTime("2025-06-27T10:12:00"), "busqueda", "Trámites legales express", List.of("legal", "express")),
                new NavegacionUsuario(null, 9, parseDateTime("2025-06-27T10:20:00"), "click", "Servicio financiero", List.of("finanzas", "urgente")),

                // Eventos para clientes que sí realizaron pedidos
                new NavegacionUsuario(null, 2, parseDateTime("2023-02-17T15:00:00"), "busqueda", "Documentos legales urgentes", List.of("legal", "urgente")),
                new NavegacionUsuario(null, 3, parseDateTime("2024-01-09T11:00:00"), "busqueda", "Certificados académicos", List.of("educacion", "certificado")),
                new NavegacionUsuario(null, 4, parseDateTime("2022-11-03T14:00:00"), "click", "Servicio judicial", List.of("judicial"))
        );
        navegacionUsuarioRepo.saveAll(eventos);

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