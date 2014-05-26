package domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

	@XmlRootElement(name = "currencyPairs")
	public class CurrencyPairs {

		private List<CurrencyPair> currencyPairsList;

		@XmlElementWrapper(name ="currencyPairsList")
		@XmlElement(name = "currencyPair")
		public List<CurrencyPair> getCurrencyPairsList() {
			if (currencyPairsList == null)
				currencyPairsList = new ArrayList<CurrencyPair>();
			return currencyPairsList;
		}

		public void setCurrencyPairsList(List<CurrencyPair> currencyPairsList) {
			this.currencyPairsList = currencyPairsList;
		}
	}