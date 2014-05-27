package domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "exchanges")
public class Exchanges {

	private List<Exchange> exchangesList;

	@XmlElementWrapper(name = "exchangesList")
	@XmlElement(name = "exchange")
	public List<Exchange> getExchangesList() {
		if (exchangesList == null)
			exchangesList = new ArrayList<Exchange>();
		return exchangesList;
	}

	public void setExchangesList(List<Exchange> exchangesList) {
		this.exchangesList = exchangesList;
	}
}