package com.finaxys.rd.dataextraction.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeyField;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeysFields;

@XmlTransient
public abstract class QuoteData extends MarketData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866083217881607082L;


    @HBaseRowKeysFields(rowkeys = {@HBaseRowKeyField(className = "IndexQuote", order = 4),
	                               @HBaseRowKeyField(className = "OptionQuote", order = 5),
	                          	   @HBaseRowKeyField(className = "StockQuote", order = 4)})
	private DateTime quoteDateTime;

	
	public QuoteData() {
		super();
	}





	public QuoteData(char source, DateTime inputDate, String symbol,
			DataType dataType, DateTime quoteDateTime) {
		super(source, inputDate, symbol, dataType);
		this.quoteDateTime = quoteDateTime;
	}





	@XmlJavaTypeAdapter(com.finaxys.rd.dataextraction.domain.jaxb.DateTimeAdapter.class)
	public DateTime getQuoteDateTime() {
		return quoteDateTime;
	}


	public void setQuoteDateTime(DateTime quoteDateTime) {
		this.quoteDateTime = quoteDateTime;
	}

	
}
