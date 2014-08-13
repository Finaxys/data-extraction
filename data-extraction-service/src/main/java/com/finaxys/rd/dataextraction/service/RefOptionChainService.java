package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.Stock;
import com.finaxys.rd.dataextraction.service.exception.ServiceException;


public interface RefOptionChainService{

	public List<OptionChain> getRefData(List<Stock> stocks) throws ServiceException;	
}
