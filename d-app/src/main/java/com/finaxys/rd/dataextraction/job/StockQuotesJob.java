/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.integration.gateway.StockQuoteMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;
import com.finaxys.rd.marketdataprovider.dao.StockDao;

// TODO: Auto-generated Javadoc
/**
 * The Class StockQuotesJob.
 */
public class StockQuotesJob extends QuartzJobBean {

	/** The stock quote service. */
	private StockQuoteMsgGateway stockQuoteMsgGateway;

	private StockDao stockDao;

	public StockQuoteMsgGateway getStockQuoteMsgGateway() {
		return stockQuoteMsgGateway;
	}

	public void setStockQuoteMsgGateway(StockQuoteMsgGateway stockQuoteMsgGateway) {
		this.stockQuoteMsgGateway = stockQuoteMsgGateway;
	}

	public StockDao getStockDao() {
		return stockDao;
	}

	public void setStockDao(StockDao stockDao) {
		this.stockDao = stockDao;
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
		try {
			List<String> symbolsGp = stockDao.listAllSymbols();
			for (String symbols : symbolsGp)
				stockQuoteMsgGateway.publishCurrentStocksQuotesList(symbols);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
