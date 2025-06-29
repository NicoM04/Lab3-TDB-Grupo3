package com.example.demo.Controller.mongo;

import com.example.demo.Entity.mongo.HistorialRepartidor;
import com.example.demo.Service.mongo.HistorialRepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.bson.Document;
import java.util.List;

@RestController
@RequestMapping("/mongo/historial-repartidores")
@CrossOrigin(origins = "*")  // permite llamadas desde tu frontend
public class HistorialRepartidorController {

    @Autowired
    private HistorialRepartidorService service;

    @GetMapping
    public List<HistorialRepartidor> getAll() {
        return service.getAll();
    }

    @GetMapping("/repartidor/{id}")
    public List<HistorialRepartidor> getByRepartidor(@PathVariable int id) {
        return service.getByRepartidor(id);
    }

    @PostMapping
    public HistorialRepartidor save(@RequestBody HistorialRepartidor historial) {
        return service.save(historial);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }


    @GetMapping("/rutas-frecuentes")
    public List<Document> obtenerRutasFrecuentesUltimos7Dias() {
        return service.obtenerRutasFrecuentesUltimos7Dias();
    }
}
