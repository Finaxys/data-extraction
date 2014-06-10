/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.service.IndexInfoService;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexInfosJob.
 */
public class IndexInfosJob extends QuartzJobBean {

	/** The index info service. */
	private IndexInfoService indexInfoService;

	/**
	 * Gets the index info service.
	 *
	 * @return the index info service
	 */
	public IndexInfoService getIndexInfoService() {
		return indexInfoService;
	}

	/**
	 * Sets the index info service.
	 *
	 * @param indexInfoService the new index info service
	 */
	public void setIndexInfoService(IndexInfoService indexInfoService) {
		this.indexInfoService = indexInfoService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			indexInfoService.publishIndexInfos();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
