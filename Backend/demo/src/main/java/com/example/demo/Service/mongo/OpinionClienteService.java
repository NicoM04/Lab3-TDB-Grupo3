package com.example.demo.Service.mongo;

import com.example.demo.Entity.mongo.OpinionCliente;
import com.example.demo.Repository.mongo.OpinionClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;
import java.util.Optional;

@Service
public class OpinionClienteService {

    @Autowired
    private OpinionClienteRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<OpinionCliente> getAllOpiniones() {
        return repository.findAll();
    }

    public Optional<OpinionCliente> getById(String id) {
        return repository.findById(id);
    }

    public OpinionCliente save(OpinionCliente opinion) {
        return repository.save(opinion);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }



    public List<Document> obtenerPromedioPuntuacionPorEmpresa() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("empresa_id")
                        .avg("puntuacion").as("promedioPuntuacion"),
                Aggregation.project("promedioPuntuacion").and("empresa_id").previousOperation()
        );

        return mongoTemplate.aggregate(aggregation, "opiniones_clientes", Document.class).getMappedResults();
    }

    public List<Document> buscarOpinionesConPalabrasClave() {
        MatchOperation filtro = Aggregation.match(Criteria.where("comentario").regex("demora|error", "i"));

        Aggregation aggregation = Aggregation.newAggregation(filtro);

        return mongoTemplate.aggregate(aggregation, "opiniones_clientes", Document.class).getMappedResults();
    }

    public List<Document> agruparOpinionesPorHora() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.project("fecha", "puntuacion")
                        .andExpression("hour(fecha)").as("horaDelDia"),
                Aggregation.group("horaDelDia")
                        .count().as("totalOpiniones")
                        .avg("puntuacion").as("promedioPuntuacion"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "_id")) // ordena por hora
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(agg, "opinion_cliente", Document.class);
        return results.getMappedResults();
    }

}

