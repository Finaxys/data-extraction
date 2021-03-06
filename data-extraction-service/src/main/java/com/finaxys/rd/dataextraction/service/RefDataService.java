package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.service.exception.ServiceException;


public interface RefDataService<T extends MarketData> {

	 List<T> getRefData() throws ServiceException;	
}
