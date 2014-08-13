/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.dao.integration.RefDataGateway;
import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.service.RefDataService;
import com.finaxys.rd.dataextraction.service.exception.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketDataServiceImpl.
 */
public class RefDataServiceImpl<T extends MarketData> implements RefDataService<T> {

	/** The gateway. */
	private RefDataGateway<T> gateway;

	public RefDataServiceImpl(RefDataGateway<T> gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<T> getRefData() throws ServiceException {
		try {
			return this.gateway.getRefData();
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}

}
