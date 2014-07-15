/*
 * 
 */
package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Interface FXRateService.
 */
public interface FXRateService {
	
	/**
	 * Get fx rates.
	 */
	public List<Message> getCurrentFXRates(String symbols);
}
