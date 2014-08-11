/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.integration.IntradayDataGateway;
import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.service.IntradayDataService;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketDataServiceImpl.
 */
public class IntradayDataServiceImpl<T extends MarketData, K extends MarketData>  implements
		IntradayDataService<T, K> {

	/** The gateway. */
	private IntradayDataGateway<T, K> gateway;

	public IntradayDataServiceImpl(IntradayDataGateway<T, K> gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<T> getCurrentData(List<K> products)
			throws Exception {
		try {
			return this.gateway.getCurrentData(products);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}