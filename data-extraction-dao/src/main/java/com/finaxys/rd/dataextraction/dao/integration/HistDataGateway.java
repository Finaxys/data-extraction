package com.finaxys.rd.dataextraction.dao.integration;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.domain.MarketData;



public interface HistDataGateway<T extends MarketData, K extends MarketData> {

	public List<T> getHistData(List<K> products, LocalDate startDate, LocalDate endDate) throws GatewayException;	
}
