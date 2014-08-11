package com.finaxys.rd.dataextraction.dao.integration;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.MarketData;


public interface EODDataGateway<T extends MarketData, K extends MarketData> {

	public List<T> getEODData(List<K> products) throws Exception;	
}
