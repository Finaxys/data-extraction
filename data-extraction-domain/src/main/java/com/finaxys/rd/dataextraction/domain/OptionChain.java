package com.finaxys.rd.dataextraction.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeyField;


@XmlRootElement(name = "optionChain")
@XmlType(propOrder = {"symbol", "expiration", "provider", "source", "inputDate", "dataType"})
public class OptionChain  extends ProductData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8241203495855981504L;


    @HBaseRowKeyField(className = "OptionChain", order = 3)
	private LocalDate expiration;
	

	public OptionChain() {
		super();
	}


	public OptionChain(char source, DateTime inputDate, String symbol,
			DataType dataType, char provider, LocalDate expiration) {
		super(source, inputDate, symbol, dataType, provider);
		this.expiration = expiration;
	}


	@XmlJavaTypeAdapter(com.finaxys.rd.dataextraction.domain.jaxb.MonthLocalDateAdapter.class)
	public LocalDate getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}

//	@XmlJavaTypeAdapter(com.finaxys.rd.dataextraction.domain.jaxb.MonthLocalDateAdapter.class)
//	public LocalDate getInputDate() {
//		return inputDate;
//	}
//
//	public void setInputDate(LocalDate inputDate) {
//		this.inputDate = inputDate;
//	}



	

}
