package com.finaxys.rd.dataextraction.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.service.integration.gateway.HistInterbankRateDataMsgGateway;

public class HistInterbankRateDataJob extends QuartzJobBean {

	private HistInterbankRateDataMsgGateway histInterbankRateDataMsgGateway;

	private Integer year;




	public HistInterbankRateDataMsgGateway getHistInterbankRateDataMsgGateway() {
		return histInterbankRateDataMsgGateway;
	}

	public void setHistInterbankRateDataMsgGateway(HistInterbankRateDataMsgGateway histInterbankRateDataMsgGateway) {
		this.histInterbankRateDataMsgGateway = histInterbankRateDataMsgGateway;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			histInterbankRateDataMsgGateway.publishHistInterbankRatesList(year);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
