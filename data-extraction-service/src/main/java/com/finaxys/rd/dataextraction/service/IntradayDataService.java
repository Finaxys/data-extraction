package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.MarketData;


public interface IntradayDataService<T extends MarketData, K extends MarketData> {

	public List<T> getCurrentData(List<K> products) throws Exception;	
}
