package com.example.demo.Controller.mongo;

import com.example.demo.Entity.mongo.LogPedido;
import com.example.demo.Service.mongo.LogPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongo/logs-pedidos")
public class LogPedidoController {

    @Autowired
    private LogPedidoService service;

    @GetMapping
    public List<LogPedido> getAll() {
        return service.getAll();
    }

    @GetMapping("/pedido/{idPedido}")
    public List<LogPedido> getByPedido(@PathVariable int idPedido) {
        return service.getByPedido(idPedido);
    }

    @PostMapping
    public LogPedido save(@RequestBody LogPedido log) {
        return service.save(log);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/cambios-mayor-3-en-10min")
    public long contarPedidosConMasDe3CambiosEn10Min() {
        return service.contarPedidosConMasDe3CambiosEn10Min();
    }
}
