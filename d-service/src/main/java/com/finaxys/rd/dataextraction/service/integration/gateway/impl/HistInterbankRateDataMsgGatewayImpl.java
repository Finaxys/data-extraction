package com.finaxys.rd.dataextraction.service.integration.gateway.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.service.HistInterbankRateDataService;
import com.finaxys.rd.dataextraction.service.integration.gateway.HistInterbankRateDataMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class HistInterbankRateDataMsgGatewayImpl implements HistInterbankRateDataMsgGateway{
	
	private HistInterbankRateDataService service;

	/** The publisher. */
	private Publisher publisher;

	/** The routing key. */
	private String routingKey;

	public HistInterbankRateDataMsgGatewayImpl() {
		super();
	}

	public HistInterbankRateDataMsgGatewayImpl(HistInterbankRateDataService service, Publisher publisher, String routingKey) {
		super();
		this.service = service;
		this.publisher = publisher;
		this.routingKey = routingKey;
	}
	
	public HistInterbankRateDataService getService() {
		return service;
	}

	public void setService(HistInterbankRateDataService service) {
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
	public void publishHistInterbankRatesList(Integer year) {
		try {
			List<Message> l = service.getHistRatesData(year);
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
