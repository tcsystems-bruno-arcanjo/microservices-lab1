package br.com.impacta.fullstack.saldoextrato.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.impacta.fullstack.saldoextrato.model.Credito;
import br.com.impacta.fullstack.saldoextrato.model.Debito;
import br.com.impacta.fullstack.saldoextrato.model.SaldoExtrato;
import brave.Span;
import brave.Tracer;

@Service
public class SaldoExtratoService {
	
	private final DiscoveryClient discoveryClient;
	private final Tracer tracer;

    public SaldoExtratoService(DiscoveryClient discoveryClient, Tracer tracer) {
		this.discoveryClient = discoveryClient;
		this.tracer = tracer;
	}

    private final Logger LOGGER = LoggerFactory.getLogger(SaldoExtratoService.class);

    public SaldoExtrato get() {
    	Span newSpan = tracer.nextSpan().name("saldoextrato").start();
        RestTemplate restTemplate = new RestTemplate();
        
        //Get Credito
        ServiceInstance serviceInstance = discoveryClient.getInstances("credito").get(0);
        String creditoUrl = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/api/v1/credito";
        ResponseEntity<Credito[]> creditoResponse = restTemplate.getForEntity(creditoUrl, Credito[].class);
        
        if (LOGGER.isInfoEnabled()) {
        	LOGGER.info("CREDITO_API_URL: " + creditoUrl);
        }
        
        List<Credito> creditoList = Arrays.asList(creditoResponse.getBody());
        SaldoExtrato saldoExtrato = new SaldoExtrato();
        saldoExtrato.setCreditoList(creditoList);
        
        //Get Debito
        serviceInstance = discoveryClient.getInstances("debito").get(0);
        String debitoUrl = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/api/v1/debito";
        ResponseEntity<Debito[]> debitoResponse = restTemplate.getForEntity(debitoUrl, Debito[].class);
        
        if (LOGGER.isInfoEnabled()) {
        	LOGGER.info("DEBITO_API_URL: " + creditoUrl);
        }
        
        List<Debito> debitoList = Arrays.asList(debitoResponse.getBody());
        saldoExtrato.setDebitoList(debitoList);
        
        //Calcular saldo
        saldoExtrato.setSaldo(calculateSaldo(creditoList, debitoList));
        
        newSpan.finish();
        
        return saldoExtrato;
    }
    
    @HystrixCommand(fallbackMethod = "fallbackBff")
    public SaldoExtrato getBff(){
        RestTemplate restTemplate = new RestTemplate();
        //Get Credito
        
        ServiceInstance serviceInstance = discoveryClient.getInstances("credito").get(0);
        String creditoUrl = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/api/v1/credito";
        ResponseEntity<Credito[]> creditoResponse = restTemplate.getForEntity(creditoUrl, Credito[].class);
        
        if (LOGGER.isInfoEnabled()) {
        	LOGGER.info("CREDITO_API_URL: " + creditoUrl);
        }
        
        List<Credito> creditoList = Arrays.asList(creditoResponse.getBody());
        
        //Get Debito
        serviceInstance = discoveryClient.getInstances("debito").get(0);
        String debitoUrl = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/api/v1/debito";
        ResponseEntity<Debito[]> debitoResponse = restTemplate.getForEntity(debitoUrl, Debito[].class);
        
        if (LOGGER.isInfoEnabled()) {
        	LOGGER.info("DEBITO_API_URL: " + debitoUrl);
        }
        
        List<Debito> debitoList = Arrays.asList(debitoResponse.getBody());
        
        //Calcular saldo
        SaldoExtrato saldoExtrato = new SaldoExtrato();
        saldoExtrato.setSaldo(calculateSaldo(creditoList, debitoList));
        
        return saldoExtrato;
    }

    private BigDecimal calculateSaldo(List<Credito> creditoList, List<Debito> debitoList) {
        BigDecimal creditoSum = creditoList.stream().map(Credito::getCredito).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal debitoSum = debitoList.stream().map(Debito::getDebito).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal saldo = creditoSum.add(debitoSum);
        
        return saldo;
    }

    public SaldoExtrato fallbackBff(){
        SaldoExtrato saldoExtratoFallBack = new SaldoExtrato();
        saldoExtratoFallBack.setSaldo(new BigDecimal(0));
        return saldoExtratoFallBack;
    }
}
