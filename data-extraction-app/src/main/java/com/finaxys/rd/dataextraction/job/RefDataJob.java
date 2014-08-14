/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.domain.MarketDataWrapper;
import com.finaxys.rd.dataextraction.service.RefDataService;
import com.finaxys.rd.dataextraction.service.integration.gateway.MarketDataPublishingGateway;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyPairsJob.
 */
public class RefDataJob<T extends MarketData> extends QuartzJobBean {

	private RefDataService<T> service;

	private MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway;

	public RefDataService<T> getService() {
		return service;
	}

	public void setService(RefDataService<T> service) {
		this.service = service;
	}

	public MarketDataPublishingGateway<MarketDataWrapper<T>> getPublishingGateway() {
		return publishingGateway;
	}

	public void setPublishingGateway(MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway) {
		this.publishingGateway = publishingGateway;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org
	 * .quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		List<T> data = service.getRefData();
		if (data != null && data.size() > 0)
			publishingGateway.publishMarketData(new MarketDataWrapper<T>(data));

	}
}