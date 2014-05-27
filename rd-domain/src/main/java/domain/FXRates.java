package domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="rates")
public class FXRates {

	private List<FXRate> ratesList;

	@XmlElementWrapper(name = "ratesList")
	@XmlElement(name = "rate")
	public List<FXRate> getRatesList() {
		if (ratesList == null)
			ratesList = new ArrayList<FXRate>();
		return ratesList;
	}

	public void setIndInfosList(List<FXRate> ratesList) {
		this.ratesList = ratesList;
	}
}
