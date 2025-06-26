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
@Document(collection = "historial_repartidores")
public class HistorialRepartidor {

    @Id
    private String id;
    private int idRepartidor;
    private List<Ubicacion> recorrido;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ubicacion {
        private double lat;
        private double lng;
        private Date timestamp;
    }
}
