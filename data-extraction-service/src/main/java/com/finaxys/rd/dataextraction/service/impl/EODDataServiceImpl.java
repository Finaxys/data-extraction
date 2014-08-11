/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.integration.EODDataGateway;
import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.service.EODDataService;

// TODO: Auto-generated Javadoc
/**
 * The Class EODMarketDataServiceImpl.
 */
public class EODDataServiceImpl<T extends MarketData, K extends MarketData> implements
		EODDataService<T, K> {

	/** The gateway. */
	private EODDataGateway<T, K> gateway;

	public EODDataServiceImpl(EODDataGateway<T, K> gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<T> getEODData(List<K> products)
			throws Exception {
		try {
			return this.gateway.getEODData(products);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
