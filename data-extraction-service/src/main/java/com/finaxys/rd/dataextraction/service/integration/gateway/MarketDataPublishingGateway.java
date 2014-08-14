package com.finaxys.rd.dataextraction.service.integration.gateway;

import com.finaxys.rd.dataextraction.domain.MarketDataWrapper;


public interface MarketDataPublishingGateway<T extends MarketDataWrapper<?>> {

	 void publishMarketData(T data);
}
