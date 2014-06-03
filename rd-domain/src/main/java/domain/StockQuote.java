package domain;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import msg.Document.DataType;

import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;

@XmlRootElement(name = "quote")
public class StockQuote {

	private String symbol;
	private String exchSymb;
	private char provider;
	private Integer averageDailyVolume;
	private BigDecimal change;
	private BigDecimal daysLow;
	private BigDecimal daysHigh;
	private BigDecimal yearLow;
	private BigDecimal yearHigh;
	private String marketCapitalization;
	private BigDecimal lastTradePriceOnly;
	private String daysRange;
	private String name;
	private Integer volume;
	private Long ts;
	private DataType dataType;

	
	public StockQuote() {
		super();
	}

	public StockQuote(String symbol, String exchSymb, char provider, Integer averageDailyVolume, BigDecimal change,
			BigDecimal daysLow, BigDecimal daysHigh, BigDecimal yearLow, BigDecimal yearHigh,
			String marketCapitalization, BigDecimal lastTradePriceOnly, String daysRange, String name, Integer volume,
			Long ts, DataType dataType) {
		super();
		this.symbol = symbol;
		this.exchSymb = exchSymb;
		this.provider = provider;
		this.averageDailyVolume = averageDailyVolume;
		this.change = change;
		this.daysLow = daysLow;
		this.daysHigh = daysHigh;
		this.yearLow = yearLow;
		this.yearHigh = yearHigh;
		this.marketCapitalization = marketCapitalization;
		this.lastTradePriceOnly = lastTradePriceOnly;
		this.daysRange = daysRange;
		this.name = name;
		this.volume = volume;
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

	@XmlElement(name = "ExchSymb")
	public String getExchSymb() {
		return exchSymb;
	}

	public void setExchSymb(String exchSymb) {
		this.exchSymb = exchSymb;
	}

	@XmlElement(name = "Provider")
	public char getProvider() {
		return provider;
	}

	public void setProvider(char provider) {
		this.provider = provider;
	}

	@XmlElement(name = "AverageDailyVolume")
	public Integer getAverageDailyVolume() {
		return averageDailyVolume;
	}

	public void setAverageDailyVolume(Integer averageDailyVolume) {
		this.averageDailyVolume = averageDailyVolume;
	}

	@XmlElement(name = "Change")
	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	@XmlElement(name = "DaysLow")
	public BigDecimal getDaysLow() {
		return daysLow;
	}

	public void setDaysLow(BigDecimal daysLow) {
		this.daysLow = daysLow;
	}

	@XmlElement(name = "DaysHigh")
	public BigDecimal getDaysHigh() {
		return daysHigh;
	}

	public void setDaysHigh(BigDecimal daysHigh) {
		this.daysHigh = daysHigh;
	}

	@XmlElement(name = "YearLow")
	public BigDecimal getYearLow() {
		return yearLow;
	}

	public void setYearLow(BigDecimal yearLow) {
		this.yearLow = yearLow;
	}

	@XmlElement(name = "YearHigh")
	public BigDecimal getYearHigh() {
		return yearHigh;
	}

	public void setYearHigh(BigDecimal yearHigh) {
		this.yearHigh = yearHigh;
	}

	@XmlElement(name = "MarketCapitalization")
	public String getMarketCapitalization() {
		return marketCapitalization;
	}

	public void setMarketCapitalization(String marketCapitalization) {
		this.marketCapitalization = marketCapitalization;
	}

	@XmlElement(name = "LastTradePriceOnly")
	public BigDecimal getLastTradePriceOnly() {
		return lastTradePriceOnly;
	}

	public void setLastTradePriceOnly(BigDecimal lastTradePriceOnly) {
		this.lastTradePriceOnly = lastTradePriceOnly;
	}

	@XmlElement(name = "DaysRange")
	public String getDaysRange() {
		return daysRange;
	}

	public void setDaysRange(String daysRange) {
		this.daysRange = daysRange;
	}

	@XmlElement(name = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "Volume")
	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
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
