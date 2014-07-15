/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.dao.integration.OptionQuoteGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.OptionQuoteService;
import com.finaxys.rd.dataextraction.splitter.Splitter;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionQuoteServiceImpl.
 */
public class OptionQuoteServiceImpl implements OptionQuoteService {

	/** The gateway. */
	private OptionQuoteGateway gateway;

	/** The converter. */
	private Converter converter;

	private Splitter splitter;
	


	/**
	 * Instantiates a new Option quote service impl.
	 */
	public OptionQuoteServiceImpl() {
		super();
	}

	/**
	 * Instantiates a new Option quote service impl.
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
	public OptionQuoteServiceImpl(OptionQuoteGateway gateway, Converter converter ) {
		super();
		this.gateway = gateway;
		this.converter = converter;
	}

	
	public OptionQuoteServiceImpl(OptionQuoteGateway gateway, Converter converter, Splitter splitter ) {
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
	public OptionQuoteGateway getGateway() {
		return gateway;
	}

	/**
	 * Sets the gateway.
	 * 
	 * @param gateway
	 *            the new gateway
	 */
	public void setGateway(OptionQuoteGateway gateway) {
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
	public List<Message> getCurrentOptionsQuotes(String symbols,
			LocalDate expiration) {
		try {
			Document d = this.gateway.getCurrentOptionsQuotes(symbols, expiration);
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