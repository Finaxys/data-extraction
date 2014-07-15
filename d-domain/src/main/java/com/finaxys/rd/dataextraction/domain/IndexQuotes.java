/*
 * 
 */
package com.finaxys.rd.dataextraction.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.finaxys.rd.dataextraction.domain.IndexQuote;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexQuotes.
 */
@XmlRootElement(name="quotes")
public class IndexQuotes {

	/** The quotes list. */
	private List<IndexQuote> quotesList;

	/**
	 * Gets the index quotes list.
	 *
	 * @return the index quotes list
	 */
	@XmlElementWrapper(name = "quotesList")
	@XmlElement(name = "quote")
	public List<IndexQuote> getIndexQuotesList() {
		if (quotesList == null)
			quotesList = new ArrayList<IndexQuote>();
		return quotesList;
	}

	/**
	 * Sets the ind quotes list.
	 *
	 * @param quotesList the new ind quotes list
	 */
	public void setIndQuotesList(List<IndexQuote> quotesList) {
		this.quotesList = quotesList;
	}
}
