package com.example.demo.Repository.mongo;

import com.example.demo.Entity.mongo.OpinionCliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpinionClienteRepository extends MongoRepository<OpinionCliente, String> {
    // Puedes agregar métodos personalizados luego, por ejemplo:
    List<OpinionCliente> findByEmpresaId(int empresaId);

    Optional<OpinionCliente> findById(int id);

}
