package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.dao.integration.OptionChainGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.OptionChainService;
import com.finaxys.rd.dataextraction.splitter.Splitter;

public class OptionChainServiceImpl implements OptionChainService{

	

	/** The gateway. */
	private OptionChainGateway gateway;

	/** The converter. */
	private Converter converter;

	private Splitter splitter;

	/**
	 * Instantiates a new optionChain service impl.
	 */
	public OptionChainServiceImpl() {
		super();
	}

	public OptionChainServiceImpl(OptionChainGateway gateway, Converter converter, Splitter splitter) {
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
	public OptionChainGateway getGateway() {
		return gateway;
	}

	/**
	 * Sets the gateway.
	 * 
	 * @param gateway
	 *            the new gateway
	 */
	public void setGateway(OptionChainGateway gateway) {
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
	public List<Message> getOptionChains(String symbols) {
		try {
			Document d = this.gateway.getOptionChains(symbols);
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