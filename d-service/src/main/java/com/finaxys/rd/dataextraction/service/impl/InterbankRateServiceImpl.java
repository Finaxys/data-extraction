package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.dao.integration.InterbankRateGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.InterbankRateService;
import com.finaxys.rd.dataextraction.splitter.Splitter;

public class InterbankRateServiceImpl implements InterbankRateService {

	/** The gateway. */
	private InterbankRateGateway gateway;

	/** The converter. */
	private Converter converter;

	private Splitter splitter;
	/**
	 * Instantiates a new rate service impl.
	 */
	public InterbankRateServiceImpl() {
		super();
	}



	public InterbankRateServiceImpl(InterbankRateGateway gateway, Converter converter, Splitter splitter) {
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
	public InterbankRateGateway getGateway() {
		return gateway;
	}

	/**
	 * Sets the gateway.
	 * 
	 * @param gateway
	 *            the new gateway
	 */
	public void setGateway(InterbankRateGateway gateway) {
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
	 * @see com.finaxys.rd.dataextraction.service.RateService#publishRates()
	 */
	public List<Message> getRates() {

		try {
			Document d = this.gateway.getRates();
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
