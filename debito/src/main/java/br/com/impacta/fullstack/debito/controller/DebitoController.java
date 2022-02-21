package br.com.impacta.fullstack.debito.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.impacta.fullstack.debito.model.Debito;
import br.com.impacta.fullstack.debito.service.DebitoService;

@RestController
@RequestMapping("/api/v1/debito")
public class DebitoController {

    private final DebitoService debitoService;

    public DebitoController(DebitoService debitoService) {
        this.debitoService = debitoService;
    }

    @GetMapping
    public List<Debito> list() throws UnknownHostException {
        System.out.println("Hostname: " + InetAddress.getLocalHost().getHostName());
        return debitoService.list();
    }
}