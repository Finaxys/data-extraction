package com.finaxys.rd.dataextraction.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import org.joda.time.DateTime;

import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeyField;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeysFields;

@XmlTransient
public abstract class ProductData extends MarketData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866083217881607082L;

	@HBaseRowKeysFields(rowkeys = {@HBaseRowKeyField(className = "CurrencyPair", order = 1),
                             @HBaseRowKeyField(className = "Index", order = 1),
                             @HBaseRowKeyField(className = "InterbankRate", order = 1),
                             @HBaseRowKeyField(className = "OptionChain", order = 1),
                             @HBaseRowKeyField(className = "Option", order = 1),
                        	 @HBaseRowKeyField(className = "Stock", order = 1)})
	private char provider;

	public ProductData() {
		super();
	}

	public ProductData(char source, DateTime inputDate, String symbol, DataType dataType, char provider) {
		super(source, inputDate, symbol, dataType);
		this.provider = provider;
	}

	public char getProvider() {
		return provider;
	}

	public void setProvider(char provider) {
		this.provider = provider;
	}

}
