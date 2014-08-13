/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.dao.integration.EODDataGateway;
import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.service.EODDataService;
import com.finaxys.rd.dataextraction.service.exception.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class EODMarketDataServiceImpl.
 */
public class EODDataServiceImpl<T extends MarketData, K extends MarketData> implements EODDataService<T, K> {

	/** The gateway. */
	private EODDataGateway<T, K> gateway;

	public EODDataServiceImpl(EODDataGateway<T, K> gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<T> getEODData(List<K> products) throws ServiceException {
		try {
			return this.gateway.getEODData(products);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}
}
