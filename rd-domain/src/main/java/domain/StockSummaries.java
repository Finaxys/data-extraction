package domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="stockSummaries")
public class StockSummaries {

	private List<StockSummary> stocksList;

	@XmlElementWrapper
	@XmlElement(name = "stock")
	public List<StockSummary> getStocksList() {
		if (stocksList == null)
			stocksList = new ArrayList<StockSummary>();
		return stocksList;
	}

	public void setExchangeList(List<StockSummary> stocksList) {
		this.stocksList = stocksList;
	}
}
