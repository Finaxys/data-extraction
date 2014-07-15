/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.CurrencyPairService;
import com.finaxys.rd.dataextraction.service.integration.gateway.CurrencyPairMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyPairsJob.
 */
public class CurrencyPairsJob extends QuartzJobBean {

	/** The currency pair service. */
	private CurrencyPairMsgGateway currencyPairMsgGateway;

	public CurrencyPairMsgGateway getCurrencyPairMsgGateway() {
		return currencyPairMsgGateway;
	}

	public void setCurrencyPairMsgGateway(CurrencyPairMsgGateway currencyPairMsgGateway) {
		this.currencyPairMsgGateway = currencyPairMsgGateway;
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
			currencyPairMsgGateway.publishCurrencyPairs();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}