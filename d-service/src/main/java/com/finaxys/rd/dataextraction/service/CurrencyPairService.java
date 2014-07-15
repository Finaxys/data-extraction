/*
 * 
 */
package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Interface CurrencyPairService.
 */
public interface CurrencyPairService {
	
	/**
	 * Get currency pairs.
	 */
	public List<Message> getCurrencyPairs();
}
