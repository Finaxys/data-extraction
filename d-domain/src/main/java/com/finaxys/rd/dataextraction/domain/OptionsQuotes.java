/*
 * 
 */
package com.finaxys.rd.dataextraction.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


// TODO: Auto-generated Javadoc
/**
 * The Class OptionsQuotes.
 */
@XmlRootElement(name="quotes")
public class OptionsQuotes {

	/** The quotes list. */
	private List<OptionQuote> quotesList;

	/**
	 * Gets the quotes list.
	 *
	 * @return the quotes list
	 */
	@XmlElementWrapper(name = "quotesList")
	@XmlElement(name = "quote")
	public List<OptionQuote> getQuotesList() {
		if (quotesList == null)
			quotesList = new ArrayList<OptionQuote>();
		return quotesList;
	}

	/**
	 * Sets the quotes list.
	 *
	 * @param quotesList the new quotes list
	 */
	public void setQuotesList(List<OptionQuote> quotesList) {
		this.quotesList = quotesList;
	}
}
