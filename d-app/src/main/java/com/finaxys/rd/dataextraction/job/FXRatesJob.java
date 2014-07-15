/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.integration.gateway.FXRateMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;
import com.finaxys.rd.marketdataprovider.dao.CurrencyPairDao;

// TODO: Auto-generated Javadoc
/**
 * The Class FXRatesJob.
 */
public class FXRatesJob extends QuartzJobBean {

	/** The fx rates service. */
	private FXRateMsgGateway fxRatesMsgGateway;

	/** The currency pair dao. */
	private CurrencyPairDao currencyPairDao;

	/**
	 * Gets the fx rate service.
	 * 
	 * @return the fx rate service
	 */
	public FXRateMsgGateway getFxRateMsgGateway() {
		return fxRatesMsgGateway;
	}

	

	public FXRateMsgGateway getFxRatesMsgGateway() {
		return fxRatesMsgGateway;
	}



	public void setFxRatesMsgGateway(FXRateMsgGateway fxRatesMsgGateway) {
		this.fxRatesMsgGateway = fxRatesMsgGateway;
	}



	public CurrencyPairDao getCurrencyPairDao() {
		return currencyPairDao;
	}



	public void setCurrencyPairDao(CurrencyPairDao currencyPairDao) {
		this.currencyPairDao = currencyPairDao;
	}



	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			List<String> symbolsGp = currencyPairDao.listAllSymbols();
			for (String symbols : symbolsGp)
				fxRatesMsgGateway.publishCurrentFXRatesList(symbols);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
