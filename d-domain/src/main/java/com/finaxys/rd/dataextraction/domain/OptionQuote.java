package com.finaxys.rd.dataextraction.domain;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;


@XmlRootElement(name = "option")
@XmlType(propOrder = {"symbol", "type", "price", "change", "prevClose", "open", "bid", "ask", "strike", "expiration", "openInterest", "ts", "provider"})
public class OptionQuote {

	private String symbol;
	
	private String type;
	
	private BigDecimal price;
	
	private BigDecimal change;
	
	private BigDecimal prevClose;

	private BigDecimal open;

	private BigDecimal bid;

	private BigDecimal ask;

	private BigDecimal strike;

	private LocalDate expiration;

	private BigInteger volume;

	private Integer openInterest;
	
	private DateTime ts;
	
	private char provider;
	
	/** The data type. */
	private DataType dataType;
	
	public OptionQuote() {
		super();
	}




	public OptionQuote(String symbol, String type, BigDecimal price, BigDecimal change, BigDecimal prevClose, BigDecimal open, BigDecimal bid, BigDecimal ask, BigDecimal strike,
			LocalDate expiration, BigInteger volume, Integer openInterest, DateTime ts, char provider, DataType dataType) {
		super();
		this.symbol = symbol;
		this.type = type;
		this.price = price;
		this.change = change;
		this.prevClose = prevClose;
		this.open = open;
		this.bid = bid;
		this.ask = ask;
		this.strike = strike;
		this.expiration = expiration;
		this.volume = volume;
		this.openInterest = openInterest;
		this.ts = ts;
		this.provider = provider;
		this.dataType = dataType;
	}




	public DataType getDataType() {
		return dataType;
	}




	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}




	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	public BigDecimal getPrevClose() {
		return prevClose;
	}

	public void setPrevClose(BigDecimal prevClose) {
		this.prevClose = prevClose;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	public BigDecimal getAsk() {
		return ask;
	}

	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}

	public BigDecimal getStrike() {
		return strike;
	}

	public void setStrike(BigDecimal strike) {
		this.strike = strike;
	}

	public LocalDate getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}

	public BigInteger getVolume() {
		return volume;
	}

	public void setVolume(BigInteger volume) {
		this.volume = volume;
	}

	public Integer getOpenInterest() {
		return openInterest;
	}

	public void setOpenInterest(Integer openInterest) {
		this.openInterest = openInterest;
	}

	public DateTime getTs() {
		return ts;
	}

	public void setTs(DateTime ts) {
		this.ts = ts;
	}

	public char getProvider() {
		return provider;
	}

	public void setProvider(char provider) {
		this.provider = provider;
	}

	


}
