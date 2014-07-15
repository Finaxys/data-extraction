/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.integration.gateway.StockMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;

// TODO: Auto-generated Javadoc
/**
 * The Class StocksJob.
 */
public class StocksJob extends QuartzJobBean {

	/** The stock service. */
	private StockMsgGateway stockMsgGateway;

	/**
	 * Gets the stock service.
	 *
	 * @return the stock service
	 */
	public StockMsgGateway getStockMsgGateway() {
		return stockMsgGateway;
	}

	/**
	 * Sets the stock service.
	 *
	 * @param stockMsgGateway the new stock service
	 */
	public void setStockMsgGateway(StockMsgGateway stockMsgGateway) {
		this.stockMsgGateway = stockMsgGateway;
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			stockMsgGateway.publishStocks();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
