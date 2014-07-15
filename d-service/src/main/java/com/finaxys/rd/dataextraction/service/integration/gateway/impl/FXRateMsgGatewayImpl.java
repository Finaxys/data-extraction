package com.finaxys.rd.dataextraction.service.integration.gateway.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.FXRateService;
import com.finaxys.rd.dataextraction.service.integration.gateway.FXRateMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;

public class FXRateMsgGatewayImpl implements FXRateMsgGateway {

	private FXRateService service;

	/** The publisher. */
	private Publisher publisher;

	/** The routing key. */
	private String routingKey;

	public FXRateMsgGatewayImpl() {
		super();
	}

	public FXRateMsgGatewayImpl(FXRateService service, Publisher publisher, String routingKey) {
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

	public FXRateService getFxRateService() {
		return service;
	}

	public void setFxRateService(FXRateService service) {
		this.service = service;
	}



	@Override
	public void publishCurrentFXRatesList(String symbols) {
		try {
			List<Message> l = service.getCurrentFXRates(symbols);
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
