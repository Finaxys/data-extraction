package domain;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import msg.Document.DataType;

import org.apache.hadoop.hbase.util.Bytes;


@XmlRootElement(name = "quote")
public class IndexQuote {
	private String symbol;
	private Integer provider;
	private BigDecimal lastTradePriceOnly;
	private Long ts;
	private BigDecimal change;
	private BigDecimal open;
	private BigDecimal daysHigh;
	private BigDecimal daysLow;
	private Integer volume;
	private DataType dataType;


	public IndexQuote() {
		super();
	}

	public IndexQuote(String symbol, Integer provider, BigDecimal lastTradePriceOnly, Long ts, BigDecimal change,
			BigDecimal open, BigDecimal daysHigh, BigDecimal daysLow, Integer volume, DataType dataType) {
		super();
		this.symbol = symbol;
		this.provider = provider;
		this.lastTradePriceOnly = lastTradePriceOnly;
		this.ts = ts;
		this.change = change;
		this.open = open;
		this.daysHigh = daysHigh;
		this.daysLow = daysLow;
		this.volume = volume;
		this.dataType = dataType;
	}

	@XmlElement(name = "Symbol")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@XmlElement(name = "Provider")
	public Integer getProvider() {
		return provider;
	}

	public void setProvider(Integer provider) {
		this.provider = provider;
	}

	@XmlElement(name = "LastTradePriceOnly")
	public BigDecimal getLastTradePriceOnly() {
		return lastTradePriceOnly;
	}

	public void setLastTradePriceOnly(BigDecimal lastTradePriceOnly) {
		this.lastTradePriceOnly = lastTradePriceOnly;
	}

	@XmlElement(name = "Ts")
	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	@XmlElement(name = "Change")
	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	@XmlElement(name = "Open")
	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	@XmlElement(name = "DaysHigh")
	public BigDecimal getDaysHigh() {
		return daysHigh;
	}

	public void setDaysHigh(BigDecimal daysHigh) {
		this.daysHigh = daysHigh;
	}

	@XmlElement(name = "DaysLow")
	public BigDecimal getDaysLow() {
		return daysLow;
	}

	public void setDaysLow(BigDecimal daysLow) {
		this.daysLow = daysLow;
	}

	@XmlElement(name = "Volume")
	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	@XmlElement(name = "Type")
	public DataType getType() {
		return dataType;
	}

	public void setType(DataType dataType) {
		this.dataType = dataType;
	}

}
