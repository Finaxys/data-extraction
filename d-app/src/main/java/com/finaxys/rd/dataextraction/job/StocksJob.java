/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.service.StockService;

// TODO: Auto-generated Javadoc
/**
 * The Class StocksJob.
 */
public class StocksJob extends QuartzJobBean {

	/** The stock service. */
	private StockService stockService;

	/**
	 * Gets the stock service.
	 *
	 * @return the stock service
	 */
	public StockService getStockService() {
		return stockService;
	}

	/**
	 * Sets the stock service.
	 *
	 * @param stockService the new stock service
	 */
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			stockService.publishStocks();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
