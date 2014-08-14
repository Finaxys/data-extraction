package com.finaxys.rd.dataextraction.dao.integration;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.domain.MarketData;


public interface IntradayDataGateway<T extends MarketData, K extends MarketData> {

	 List<T> getCurrentData(List<K> products) throws GatewayException;	
}
