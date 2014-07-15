/*
 * 
 */
package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Interface StockQuoteService.
 */
public interface StockQuoteService {
	
	/**
	 * Get stocks quotes.
	 */
	public List<Message> getCurrentStocksQuotes(String symbols);
}
