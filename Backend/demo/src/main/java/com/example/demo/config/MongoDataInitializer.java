package com.example.demo.config;

import com.example.demo.Entity.mongo.*;
import com.example.demo.Repository.mongo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MongoDataInitializer implements CommandLineRunner {

    @Autowired private OpinionClienteRepository opinionClienteRepo;
    @Autowired private LogPedidoRepository logPedidoRepo;
    @Autowired private HistorialRepartidorRepository historialRepartidorRepo;
    @Autowired private NavegacionUsuarioRepository navegacionUsuarioRepo;

    @Override
    public void run(String... args) throws Exception {
        if (opinionClienteRepo.count() == 0) {
            List<OpinionCliente> opiniones = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                opiniones.add(new OpinionCliente(
                        UUID.randomUUID().toString(),
                        "Comentario " + i,
                        new Random().nextInt(5) + 1,
                        new Date(),
                        i, // clienteId
                        i  // empresaId
                ));
            }
            opinionClienteRepo.saveAll(opiniones);
        }

        if (logPedidoRepo.count() == 0) {
            List<LogPedido> logs = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                List<LogPedido.EstadoPedido> historial = List.of(
                        new LogPedido.EstadoPedido("CREADO", new Date()),
                        new LogPedido.EstadoPedido("EN_CAMINO", new Date())
                );
                logs.add(new LogPedido(UUID.randomUUID().toString(), i, historial));
            }
            logPedidoRepo.saveAll(logs);
        }

        if (historialRepartidorRepo.count() == 0) {
            List<HistorialRepartidor> historiales = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                List<HistorialRepartidor.Ubicacion> recorrido = List.of(
                        new HistorialRepartidor.Ubicacion(1.0 + i, 2.0 + i, new Date()),
                        new HistorialRepartidor.Ubicacion(3.0 + i, 4.0 + i, new Date())
                );
                historiales.add(new HistorialRepartidor(UUID.randomUUID().toString(), i, recorrido));
            }
            historialRepartidorRepo.saveAll(historiales);
        }

        if (navegacionUsuarioRepo.count() == 0) {
            List<NavegacionUsuario> eventos = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                eventos.add(new NavegacionUsuario(
                        UUID.randomUUID().toString(),
                        i,
                        new Date(),
                        "busqueda",
                        "Buscó producto " + i,
                        List.of("filtro" + i, "oferta")
                ));
            }
            navegacionUsuarioRepo.saveAll(eventos);
        }

        System.out.println("🟢 Colecciones Mongo pobladas con 5 objetos de prueba cada una.");
    }
}
