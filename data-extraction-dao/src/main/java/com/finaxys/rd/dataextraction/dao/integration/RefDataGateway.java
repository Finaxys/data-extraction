package com.finaxys.rd.dataextraction.dao.integration;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.domain.MarketData;


public interface RefDataGateway<T extends MarketData> {

	 List<T> getRefData() throws GatewayException;	
}
