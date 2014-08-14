package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.service.exception.ServiceException;


public interface IntradayDataService<T extends MarketData, K extends MarketData> {

	 List<T> getCurrentData(List<K> products) throws ServiceException;	
}
