package com.example.demo.Service.mongo;

import com.example.demo.Entity.mongo.NavegacionUsuario;
import com.example.demo.Repository.mongo.NavegacionUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;


import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.bson.Document;
import java.util.stream.Collectors;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;



@Service
public class NavegacionUsuarioService {

    @Autowired
    private NavegacionUsuarioRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public List<NavegacionUsuario> getAll() {
        return repository.findAll();
    }

    public NavegacionUsuario save(NavegacionUsuario nav) {
        return repository.save(nav);
    }

    public List<NavegacionUsuario> getByCliente(int idCliente) {
        return repository.findByIdCliente(idCliente);
    }

    public List<NavegacionUsuario> getByTipoEvento(String tipo) {
        return repository.findByTipoEvento(tipo);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Integer> detectarClientesSinCompraEnUltimos7Dias() {
        Date sieteDiasAtras = new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000);

        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("fechaEvento").gte(sieteDiasAtras)),
                match(Criteria.where("tipoEvento").is("busqueda")),
                group("idCliente").first("idCliente").as("idCliente")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(agg, "navegacion_usuarios", Document.class);

        return results.getMappedResults()
                .stream()
                .map(doc -> doc.getInteger("idCliente"))
                .collect(Collectors.toList());
    }


}
