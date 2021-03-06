package com.finaxys.rd.dataextraction.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeyField;
import com.finaxys.rd.dataextraction.domain.hbase.HBaseRowKeysFields;

@XmlTransient
public abstract class RateData extends MarketData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866083217881607082L;


	@HBaseRowKeysFields(rowkeys = {@HBaseRowKeyField(className = "FXRate", order = 3),
		                           @HBaseRowKeyField(className = "InterbankRateData", order = 5)})
	private DateTime rateDateTime;
	
	
	public RateData() {
		super();
	}


	public RateData(char source, DateTime inputDate, String symbol,
			DataType dataType, DateTime rateDateTime) {
		super(source, inputDate, symbol, dataType);
		this.rateDateTime = rateDateTime;
	}


	@XmlJavaTypeAdapter(com.finaxys.rd.dataextraction.domain.jaxb.DateTimeAdapter.class)
	public DateTime getRateDateTime() {
		return rateDateTime;
	}


	public void setRateDateTime(DateTime rateDateTime) {
		this.rateDateTime = rateDateTime;
	}

	

}
