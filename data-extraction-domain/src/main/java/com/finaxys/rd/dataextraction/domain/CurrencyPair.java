/*
 * 
 */
package com.finaxys.rd.dataextraction.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

import com.finaxys.rd.dataextraction.domain.Enum.DataType;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyPair.
 */
@XmlRootElement(name = "currency")
@XmlType(propOrder = { "symbol", "baseCurrency", "quoteCurrency", "provider" , "source", "inputDate", "dataType"})
public class CurrencyPair extends ProductData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3823599717828581565L;



	/** The base currency. */
	private String baseCurrency;

	/** The quote currency. */
	private String quoteCurrency;



	public CurrencyPair() {
		super();
	}



	public CurrencyPair(char source, DateTime inputDate, String symbol,
			DataType dataType, char provider, String baseCurrency,
			String quoteCurrency) {
		super(source, inputDate, symbol, dataType, provider);
		this.baseCurrency = baseCurrency;
		this.quoteCurrency = quoteCurrency;
	}



	/**
	 * Gets the base currency.
	 * 
	 * @return the base currency
	 */
	@XmlElement(name = "base")
	public String getBaseCurrency() {
		return baseCurrency;
	}

	/**
	 * Sets the base currency.
	 * 
	 * @param baseCurrency
	 *            the new base currency
	 */
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	/**
	 * Gets the quote currency.
	 * 
	 * @return the quote currency
	 */
	@XmlElement(name = "quote")
	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	/**
	 * Sets the quote currency.
	 * 
	 * @param quoteCurrency
	 *            the new quote currency
	 */
	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}



	@Override
	public String toString() {
		return "CurrencyPair [baseCurrency=" + baseCurrency
				+ ", quoteCurrency=" + quoteCurrency + "]";
	}







}
