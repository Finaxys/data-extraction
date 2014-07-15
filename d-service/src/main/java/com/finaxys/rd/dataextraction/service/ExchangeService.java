/*
 * 
 */
package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Interface ExchangeService.
 */
public interface ExchangeService {
	
	/**
	 * Get exchanges.
	 */
	public List<Message>  getExchanges();
}
