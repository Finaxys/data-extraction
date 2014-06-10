/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.service.StockQuoteService;


// TODO: Auto-generated Javadoc
/**
 * The Class StockQuotesJob.
 */
public class StockQuotesJob extends QuartzJobBean {

	/** The stock quote service. */
	private StockQuoteService stockQuoteService;

	/**
	 * Gets the stock quote service.
	 *
	 * @return the stock quote service
	 */
	public StockQuoteService getStockQuoteService() {
		return stockQuoteService;
	}

	/**
	 * Sets the stock quote service.
	 *
	 * @param stockQuoteService the new stock quote service
	 */
	public void setStockQuoteService(StockQuoteService stockQuoteService) {
		this.stockQuoteService = stockQuoteService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try{
			stockQuoteService.publishStocksQuotes();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
