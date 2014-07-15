/*
 * 
 */
package com.finaxys.rd.dataextraction.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.finaxys.rd.dataextraction.domain.OptionChain;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionChains.
 */
@XmlRootElement(name="optionChains")
public class OptionChains {

	/** The optionChains list. */
	private List<OptionChain> optionChainsList;

	/**
	 * Gets the optionChains list.
	 *
	 * @return the optionChains list
	 */
	@XmlElementWrapper(name = "optionChainsList")
	@XmlElement(name = "optionChain")
	public List<OptionChain> getOptionChainsList() {
		if (optionChainsList == null)
			optionChainsList = new ArrayList<OptionChain>();
		return optionChainsList;
	}

	/**
	 * Sets the optionChains list.
	 *
	 * @param optionChainsList the new optionChains list
	 */
	public void setOptionChainsList(List<OptionChain> optionChainsList) {
		this.optionChainsList = optionChainsList;
	}
}
