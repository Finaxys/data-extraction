package com.finaxys.rd.dataextraction.service.integration.gateway.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.IndexInfoService;
import com.finaxys.rd.dataextraction.service.integration.gateway.IndexInfoMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;

public class IndexInfoMsgGatewayImpl implements IndexInfoMsgGateway {

	private IndexInfoService service;

	/** The publisher. */
	private Publisher publisher;

	/** The routing key. */
	private String routingKey;
	

	public IndexInfoMsgGatewayImpl() {
		super();
	}

	public IndexInfoMsgGatewayImpl(IndexInfoService service, Publisher publisher, String routingKey) {
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

	public IndexInfoService getInfoIndexService() {
		return service;
	}

	public void setInfoIndexService(IndexInfoService service) {
		this.service = service;
	}

	public IndexInfoService getService() {
		return service;
	}

	public void setService(IndexInfoService service) {
		this.service = service;
	}



	@Override
	public void publishIndexInfos() {
		try {
			List<Message> l = service.getIndexInfos();
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
