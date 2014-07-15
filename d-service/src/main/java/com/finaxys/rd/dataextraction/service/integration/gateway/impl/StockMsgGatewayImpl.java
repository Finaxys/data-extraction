package com.finaxys.rd.dataextraction.service.integration.gateway.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.service.StockService;
import com.finaxys.rd.dataextraction.service.integration.gateway.StockMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class StockMsgGatewayImpl implements StockMsgGateway {

	private StockService service;

	/** The publisher. */
	private Publisher publisher;

	/** The routing key. */
	private String routingKey;

	public StockMsgGatewayImpl() {
		super();
	}

	public StockMsgGatewayImpl(StockService service, Publisher publisher, String routingKey) {
		super();
		this.service = service;
		this.publisher = publisher;
		this.routingKey = routingKey;
	}

	public StockService getStockService() {
		return service;
	}

	public void setStockService(StockService service) {
		this.service = service;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	@Override
	public void publishStocks() {
		try {
			List<Message> l = service.getStocks();
			if(l != null && l.size() > 0)
			for (Message m : l)
				if (m != null && m.isSend()) {
					m.setRoutingKey(routingKey);
					publisher.publish(m);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
