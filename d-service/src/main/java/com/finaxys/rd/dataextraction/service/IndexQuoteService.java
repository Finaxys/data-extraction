/*
 * 
 */
package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Interface IndexQuoteService.
 */
public interface IndexQuoteService {
	
	/**
	 * Get index quotes.
	 */
	public List<Message> getCurrentIndexQuotes(String symbols);
}
