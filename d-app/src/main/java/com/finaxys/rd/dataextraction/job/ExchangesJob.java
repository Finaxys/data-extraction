/*
 * 
 */
package com.finaxys.rd.dataextraction.job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.service.ExchangeService;

// TODO: Auto-generated Javadoc
/**
 * The Class ExchangesJob.
 */
public class ExchangesJob extends QuartzJobBean {

	/** The exchange service. */
	private ExchangeService exchangeService;
	
	/**
	 * Gets the exchange service.
	 *
	 * @return the exchange service
	 */
	public ExchangeService getExchangeService() {
		return exchangeService;
	}

	/**
	 * Sets the exchange service.
	 *
	 * @param exchangeService the new exchange service
	 */
	public void setExchangeService(ExchangeService exchangeService) {
		this.exchangeService = exchangeService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try{
			if(exchangeService!=null)exchangeService.publishExchanges();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
