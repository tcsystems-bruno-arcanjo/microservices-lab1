package br.com.impacta.fullstack.credito.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Credito implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal credito;

	public Credito() {
		super();
	}

	public Credito(BigDecimal credito) {
		this.credito = credito;
	}

	public BigDecimal getCredito() {
		return credito;
	}

	public void setCredito(BigDecimal credito) {
		this.credito = credito;
	}

	@Override
	public String toString() {
		return "Credito [credito=" + credito + "]";
	}

}
