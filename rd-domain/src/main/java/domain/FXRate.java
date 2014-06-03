package domain;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import msg.Document.DataType;

import org.apache.hadoop.hbase.util.Bytes;


@XmlRootElement(name = "rate")
public class FXRate {
	private String symbol;
	private BigDecimal rate;
	private BigDecimal ask;
	private BigDecimal bid;
	private char provider;
	private Long ts;
	private DataType dataType;

	

	public FXRate() {
		super();
	}

	public FXRate(String symbol, BigDecimal rate, BigDecimal ask, BigDecimal bid, char provider, Long ts, DataType dataType) {
		super();
		this.symbol = symbol;
		this.rate = rate;
		this.ask = ask;
		this.bid = bid;
		this.provider = provider;
		this.ts = ts;
		this.dataType = dataType;
	}

	@XmlElement(name = "Symbol")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@XmlElement(name = "Rate")
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	@XmlElement(name = "Ask")
	public BigDecimal getAsk() {
		return ask;
	}

	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}

	@XmlElement(name = "Bid")
	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	@XmlElement(name = "Provider")
	public char getProvider() {
		return provider;
	}

	public void setProvider(char provider) {
		this.provider = provider;
	}

	@XmlElement(name = "ts")
	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	@XmlElement(name = "DataType")
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

}
