package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.dao.integration.RefOptionChainGateway;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.Stock;
import com.finaxys.rd.dataextraction.service.RefOptionChainService;
import com.finaxys.rd.dataextraction.service.exception.ServiceException;

public class OptionChainServiceImpl implements RefOptionChainService {

	/** The gateway. */
	private RefOptionChainGateway gateway;

	public OptionChainServiceImpl(RefOptionChainGateway gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<OptionChain> getRefData(List<Stock> stocks) throws ServiceException {
		try {
			return this.gateway.getRefData(stocks);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}
}