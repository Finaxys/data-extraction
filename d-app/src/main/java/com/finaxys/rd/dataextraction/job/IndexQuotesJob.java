/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.service.IndexQuoteService;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexQuotesJob.
 */
public class IndexQuotesJob extends QuartzJobBean {

	/** The index quote service. */
	private IndexQuoteService indexQuoteService;

	/**
	 * Gets the index quote service.
	 *
	 * @return the index quote service
	 */
	public IndexQuoteService getIndexQuoteService() {
		return indexQuoteService;
	}

	/**
	 * Sets the index quote service.
	 *
	 * @param indexQuoteService the new index quote service
	 */
	public void setIndexQuoteService(IndexQuoteService indexQuoteService) {
		this.indexQuoteService = indexQuoteService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			indexQuoteService.publishIndexQuotes();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}
}
