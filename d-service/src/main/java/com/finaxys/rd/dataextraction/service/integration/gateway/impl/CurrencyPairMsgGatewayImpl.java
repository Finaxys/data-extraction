package com.finaxys.rd.dataextraction.service.integration.gateway.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.service.CurrencyPairService;
import com.finaxys.rd.dataextraction.service.integration.gateway.CurrencyPairMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class CurrencyPairMsgGatewayImpl implements CurrencyPairMsgGateway {

	private CurrencyPairService service;

	/** The publisher. */
	private Publisher publisher;

	/** The routing key. */
	private String routingKey;

	public CurrencyPairMsgGatewayImpl() {
		super();
	}

	public CurrencyPairMsgGatewayImpl(CurrencyPairService service, Publisher publisher, String routingKey) {
		super();
		this.service = service;
		this.publisher = publisher;
		this.routingKey = routingKey;
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

	public CurrencyPairService getCurrencyPairService() {
		return service;
	}

	public void setCurrencyPairService(CurrencyPairService service) {
		this.service = service;
	}

	@Override
	public void publishCurrencyPairs() {
		try {
			List<Message> l = service.getCurrencyPairs();
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
