package com.finaxys.rd.dataextraction.dao.integration;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.Stock;


public interface RefOptionChainGateway {

	 List<OptionChain> getRefData(List<Stock> stocks) throws GatewayException ;
		
}
