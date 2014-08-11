package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.MarketData;


public interface RefDataService<T extends MarketData> {

	public List<T> getRefData() throws Exception;	
}
