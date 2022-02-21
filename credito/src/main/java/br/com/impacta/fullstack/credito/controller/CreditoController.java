package br.com.impacta.fullstack.credito.controller;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.impacta.fullstack.credito.model.Credito;
import br.com.impacta.fullstack.credito.service.CreditoService;

@RestController
@RequestMapping("/api/v1/credito")
public class CreditoController {

	private final CreditoService creditoService;

	public CreditoController(CreditoService creditoService) {
		this.creditoService = creditoService;
	}

	@GetMapping
	public List<Credito> list() throws UnknownHostException {
		return creditoService.list();
	}

}