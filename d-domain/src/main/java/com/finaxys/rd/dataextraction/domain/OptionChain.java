package com.finaxys.rd.dataextraction.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.LocalDate;


@XmlRootElement(name = "optionChain")
@XmlType(propOrder = {"symbol", "expiration", "inputDate", "provider"})
public class OptionChain {

	private String symbol;
	
	private LocalDate expiration;
	
	private LocalDate inputDate;
	
	private char provider;
	
	public OptionChain() {
		super();
	}

	
	public OptionChain(String symbol, LocalDate expiration,
			LocalDate inputDate, char provider) {
		super();
		this.symbol = symbol;
		this.expiration = expiration;
		this.inputDate = inputDate;
		this.provider = provider;
	}



	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public LocalDate getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}

	public LocalDate getInputDate() {
		return inputDate;
	}

	public void setInputDate(LocalDate inputDate) {
		this.inputDate = inputDate;
	}

	public char getProvider() {
		return provider;
	}

	public void setProvider(char provider) {
		this.provider = provider;
	}




}
