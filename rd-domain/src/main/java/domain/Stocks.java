package domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="stocks")
public class Stocks {

	private List<Stock> stocksList;

	@XmlElementWrapper(name = "stocksList")
	@XmlElement(name = "stock")
	public List<Stock> getStocksList() {
		if (stocksList == null)
			stocksList = new ArrayList<Stock>();
		return stocksList;
	}

	public void setStocksList(List<Stock> stocksList) {
		this.stocksList = stocksList;
	}
}
