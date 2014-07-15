/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.dao.integration.StockQuoteGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.StockQuoteService;
import com.finaxys.rd.dataextraction.splitter.Splitter;

// TODO: Auto-generated Javadoc
/**
 * The Class StockQuoteServiceImpl.
 */
public class StockQuoteServiceImpl implements StockQuoteService {

	/** The gateway. */
	private StockQuoteGateway gateway;

	/** The converter. */
	private Converter converter;

	private Splitter splitter;
	


	/**
	 * Instantiates a new stock quote service impl.
	 */
	public StockQuoteServiceImpl() {
		super();
	}

	/**
	 * Instantiates a new stock quote service impl.
	 * 
	 * @param gateway
	 *            the gateway
	 * @param converter
	 *            the converter
	 * @param publisher
	 *            the publisher
	 * @param contentType
	 *            the content type
	 */
	public StockQuoteServiceImpl(StockQuoteGateway gateway, Converter converter ) {
		super();
		this.gateway = gateway;
		this.converter = converter;
	}

	
	public StockQuoteServiceImpl(StockQuoteGateway gateway, Converter converter, Splitter splitter ) {
		super();
		this.gateway = gateway;
		this.converter = converter;
		this.splitter = splitter;
	}

	/**
	 * Gets the gateway.
	 * 
	 * @return the gateway
	 */
	public StockQuoteGateway getGateway() {
		return gateway;
	}

	/**
	 * Sets the gateway.
	 * 
	 * @param gateway
	 *            the new gateway
	 */
	public void setGateway(StockQuoteGateway gateway) {
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


	
	
	public Splitter getSplitter() {
		return splitter;
	}

	public void setSplitter(Splitter splitter) {
		this.splitter = splitter;
	}



	@Override
	public List<Message> getCurrentStocksQuotes(String symbols) {
		try {
			Document d = this.gateway.getCurrentStocksQuotes(symbols);
			if (d != null) {
				Message message = new Message(d);
				this.converter.convert(message);
				List<Message> l = splitter.split(message);
				return l;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}