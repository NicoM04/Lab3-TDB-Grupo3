package com.example.demo.Service.mongo;

import com.example.demo.Entity.mongo.HistorialRepartidor;
import com.example.demo.Repository.mongo.HistorialRepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
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

        MatchOperation match = match(Criteria.where("timestamp").gte(fechaLimite));
        GroupOperation group = group("idRepartidor", "latitud", "longitud").count().as("frecuencia");
        SortOperation sort = sort(Sort.by(Sort.Direction.DESC, "frecuencia"));
        LimitOperation limit = limit(10);

        Aggregation agg = newAggregation(match, group, sort, limit);

        AggregationResults<Document> results = mongoTemplate.aggregate(agg, "historial_repartidores", Document.class);
        return results.getMappedResults();
    }
}
