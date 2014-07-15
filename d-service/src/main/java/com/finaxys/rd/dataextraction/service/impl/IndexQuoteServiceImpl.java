/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.dao.integration.IndexQuoteGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.IndexQuoteService;
import com.finaxys.rd.dataextraction.splitter.Splitter;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexQuoteServiceImpl.
 */
public class IndexQuoteServiceImpl implements IndexQuoteService {

	/** The gateway. */
	private IndexQuoteGateway gateway;

	/** The converter. */
	private Converter converter;

	private Splitter splitter;


	/**
	 * Instantiates a new index quote service impl.
	 */
	public IndexQuoteServiceImpl() {
		super();
	}

	/**
	 * Instantiates a new index quote service impl.
	 * 
	 * @param gateway
	 *            the gateway
	 * @param converter
	 *            the converter
	 * @param contentType
	 *            the content type
	 */
	public IndexQuoteServiceImpl(IndexQuoteGateway gateway, Converter converter) {
		super();
		this.gateway = gateway;
		this.converter = converter;
	}

	public IndexQuoteServiceImpl(IndexQuoteGateway gateway, Converter converter, Splitter splitter) {
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
	public IndexQuoteGateway getGateway() {
		return gateway;
	}

	/**
	 * Sets the gateway.
	 * 
	 * @param gateway
	 *            the new gateway
	 */
	public void setGateway(IndexQuoteGateway gateway) {
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



	@Override
	public List<Message> getCurrentIndexQuotes(String symbols) {

		try {
			Document d = this.gateway.getCurrentIndexQuotes(symbols);
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
