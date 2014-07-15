package com.finaxys.rd.dataextraction.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.service.integration.gateway.InterbankRateMsgGateway;

public class InterbankRatesJob extends QuartzJobBean {
	
	private InterbankRateMsgGateway interbankRateMsgGateway;



	public InterbankRateMsgGateway getInterbankRateMsgGateway() {
		return interbankRateMsgGateway;
	}



	public void setInterbankRateMsgGateway(InterbankRateMsgGateway interbankRateMsgGateway) {
		this.interbankRateMsgGateway = interbankRateMsgGateway;
	}



	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			interbankRateMsgGateway.publishRates();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
