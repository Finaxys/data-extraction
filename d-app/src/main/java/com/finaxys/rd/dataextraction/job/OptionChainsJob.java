package com.finaxys.rd.dataextraction.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.service.integration.gateway.OptionChainMsgGateway;
import com.finaxys.rd.marketdataprovider.dao.StockDao;

public class OptionChainsJob extends QuartzJobBean {

	/** The optionChain service. */
	private OptionChainMsgGateway optionChainMsgGateway;

	private StockDao stockDao;

	/**
	 * Gets the optionChain service.
	 * 
	 * @return the optionChain service
	 */
	public OptionChainMsgGateway getOptionChainMsgGateway() {
		return optionChainMsgGateway;
	}

	/**
	 * Sets the optionChain service.
	 * 
	 * @param optionChainMsgGateway
	 *            the new optionChain service
	 */
	public void setOptionChainMsgGateway(
			OptionChainMsgGateway optionChainMsgGateway) {
		this.optionChainMsgGateway = optionChainMsgGateway;
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
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			List<String> symbolsGp = stockDao.listAllSymbols();
			for (String symbols : symbolsGp)
				optionChainMsgGateway.publishOptionChains(symbols);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
