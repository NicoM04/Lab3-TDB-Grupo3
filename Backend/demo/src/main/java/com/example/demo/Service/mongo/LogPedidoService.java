package com.example.demo.Service.mongo;

import com.example.demo.Entity.mongo.LogPedido;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.example.demo.Repository.mongo.LogPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@Service
public class LogPedidoService {

    @Autowired
    private LogPedidoRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<LogPedido> getAll() {
        return repository.findAll();
    }

    public LogPedido save(LogPedido log) {
        return repository.save(log);
    }

    public List<LogPedido> getByPedido(int idPedido) {
        return repository.findByIdPedido(idPedido);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public long contarPedidosConMasDe3CambiosEn10Min() {
        Aggregation agg = newAggregation(
                unwind("historial"),
                group("idPedido")
                        .count().as("totalCambios")
                        .min("historial.timestamp").as("minTime")
                        .max("historial.timestamp").as("maxTime"),
                addFields()
                        .addFieldWithValue("diffMillis",
                                ArithmeticOperators.Subtract.valueOf("maxTime").subtract("minTime")
                        ).build(),
                match(Criteria.where("totalCambios").gt(3)
                        .and("diffMillis").lte(10 * 60 * 1000)),
                count().as("totalPedidos")
        );

        AggregationResults<ContadorResultado> results =
                mongoTemplate.aggregate(agg, "logs_pedidos", ContadorResultado.class);

        ContadorResultado res = results.getUniqueMappedResult();
        return res != null ? res.getTotalPedidos() : 0L;
    }

    static class ContadorResultado {
        private long totalPedidos;
        public long getTotalPedidos() { return totalPedidos; }
        public void setTotalPedidos(long totalPedidos) { this.totalPedidos = totalPedidos; }
    }
}
