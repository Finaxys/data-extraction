package com.finaxys.rd.dataextraction.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeyField;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeysFields;

@XmlTransient
public abstract class ExtractedData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2214146820730387828L;

	/** The source. */

	@HBaseRowKeysFields(rowkeys = { @HBaseRowKeyField(className = "CurrencyPair", order = 4),
			                  @HBaseRowKeyField(className = "FXRate", order = 4), 
			                  @HBaseRowKeyField(className = "Exchange", order = 4),
			                  @HBaseRowKeyField(className = "Index", order = 4),
			                  @HBaseRowKeyField(className = "IndexQuote", order = 5),
			                  @HBaseRowKeyField(className = "InterbankRate", order = 4),
			                  @HBaseRowKeyField(className = "InterbankRateData", order = 6),
			                  @HBaseRowKeyField(className = "OptionChain", order = 4),
			                  @HBaseRowKeyField(className = "Option", order = 6),
			             	  @HBaseRowKeyField(className = "OptionQuote", order = 5),
			             	 @HBaseRowKeyField(className = "Stock", order = 4),
			            	 @HBaseRowKeyField(className = "StockQuote", order = 5)})
	private char source;

	/** The input date. */
	@HBaseRowKeysFields(rowkeys = { @HBaseRowKeyField(className = "CurrencyPair", order = 2), 
			                  @HBaseRowKeyField(className = "FXRate", order = 5), 
			                  @HBaseRowKeyField(className = "Exchange", order = 5),
			                  @HBaseRowKeyField(className = "Index", order = 5),
			                  @HBaseRowKeyField(className = "IndexQuote", order = 6),
			                  @HBaseRowKeyField(className = "InterbankRate", order = 5),
			                  @HBaseRowKeyField(className = "InterbankRateData", order = 7),
			                  @HBaseRowKeyField(className = "OptionChain", order = 5),
			                  @HBaseRowKeyField(className = "Option", order = 7),
			             	  @HBaseRowKeyField(className = "OptionQuote", order = 6),
			             	  @HBaseRowKeyField(className = "Stock", order = 5),
			             	 @HBaseRowKeyField(className = "StockQuote", order = 6) })
	private DateTime inputDate;

	public ExtractedData() {
		super();
	}

	public ExtractedData(char source, DateTime inputDate) {
		super();
		this.source = source;
		this.inputDate = inputDate;
	}

	@XmlElement(name = "source")
	public char getSource() {
		return source;
	}

	public void setSource(char source) {
		this.source = source;
	}

	@XmlElement(name = "inputDate")
	@XmlJavaTypeAdapter(com.finaxys.rd.dataextraction.domain.jaxb.DateTimeAdapter.class)
	public DateTime getInputDate() {
		return inputDate;
	}

	public void setInputDate(DateTime inputDate) {
		this.inputDate = inputDate;
	}

}
