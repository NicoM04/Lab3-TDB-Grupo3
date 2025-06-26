package com.example.demo.Entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "opiniones_clientes")
public class OpinionCliente {

    @Id
    private String id;

    private String comentario;
    private int puntuacion;
    private Date fecha;

    private int clienteId;   // relación lógica con tabla relacional
    private int empresaId;
}
