package com.finaxys.rd.dataextraction.service.integration.gateway.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.service.InterbankRateService;
import com.finaxys.rd.dataextraction.service.integration.gateway.InterbankRateMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class InterbankRateMsgGatewayImpl implements InterbankRateMsgGateway {

	private InterbankRateService service;

	/** The publisher. */
	private Publisher publisher;

	/** The routing key. */
	private String routingKey;

	public InterbankRateMsgGatewayImpl() {
		super();
	}

	public InterbankRateMsgGatewayImpl(InterbankRateService service, Publisher publisher, String routingKey) {
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

	public InterbankRateService getRateService() {
		return service;
	}

	public void setRateService(InterbankRateService service) {
		this.service = service;
	}

	@Override
	public void publishRates() {
		try {
			List<Message> l = service.getRates();
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
