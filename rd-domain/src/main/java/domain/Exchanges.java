package domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Exchanges {

	private List<Exchange> exchangeList;

	@XmlElementWrapper
	@XmlElement(name = "exchange")
	public List<Exchange> getExchangeList() {
		if (exchangeList == null)
			exchangeList = new ArrayList<Exchange>();
		return exchangeList;
	}

	public void setExchangeList(List<Exchange> exchangeList) {
		this.exchangeList = exchangeList;
	}
}