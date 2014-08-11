package com.finaxys.rd.dataextraction.service;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.domain.MarketData;



public interface HistDataService<T extends MarketData, K extends MarketData> {

	public List<T> getHistData(List<K> products, LocalDate startDate, LocalDate endDate) throws Exception;	
}
