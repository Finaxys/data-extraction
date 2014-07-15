package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

//TODO: Auto-generated Javadoc
/**
* The Interface RatesService.
*/
public interface InterbankRateService {

	/**
	 * Get index quotes.
	 */
	public List<Message> getRates();
}
