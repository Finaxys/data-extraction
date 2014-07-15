/*
 * 
 */
package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Interface StockService.
 */
public interface StockService {
	
	/**
	 * Get stocks.
	 */
	public List<Message> getStocks();
}
