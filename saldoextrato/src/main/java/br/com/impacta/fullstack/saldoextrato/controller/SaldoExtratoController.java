package br.com.impacta.fullstack.saldoextrato.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.impacta.fullstack.saldoextrato.model.SaldoExtrato;
import br.com.impacta.fullstack.saldoextrato.service.SaldoExtratoService;
import brave.Span;
import brave.Tracer;

@RestController
@RequestMapping(("/api/v1/saldoextrato"))
public class SaldoExtratoController {

    private final SaldoExtratoService saldoExtratoService;
    private final Tracer tracer;
    
    private final Logger LOGGER = LoggerFactory.getLogger(SaldoExtratoController.class);

    public SaldoExtratoController(SaldoExtratoService saldoExtratoService, Tracer tracer) {
        this.saldoExtratoService = saldoExtratoService;
		this.tracer = tracer;
    }

    @GetMapping
    public SaldoExtrato get() throws UnknownHostException {
    	Span newSpan = tracer.nextSpan().name("saldoextrato").start();
    	
    	if (LOGGER.isInfoEnabled()) {
    		LOGGER.info("Hostname: " + InetAddress.getLocalHost().getHostName());
    	}
    	
    	SaldoExtrato saldoExtrato = saldoExtratoService.get();
    	
    	newSpan.finish();
    	
    	return saldoExtrato;
    }
    
    @GetMapping
    @RequestMapping("/mobile")
    public SaldoExtrato getBff() throws UnknownHostException {
    	if (LOGGER.isInfoEnabled()) {
    		LOGGER.info("Hostname: " + InetAddress.getLocalHost().getHostName());
    	}
    	
        return saldoExtratoService.getBff();
    }

}
