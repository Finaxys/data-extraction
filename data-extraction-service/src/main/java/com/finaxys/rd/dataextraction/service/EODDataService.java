package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.service.exception.ServiceException;


public interface EODDataService<T extends MarketData, K extends MarketData> {

	public List<T> getEODData(List<K> products) throws ServiceException;	
}
