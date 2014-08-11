package com.finaxys.rd.dataextraction.job;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.dao.StockDao;
import com.finaxys.rd.dataextraction.domain.MarketDataWrapper;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.Stock;
import com.finaxys.rd.dataextraction.service.RefOptionChainService;
import com.finaxys.rd.dataextraction.service.integration.gateway.MarketDataPublishingGateway;

public class OptionChainsJob extends QuartzJobBean {

	private RefOptionChainService service;

	private MarketDataPublishingGateway<MarketDataWrapper<OptionChain>> publishingGateway;

	private StockDao stockDao;

	private ExecutorService executorService;

	private char provider;

	private int bucketSize;

	public RefOptionChainService getService() {
		return service;
	}

	public void setService(RefOptionChainService service) {
		this.service = service;
	}

	public MarketDataPublishingGateway<MarketDataWrapper<OptionChain>> getPublishingGateway() {
		return publishingGateway;
	}

	public void setPublishingGateway(
			MarketDataPublishingGateway<MarketDataWrapper<OptionChain>> publishingGateway) {
		this.publishingGateway = publishingGateway;
	}

	public StockDao getStockDao() {
		return stockDao;
	}

	public void setStockDao(StockDao stockDao) {
		this.stockDao = stockDao;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public char getProvider() {
		return provider;
	}

	public void setProvider(char provider) {
		this.provider = provider;
	}

	public int getBucketSize() {
		return bucketSize;
	}

	public void setBucketSize(int bucketSize) {
		this.bucketSize = bucketSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org
	 * .quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			List<Stock> stocks = stockDao.list(provider);

			int x = stocks.size() / bucketSize;
			int y = stocks.size() % bucketSize;

			for (int i = 0; i < x; i++) {

				Task task = new Task(stocks.subList((i * bucketSize),
						((i + 1) * bucketSize)), service, publishingGateway);
				executorService.execute(task);
			}

			Task task = new Task(stocks.subList((x * bucketSize),
					(x * bucketSize) + y), service, publishingGateway);
			executorService.execute(task);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static class Task implements Runnable {
		List<Stock> stocks;
		private RefOptionChainService service;

		private MarketDataPublishingGateway<MarketDataWrapper<OptionChain>> publishingGateway;

		public Task(List<Stock> stocks, RefOptionChainService service,
				MarketDataPublishingGateway<MarketDataWrapper<OptionChain>> publishingGateway) {
			super();
			this.stocks = stocks;
			this.service = service;
			this.publishingGateway = publishingGateway;
		}

		public void run() {
			try {
				List<OptionChain> data = service.getRefData(stocks);
				if (data != null && data.size() > 0)
				publishingGateway.publishMarketData(new MarketDataWrapper<OptionChain>(data));
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
