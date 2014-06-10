/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.service.CurrencyPairService;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyPairsJob.
 */
public class CurrencyPairsJob extends QuartzJobBean {

	/** The currency pair service. */
	private CurrencyPairService currencyPairService;

	/**
	 * Gets the currency pair service.
	 *
	 * @return the currency pair service
	 */
	public CurrencyPairService getCurrencyPairService() {
		return currencyPairService;
	}

	/**
	 * Sets the currency pair service.
	 *
	 * @param currencyPairService the new currency pair service
	 */
	public void setCurrencyPairService(CurrencyPairService currencyPairService) {
		this.currencyPairService = currencyPairService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			currencyPairService.publishCurrencyPairs();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}