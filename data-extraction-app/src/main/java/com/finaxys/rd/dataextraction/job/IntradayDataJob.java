/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.domain.MarketDataWrapper;
import com.finaxys.rd.dataextraction.service.IntradayDataService;
import com.finaxys.rd.dataextraction.service.integration.gateway.MarketDataPublishingGateway;

// TODO: Auto-generated Javadoc
/**
 * The Class FXRatesJob.
 */
public class IntradayDataJob<T extends MarketData, K extends MarketData>
		extends QuartzJobBean {

	private List<K> products;

	private IntradayDataService<T, K> service;

	private MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway;

	private ExecutorService executorService;

	private int bucketSize;

	public List<K> getProducts() {
		return products;
	}

	public void setProducts(List<K> products) {
		this.products = products;
	}

	public IntradayDataService<T, K> getService() {
		return service;
	}

	public void setService(IntradayDataService<T, K> service) {
		this.service = service;
	}

	public MarketDataPublishingGateway<MarketDataWrapper<T>> getPublishingGateway() {
		return publishingGateway;
	}

	public void setPublishingGateway(
			MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway) {
		this.publishingGateway = publishingGateway;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public int getBucketSize() {
		return bucketSize;
	}

	public void setBucketSize(int bucketSize) {
		this.bucketSize = bucketSize;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {

		try {
			int x = products.size() / bucketSize;
			int y = products.size() % bucketSize;

			for (int i = 0; i < x; i++) {
				Task<T, K> task = new Task<T, K>(products.subList(
						(i * bucketSize), ((i + 1) * bucketSize)), service,
						publishingGateway);
				executorService.execute(task);
			}

			Task<T, K> task = new Task<T, K>(products.subList((x * bucketSize),
					(x * bucketSize) + y), service, publishingGateway);
			executorService.execute(task);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static class Task<T extends MarketData, K extends MarketData>
			implements Runnable {
		private List<K> products;

		private IntradayDataService<T, K> service;

		private MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway;

		public Task(
				List<K> products,
				IntradayDataService<T, K> service,
				MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway) {
			super();
			this.products = products;
			this.service = service;
			this.publishingGateway = publishingGateway;
		}


		public void run() {
			List<T> data;
			try {
				data = service.getCurrentData(products);
				if (data != null && data.size() > 0)
					publishingGateway.publishMarketData(new MarketDataWrapper<T>(data));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
