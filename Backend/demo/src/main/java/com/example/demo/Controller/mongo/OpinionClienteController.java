package com.example.demo.Controller.mongo;

import com.example.demo.Entity.mongo.OpinionCliente;
import com.example.demo.Service.mongo.OpinionClienteService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mongo/opiniones")
@CrossOrigin(origins = "*")
public class OpinionClienteController {

    @Autowired
    private OpinionClienteService service;

    @Autowired
    private OpinionClienteService opinionClienteService;


    @GetMapping
    public List<OpinionCliente> getAll() {
        return service.getAllOpiniones();
    }

    @GetMapping("/{id}")
    public Optional<OpinionCliente> getById(@PathVariable int id) {
        return service.getById(id);
    }

    @PostMapping
    public OpinionCliente create(@RequestBody OpinionCliente opinion) {
        return service.save(opinion);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteById(id);
    }

    //------------- CONSULTAS REQUERIDAS -------------

    //1) Obtener el promedio de puntuación por empresa o farmacia.
    @GetMapping("/promedio-puntuacion")
    public List<org.bson.Document> getPromedioPorEmpresa() {
        return opinionClienteService.obtenerPromedioPuntuacionPorEmpresa();
    }

    //2) Listar las opiniones que contengan palabras clave como 'demora' o 'error'.
    @GetMapping("/buscar-claves")
    public List<org.bson.Document> getOpinionesConPalabrasClave() {
        return opinionClienteService.buscarOpinionesConPalabrasClave();
    }

    //6) Agrupar opiniones por hora del día para analizar patrones de satisfacción.
    @GetMapping("/agrupadas-por-hora")
    public List<Document> agruparPorHora() {
        return service.agruparOpinionesPorHora();
    }


}
