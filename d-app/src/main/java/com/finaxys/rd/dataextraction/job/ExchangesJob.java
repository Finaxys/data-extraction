/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.integration.gateway.ExchangeMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;

// TODO: Auto-generated Javadoc
/**
 * The Class ExchangesJob.
 */
public class ExchangesJob extends QuartzJobBean {

	/** The exchange service. */
	private ExchangeMsgGateway exchangeMsgGateway;


	public ExchangeMsgGateway getExchangeMsgGateway() {
		return exchangeMsgGateway;
	}

	public void setExchangeMsgGateway(ExchangeMsgGateway exchangeMsgGateway) {
		this.exchangeMsgGateway = exchangeMsgGateway;
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
			exchangeMsgGateway.publishExchanges();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
