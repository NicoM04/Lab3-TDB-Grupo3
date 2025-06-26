package com.example.demo.Repository.mongo;

import com.example.demo.Entity.mongo.NavegacionUsuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NavegacionUsuarioRepository extends MongoRepository<NavegacionUsuario, String> {
    List<NavegacionUsuario> findByIdCliente(int idCliente);
    List<NavegacionUsuario> findByTipoEvento(String tipoEvento);
}
