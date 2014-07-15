/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.integration.gateway.IndexInfoMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexInfosJob.
 */
public class IndexInfosJob extends QuartzJobBean {

	/** The index info service. */
	private IndexInfoMsgGateway indexInfoMsgGateway;


	/**
	 * Gets the index info service.
	 *
	 * @return the index info service
	 */
	public IndexInfoMsgGateway getIndexInfoMsgGateway() {
		return indexInfoMsgGateway;
	}

	/**
	 * Sets the index info service.
	 *
	 * @param indexInfoMsgGateway the new index info service
	 */
	public void setIndexInfoMsgGateway(IndexInfoMsgGateway indexInfoMsgGateway) {
		this.indexInfoMsgGateway = indexInfoMsgGateway;
	}
	

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			indexInfoMsgGateway.publishIndexInfos();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
