package com.example.demo.Controller.mongo;

import com.example.demo.Entity.mongo.NavegacionUsuario;
import com.example.demo.Service.mongo.NavegacionUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongo/navegacion-usuarios")
public class NavegacionUsuarioController {

    @Autowired
    private NavegacionUsuarioService service;

    @GetMapping
    public List<NavegacionUsuario> getAll() {
        return service.getAll();
    }

    @GetMapping("/cliente/{id}")
    public List<NavegacionUsuario> getByCliente(@PathVariable int id) {
        return service.getByCliente(id);
    }

    @GetMapping("/tipo/{tipo}")
    public List<NavegacionUsuario> getByTipo(@PathVariable String tipo) {
        return service.getByTipoEvento(tipo);
    }

    @PostMapping
    public NavegacionUsuario save(@RequestBody NavegacionUsuario nav) {
        return service.save(nav);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/clientes-sin-compra")
    public List<Integer> detectarClientesSinCompra() {
        return service.detectarClientesSinCompraEnUltimos7Dias();
    }
}
