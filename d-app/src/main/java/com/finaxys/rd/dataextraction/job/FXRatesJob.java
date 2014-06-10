/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.service.FXRateService;

// TODO: Auto-generated Javadoc
/**
 * The Class FXRatesJob.
 */
public class FXRatesJob extends QuartzJobBean {

	/** The fx rates service. */
	private FXRateService fxRatesService;

	/**
	 * Gets the fx rate service.
	 *
	 * @return the fx rate service
	 */
	public FXRateService getFxRateService() {
		return fxRatesService;
	}

	/**
	 * Sets the fx rate service.
	 *
	 * @param fxRatesService the new fx rate service
	 */
	public void setFxRateService(FXRateService fxRatesService) {
		this.fxRatesService = fxRatesService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			fxRatesService.publishFXRates();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}
}
