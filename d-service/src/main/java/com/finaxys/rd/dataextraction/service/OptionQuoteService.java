/*
 * 
 */
package com.finaxys.rd.dataextraction.service;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Interface StockQuoteService.
 */
public interface OptionQuoteService {
	
	/**
	 * Get stocks quotes.
	 */
	public List<Message> getCurrentOptionsQuotes(String symbols, LocalDate expiration);
}
