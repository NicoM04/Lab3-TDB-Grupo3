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
@Document(collection = "logs_pedidos")
public class LogPedido {
    @Id
    private String id;
    private int idPedido; // FK lógica a pedido relacional
    private List<EstadoPedido> historial; // Lista de estados con timestamps

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EstadoPedido {
        private String estado;
        private Date timestamp;
    }
}
