package com.example.demo.Entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "navegacion_usuarios")
public class NavegacionUsuario {
    @Id
    private String id;

    private int idCliente; // FK lógica
    private Date fechaEvento;

    private String tipoEvento; // ejemplo: "busqueda", "clic", "filtro"
    private String descripcion; // texto libre del evento
    private List<String> filtrosAplicados; // puede ser null o vacío si no aplica
}
