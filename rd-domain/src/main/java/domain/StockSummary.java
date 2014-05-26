package domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stock")
public class StockSummary {

	
	private String symbol;
	private String exchSymb;
	private Integer provider;
	private String companyName;
	private Date start;
	private Date end;
	private String sector;
	private String industry;
	private Integer fullTimeEmployees;

	public StockSummary() {
		super();
	}

	public StockSummary(String symbol, String exchSymb, Integer provider, String companyName, Date start, Date end, String sector, String industry,
			Integer fullTimeEmployees) {
		super();
		this.exchSymb = exchSymb;
		this.provider = provider;
		this.symbol = symbol;
		this.companyName = companyName;
		this.start = start;
		this.end = end;
		this.sector = sector;
		this.industry = industry;
		this.fullTimeEmployees = fullTimeEmployees;
	}
	@XmlElement(name = "Symbol")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	@XmlElement(name = "CompanyName")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@XmlElement(name = "start")
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	@XmlElement(name = "end")
	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	@XmlElement(name = "Sector")
	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		sector = sector;
	}
	@XmlElement(name = "Industry")
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}
	@XmlElement(name = "FullTimeEmployees")
	public Integer getFullTimeEmployees() {
		return fullTimeEmployees;
	}

	public void setFullTimeEmployees(Integer fullTimeEmployees) {
		this.fullTimeEmployees = fullTimeEmployees;
	}
	@XmlElement(name = "ExchSymb")
	public String getExchSymb() {
		return exchSymb;
	}

	public void setExchSymb(String exchSymb) {
		this.exchSymb = exchSymb;
	}
	@XmlElement(name = "Provider")
	public Integer getProvider() {
		return provider;
	}

	public void setProvider(Integer provider) {
		this.provider = provider;
	}

}
