package com.finaxys.rd.dataextraction.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.joda.time.DateTime;

import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeyField;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeysFields;

@XmlTransient
public abstract class MarketData extends ExtractedData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866083217881607082L;

	@HBaseRowKeysFields(rowkeys = {@HBaseRowKeyField(className = "CurrencyPair", order = 3),
			                            @HBaseRowKeyField(className = "FXRate", order = 2),
			                            @HBaseRowKeyField(className = "Exchange", order = 3),
			                            @HBaseRowKeyField(className = "Index", order = 3),
			                            @HBaseRowKeyField(className = "IndexQuote", order = 2),
			                            @HBaseRowKeyField(className = "InterbankRate", order = 2),
			                            @HBaseRowKeyField(className = "InterbankRateData", order = 2),
			                            @HBaseRowKeyField(className = "OptionChain", order = 2),
			                            @HBaseRowKeyField(className = "Option", order = 5),
			                       	    @HBaseRowKeyField(className = "OptionQuote", order = 4),
			                       	    @HBaseRowKeyField(className = "Stock", order = 3),
			                       	 @HBaseRowKeyField(className = "StockQuote", order = 3)})
	private String symbol;

	@HBaseRowKeysFields(rowkeys = {@HBaseRowKeyField(className = "FXRate", order = 1),
		                           @HBaseRowKeyField(className = "IndexQuote", order = 1),
		                           @HBaseRowKeyField(className = "InterbankRateData", order = 1),
		                      	   @HBaseRowKeyField(className = "OptionQuote", order = 1),
		                      	 @HBaseRowKeyField(className = "StockQuote", order = 1)})
	private DataType dataType;

	
	public MarketData() {
		super();
	}

	public MarketData(char source, DateTime inputDate, String symbol,
			DataType dataType) {
		super(source, inputDate);
		this.symbol = symbol;
		this.dataType = dataType;
	}

	@XmlElement(name = "symbol")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@XmlElement(name = "dataType")
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

}
