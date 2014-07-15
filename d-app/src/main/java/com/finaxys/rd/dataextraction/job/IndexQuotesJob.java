/*
 * 
 */
package com.finaxys.rd.dataextraction.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.service.integration.gateway.IndexQuoteMsgGateway;
import com.finaxys.rd.dataextraction.service.integration.publisher.Publisher;
import com.finaxys.rd.marketdataprovider.dao.IndexInfoDao;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexQuotesJob.
 */
public class IndexQuotesJob extends QuartzJobBean {

	/** The index quote service. */
	private IndexQuoteMsgGateway indexQuoteMsgGateway;

	private IndexInfoDao indexInfoDao;

	
	public IndexQuoteMsgGateway getIndexQuoteMsgGateway() {
		return indexQuoteMsgGateway;
	}

	public void setIndexQuoteMsgGateway(IndexQuoteMsgGateway indexQuoteMsgGateway) {
		this.indexQuoteMsgGateway = indexQuoteMsgGateway;
	}

	public IndexInfoDao getIndexInfoDao() {
		return indexInfoDao;
	}

	public void setIndexInfoDao(IndexInfoDao indexInfoDao) {
		this.indexInfoDao = indexInfoDao;
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
			List<String> symbolsGp = indexInfoDao.listAllSymbols();
			for (String symbols : symbolsGp)
				indexQuoteMsgGateway.publishCurrentIndexQuotesList(symbols);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
