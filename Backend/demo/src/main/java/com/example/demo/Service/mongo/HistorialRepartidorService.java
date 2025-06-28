package com.example.demo.Service.mongo;

import com.example.demo.Entity.mongo.HistorialRepartidor;
import com.example.demo.Repository.mongo.HistorialRepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import org.bson.Document;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class HistorialRepartidorService {

    @Autowired
    private HistorialRepartidorRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public List<HistorialRepartidor> getAll() {
        return repository.findAll();
    }

    public HistorialRepartidor save(HistorialRepartidor historial) {
        return repository.save(historial);
    }

    public List<HistorialRepartidor> getByRepartidor(int idRepartidor) {
        return repository.findByIdRepartidor(idRepartidor);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
    
    public List<Document> obtenerRutasFrecuentesUltimos7Dias() {
        Date fechaLimite = new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000);

        Aggregation agg = newAggregation(
                // 1) Filtrar documentos que contengan alguna ubicación en últimos 7 días
                match(Criteria.where("recorrido.timestamp").gte(fechaLimite)),

                // 2) Desplegar el arreglo de ubicaciones para poder proyectar solo lat/lng
                unwind("recorrido"),

                // 3) Proyectar solo lat y lng (ignorando timestamp)
                project()
                        .and("idRepartidor").as("idRepartidor")
                        .and("recorrido.lat").as("lat")
                        .and("recorrido.lng").as("lng"),

                // 4) Reagrupar las ubicaciones por idRepartidor para reconstruir la ruta sin timestamp
                group("idRepartidor")
                        .push(new Document("lat", "$lat").append("lng", "$lng")).as("ruta"),

                // 5) Agrupar por la ruta (arreglo de lat/lng) para contar frecuencia (rutas repetidas)
                group("ruta")
                        .count().as("frecuencia"),

                // 6) Ordenar y limitar
                sort(Sort.by(Sort.Direction.DESC, "frecuencia")),
                limit(10)
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(agg, "historial_repartidores", Document.class);
        return results.getMappedResults();
    }


}
