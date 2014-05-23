package domain;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.hadoop.hbase.util.Bytes;

@XmlRootElement(name = "exchange")
public class Exchange {

	private String mic;
	private String symbol;
	private Integer provider;
	private String name;
	private String type;
	// private Map<String,String> dataProviderSymbol;
	private String continent;
	private String country;
	private String currency;
	private long openTime;
	private long closeTime;
	// private long inputDate;
	private boolean status;

	public Exchange() {
		super();
	}

	// public Exchange(String mic, String name, String type, String continent,
	// String country,
	// String currency, long openTime, long closeTime, long inputDate, boolean
	// status, Map<String,String> dataProviderSymbol) {
	//
	public Exchange(String mic, String symbol, Integer provider, String name, String type, String continent,
			String country, String currency, long openTime, long closeTime, boolean status) {
		super();
		this.mic = mic;
		this.symbol = symbol;
		this.name = name;
		this.type = type;
		this.continent = continent;
		this.country = country;
		this.currency = currency;
		this.openTime = openTime;
		this.closeTime = closeTime;
		// this.inputDate = inputDate;
		this.status = status;
	}

	public Exchange(byte[] mic, byte[] symbol, byte[] provider, byte[] name, byte[] type, byte[] continent,
			byte[] country, byte[] currency, byte[] openTime, byte[] closeTime, byte[] status) {
		this(Bytes.toString(mic), Bytes.toString(symbol), Bytes.toInt(provider), Bytes.toString(name), Bytes
				.toString(type), Bytes.toString(continent), Bytes.toString(country), Bytes.toString(currency), Bytes
				.toLong(openTime), Bytes.toLong(closeTime), Bytes.toBoolean(status));
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}

	public long getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(long closeTime) {
		this.closeTime = closeTime;
	}

	// public long getInputDate() {
	// return inputDate;
	// }
	// public void setInputDate(long inputDate) {
	// this.inputDate = inputDate;
	// }
	// public Map<String,String> getDataProviderSymbol() {
	// return dataProviderSymbol;
	// }
	// public void setDataProviderSymbol(Map<String,String> dataProviderSymbol)
	// {
	// this.dataProviderSymbol = dataProviderSymbol;
	// }

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getProvider() {
		return provider;
	}

	public void setProvider(Integer provider) {
		this.provider = provider;
	}

}
