package domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "currency")
public class CurrencyPair {

	private String symbol;
	private String baseCurrency;
	private String quoteCurrency;
	
	public CurrencyPair() {
		super();
	}

	public CurrencyPair(String symbol, String baseCurrency, String quoteCurrency) {
		super();
		this.symbol = symbol;
		this.baseCurrency = baseCurrency;
		this.quoteCurrency = quoteCurrency;
	}
	@XmlElement(name = "symbol")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	@XmlElement(name = "base")
	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	@XmlElement(name = "quote")
	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}	
	
}
