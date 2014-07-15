/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.integration.gateway.OptionQuoteMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionQuotesJob.
 */
public class OptionQuotesJob extends QuartzJobBean {

	/** The option quote service. */
	private OptionQuoteMsgGateway optionQuoteMsgGateway;

//	private OptionChainDao optionChainDao;

	public OptionQuoteMsgGateway getOptionQuoteMsgGateway() {
		return optionQuoteMsgGateway;
	}

	public void setOptionQuoteMsgGateway(OptionQuoteMsgGateway optionQuoteMsgGateway) {
		this.optionQuoteMsgGateway = optionQuoteMsgGateway;
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
//			List<String> symbolsGp = optionDao.listAllSymbols();
//			for (String symbols : symbolsGp)
//				optionQuoteMsgGateway.publishCurrentOptionsQuotesList(symbols, );

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
