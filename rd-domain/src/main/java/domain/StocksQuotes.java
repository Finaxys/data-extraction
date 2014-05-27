package domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="quotes")
public class StocksQuotes {

	private List<StockQuote> quotesList;

	@XmlElementWrapper(name = "quotesList")
	@XmlElement(name = "quote")
	public List<StockQuote> getQuotesList() {
		if (quotesList == null)
			quotesList = new ArrayList<StockQuote>();
		return quotesList;
	}

	public void setQuotesList(List<StockQuote> quotesList) {
		this.quotesList = quotesList;
	}
}
