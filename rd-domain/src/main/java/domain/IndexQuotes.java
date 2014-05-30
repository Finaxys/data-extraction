package domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="quotes")
public class IndexQuotes {

	private List<IndexQuote> quotesList;

	@XmlElementWrapper(name = "quotesList")
	@XmlElement(name = "quote")
	public List<IndexQuote> getIndexQuotesList() {
		if (quotesList == null)
			quotesList = new ArrayList<IndexQuote>();
		return quotesList;
	}

	public void setIndQuotesList(List<IndexQuote> quotesList) {
		this.quotesList = quotesList;
	}
}
