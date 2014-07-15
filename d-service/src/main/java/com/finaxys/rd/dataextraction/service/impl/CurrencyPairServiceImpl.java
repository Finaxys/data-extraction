/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.dao.integration.CurrencyPairGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.CurrencyPairService;
import com.finaxys.rd.dataextraction.splitter.Splitter;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyPairServiceImpl.
 */
public class CurrencyPairServiceImpl implements CurrencyPairService {

	/** The gateway. */
	private CurrencyPairGateway gateway;

	/** The converter. */
	private Converter converter;


	private Splitter splitter;
	/**
	 * Instantiates a new currency pair service impl.
	 */
	public CurrencyPairServiceImpl() {
		super();
	}


	public CurrencyPairServiceImpl(CurrencyPairGateway gateway, Converter converter, Splitter splitter) {
		super();
		this.gateway = gateway;
		this.converter = converter;
		this.splitter = splitter;
	}

	public Splitter getSplitter() {
		return splitter;
	}

	public void setSplitter(Splitter splitter) {
		this.splitter = splitter;
	}

	/**
	 * Gets the gateway.
	 * 
	 * @return the gateway
	 */
	public CurrencyPairGateway getGateway() {
		return gateway;
	}

	/**
	 * Sets the gateway.
	 * 
	 * @param gateway
	 *            the new gateway
	 */
	public void setGateway(CurrencyPairGateway gateway) {
		this.gateway = gateway;
	}

	/**
	 * Gets the converter.
	 * 
	 * @return the converter
	 */
	public Converter getConverter() {
		return converter;
	}

	/**
	 * Sets the converter.
	 * 
	 * @param converter
	 *            the new converter
	 */
	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.finaxys.rd.dataextraction.service.CurrencyPairService#
	 * publishCurrencyPairs()
	 */
	public List<Message> getCurrencyPairs() {

		try {
			Document d = this.gateway.getCurrencyPairs();
			if (d != null) {
				Message message = new Message(d);
				this.converter.convert(message);
				List<Message> l = this.splitter.split(message);
				return l;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}
