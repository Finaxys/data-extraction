package com.finaxys.rd.dataextraction.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeyField;


@XmlRootElement(name = "option")
@XmlType(propOrder = {"symbol", "exchSymb", "optionChain", "expiration", "optionType", "strike", "provider", "source", "inputDate", "dataType"})
public class Option  extends ProductData  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8128209219568521667L;



    @HBaseRowKeyField(className = "Option", order = 2)
	private String exchSymb;

    @HBaseRowKeyField(className = "Option", order = 4)
	private String optionChain;
	
	private String optionType;

	private BigDecimal strike;

    @HBaseRowKeyField(className = "Option", order = 3)
	private LocalDate expiration;
	


	public Option() {
		super();
	}







	public Option(char source, DateTime inputDate, String symbol,
			DataType dataType, char provider, String exchSymb,
			String optionChain, String optionType, BigDecimal strike,
			LocalDate expiration) {
		super(source, inputDate, symbol, dataType, provider);
		this.exchSymb = exchSymb;
		this.optionChain = optionChain;
		this.optionType = optionType;
		this.strike = strike;
		this.expiration = expiration;
	}







	public String getOptionChain() {
		return optionChain;
	}


	public void setOptionChain(String optionChain) {
		this.optionChain = optionChain;
	}


	public String getOptionType() {
		return optionType;
	}


	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}


	public BigDecimal getStrike() {
		return strike;
	}


	public void setStrike(BigDecimal strike) {
		this.strike = strike;
	}


	@XmlJavaTypeAdapter(com.finaxys.rd.dataextraction.domain.jaxb.LocalDateAdapter.class)
	public LocalDate getExpiration() {
		return expiration;
	}


	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}

//
//	@XmlJavaTypeAdapter(com.finaxys.rd.dataextraction.domain.jaxb.MonthLocalDateAdapter.class)
//	public LocalDate getInputDate() {
//		return inputDate;
//	}
//
//
//	public void setInputDate(LocalDate inputDate) {
//		this.inputDate = inputDate;
//	}


	public String getExchSymb() {
		return exchSymb;
	}

	public void setExchSymb(String exchSymb) {
		this.exchSymb = exchSymb;
	}


}
