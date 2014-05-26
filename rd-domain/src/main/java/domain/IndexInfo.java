package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "indexInfo")
public class IndexInfo {

	private String symbol;
	private String name;
	private String exchSymb;
	private Integer provider;
		
	public IndexInfo() {
		super();
	}

	public IndexInfo(String symbol, String name, String exchSymb, Integer provider) {
		super();
		this.symbol = symbol;
		this.exchSymb = exchSymb;
		this.name = name;
		this.provider = provider;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getExchSymb() {
		return exchSymb;
	}
	public void setExchSymb(String exchSymb) {
		this.exchSymb = exchSymb;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getProvider() {
		return provider;
	}

	public void setProvider(Integer provider) {
		this.provider = provider;
	}
	
	
}
