package com.example.demo.Repository.mongo;

import com.example.demo.Entity.mongo.HistorialRepartidor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialRepartidorRepository extends MongoRepository<HistorialRepartidor, String> {
    List<HistorialRepartidor> findByIdRepartidor(int idRepartidor);
}
