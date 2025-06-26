package com.example.demo.Service.mongo;

import com.example.demo.Entity.mongo.LogPedido;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.example.demo.Repository.mongo.LogPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.count;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;


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
        Date diezMinutosAtras = new Date(System.currentTimeMillis() - 10 * 60 * 1000);

        Aggregation agg = newAggregation(
                match(Criteria.where("timestamp").gte(diezMinutosAtras)),
                group("idPedido").count().as("totalCambios"),
                match(Criteria.where("totalCambios").gt(3)),
                count().as("totalPedidos")
        );

        AggregationResults<ContadorResultado> results = mongoTemplate.aggregate(agg, "logs_pedidos", ContadorResultado.class);
        return results.getUniqueMappedResult() != null ? results.getUniqueMappedResult().getTotalPedidos() : 0;
    }

    static class ContadorResultado {
        private long totalPedidos;
        public long getTotalPedidos() {
            return totalPedidos;
        }
        public void setTotalPedidos(long totalPedidos) {
            this.totalPedidos = totalPedidos;
        }
    }
}
