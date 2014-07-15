package com.finaxys.rd.dataextraction.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.finaxys.rd.dataextraction.domain.InterbankRateData;

@XmlRootElement(name = "rates")
public class InterbankRateDatas {

	/** The stocks list. */
	private List<InterbankRateData> ratesList;

	/**
	 * Gets the stocks list.
	 *
	 * @return the stocks list
	 */
	@XmlElementWrapper(name = "ratesList")
	@XmlElement(name = "rate")
	public List<InterbankRateData> getRatesList() {
		if (ratesList == null)
			ratesList = new ArrayList<InterbankRateData>();
		return ratesList;
	}
	
	/**
	 * Sets the rates list.
	 *
	 * @param ratesList the new rates list
	 */
	public void setRatesList(List<InterbankRateData> ratesList) {
		this.ratesList = ratesList;
	}
}
