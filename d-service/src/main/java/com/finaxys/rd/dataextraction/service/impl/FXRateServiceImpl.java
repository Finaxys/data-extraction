/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.dao.integration.FXRateGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.FXRateService;
import com.finaxys.rd.dataextraction.splitter.Splitter;

// TODO: Auto-generated Javadoc
/**
 * The Class FXRateServiceImpl.
 */
public class FXRateServiceImpl implements FXRateService {

	/** The gateway. */
	private FXRateGateway gateway;

	/** The converter. */
	private Converter converter;

	private Splitter splitter;
	


	/**
	 * Instantiates a new FX rate service impl.
	 */
	public FXRateServiceImpl() {
		super();
	}

	/**
	 * Instantiates a new FX rate service impl.
	 * 
	 * @param gateway
	 *            the gateway
	 * @param converter
	 *            the converter
	 * @param contentType
	 *            the content type
	 */
	public FXRateServiceImpl(FXRateGateway gateway, Converter converter) {
		super();
		this.gateway = gateway;
		this.converter = converter;
	}

	public FXRateServiceImpl(FXRateGateway gateway, Converter converter, Splitter splitter) {
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
	public FXRateGateway getGateway() {
		return gateway;
	}

	/**
	 * Sets the gateway.
	 * 
	 * @param gateway
	 *            the new gateway
	 */
	public void setGateway(FXRateGateway gateway) {
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
	public List<Message> getCurrentFXRates(String symbols) {
		try {
			Document d = this.gateway.getCurrentFXRates(symbols);
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
