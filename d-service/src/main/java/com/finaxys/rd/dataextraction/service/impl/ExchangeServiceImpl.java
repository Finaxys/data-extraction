/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.dao.integration.ExchangeGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.ExchangeService;
import com.finaxys.rd.dataextraction.splitter.Splitter;

// TODO: Auto-generated Javadoc
/**
 * The Class ExchangeServiceImpl.
 */
public class ExchangeServiceImpl implements ExchangeService {

	/** The gateway. */
	private ExchangeGateway gateway;

	/** The converter. */
	private Converter converter;

	private Splitter splitter;

	/**
	 * Instantiates a new exchange service impl.
	 */
	public ExchangeServiceImpl() {
		super();
	}

	

	public ExchangeServiceImpl(ExchangeGateway gateway, Converter converter, Splitter splitter) {
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
	public ExchangeGateway getGateway() {
		return gateway;
	}

	/**
	 * Sets the gateway.
	 * 
	 * @param gateway
	 *            the new gateway
	 */
	public void setGateway(ExchangeGateway gateway) {
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
	 * @see
	 * com.finaxys.rd.dataextraction.service.ExchangeService#publishExchanges()
	 */
	public List<Message>  getExchanges() {

		try {
			Document d = this.gateway.getExchanges();
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