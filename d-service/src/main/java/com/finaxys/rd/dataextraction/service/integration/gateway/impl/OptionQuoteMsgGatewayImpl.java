package com.finaxys.rd.dataextraction.service.integration.gateway.impl;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.service.OptionQuoteService;
import com.finaxys.rd.dataextraction.service.integration.gateway.OptionQuoteMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class OptionQuoteMsgGatewayImpl implements OptionQuoteMsgGateway {

	private OptionQuoteService service;

	/** The publisher. */
	private Publisher publisher;

	/** The routing key. */
	private String routingKey;

	public OptionQuoteMsgGatewayImpl() {
		super();
	}

	public OptionQuoteMsgGatewayImpl(OptionQuoteService service, Publisher publisher, String routingKey) {
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

	public OptionQuoteService getOptionQuoteService() {
		return service;
	}

	public void setOptionQuoteService(OptionQuoteService service) {
		this.service = service;
	}


	@Override
	public void publishCurrentOptionsQuotesList(String symbols, LocalDate expiration) {
		try {
				List<Message> l = service.getCurrentOptionsQuotes(symbols, expiration);
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
