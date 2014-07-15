package com.finaxys.rd.dataextraction.service.integration.gateway.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.OptionChainService;
import com.finaxys.rd.dataextraction.service.integration.gateway.OptionChainMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;

public class OptionChainMsgGatewayImpl implements OptionChainMsgGateway{

	private OptionChainService service;

	/** The publisher. */
	private Publisher publisher;

	/** The routing key. */
	private String routingKey;

	public OptionChainMsgGatewayImpl() {
		super();
	}

	public OptionChainMsgGatewayImpl(OptionChainService service, Publisher publisher, String routingKey) {
		super();
		this.service = service;
		this.publisher = publisher;
		this.routingKey = routingKey;
	}

	public OptionChainService getOptionChainService() {
		return service;
	}

	public void setOptionChainService(OptionChainService service) {
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
	public void publishOptionChains(String symbols) {	try {
		List<Message> l = service.getOptionChains(symbols);
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
