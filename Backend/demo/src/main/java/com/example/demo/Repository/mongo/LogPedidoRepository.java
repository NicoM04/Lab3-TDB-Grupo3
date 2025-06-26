package com.example.demo.Repository.mongo;

import com.example.demo.Entity.mongo.LogPedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogPedidoRepository extends MongoRepository<LogPedido, String> {
    List<LogPedido> findByIdPedido(int idPedido);
}
