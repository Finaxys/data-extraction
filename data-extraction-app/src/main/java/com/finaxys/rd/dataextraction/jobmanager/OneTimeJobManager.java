package com.finaxys.rd.dataextraction.jobmanager;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerUtils;
import org.quartz.spi.OperableTrigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.finaxys.rd.dataextraction.dao.ExchangeDao;
import com.finaxys.rd.dataextraction.dao.IndexDao;
import com.finaxys.rd.dataextraction.dao.InterbankRateDao;
import com.finaxys.rd.dataextraction.dao.StockDao;
import com.finaxys.rd.dataextraction.domain.Exchange;
import com.finaxys.rd.dataextraction.service.RefDataService;
import com.finaxys.rd.dataextraction.service.RefOptionChainService;
import com.finaxys.rd.dataextraction.service.impl.HistDataServiceImpl;
import com.finaxys.rd.dataextraction.service.integration.gateway.MarketDataPublishingGateway;

public class OneTimeJobManager {
	private static Logger logger = Logger.getLogger(OneTimeJobManager.class);

	ApplicationContext context;

	public OneTimeJobManager(ApplicationContext context) {
		super();
		this.context = context;
	}

	public void startRefDataJob(String serviceBeanName,
			String publishingGatewayBeanName) throws IOException,
			SchedulerException {

		Map<String, Object> jobData = new HashMap<String, Object>();
		jobData.put("service",
				(RefDataService<?>) context.getBean(serviceBeanName));
		jobData.put("publishingGateway",
				(MarketDataPublishingGateway<?>) context
						.getBean(publishingGatewayBeanName));
		this.startJob(com.finaxys.rd.dataextraction.job.RefDataJob.class,
				jobData);
	}
	
	
	public void startOptionChainsJob(char provider, String exchangeDaoBeanName,
			String stockDaoBeanName, String serviceBeanName,
			String publishingGatewayBeanName, int bucket) throws IOException,
			SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context
				.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {

				Map<String, Object> jobData = new HashMap<String, Object>();
				jobData.put("service", (RefOptionChainService) context
						.getBean(serviceBeanName));
				jobData.put("publishingGateway",
						(MarketDataPublishingGateway<?>) context
								.getBean(publishingGatewayBeanName));
				jobData.put("stockDao",
						(StockDao) context.getBean(stockDaoBeanName));
				jobData.put("executorService",
						(ExecutorService) context.getBean("executorService"));
				jobData.put("exchSymb", exchange.getSourceSymbol());
				jobData.put("provider", provider);
				jobData.put("bucketSize", bucket);

				this.startJob(
						com.finaxys.rd.dataextraction.job.OptionChainsJob.class,
						jobData);
			}
		}

	}

	public void startHistStockQuotesJob(char provider,
			String exchangeDaoBeanName, String startDate, String endDate,
			String stockDaoBeanName, String serviceBeanName,
			String publishingGatewayBeanName, int bucket) throws IOException,
			SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context
				.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {

				Map<String, Object> jobData = new HashMap<String, Object>();
				jobData.put("service", (HistDataServiceImpl<?, ?>) context
						.getBean(serviceBeanName));
				jobData.put("publishingGateway",
						(MarketDataPublishingGateway<?>) context
								.getBean(publishingGatewayBeanName));
				jobData.put("dao", (StockDao) context.getBean(stockDaoBeanName));
				jobData.put("executorService",
						(ExecutorService) context.getBean("executorService"));
				jobData.put("exchSymb", exchange.getSourceSymbol());
				jobData.put("provider", provider);
				jobData.put("bucketSize", bucket);
				jobData.put("startDate", startDate);
				jobData.put("endDate", endDate);

				this.startJob(
						com.finaxys.rd.dataextraction.job.HistStockQuotesJob.class,
						jobData);
			}
		}

	}

	public void startHistIndexQuotesJob(char provider,
			String exchangeDaoBeanName, String startDate, String endDate,
			String indexDaoBeanName, String serviceBeanName,
			String publishingGatewayBeanName, int bucket) throws IOException,
			SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context
				.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {

				Map<String, Object> jobData = new HashMap<String, Object>();
				jobData.put("service", (HistDataServiceImpl<?, ?>) context
						.getBean(serviceBeanName));
				jobData.put("publishingGateway",
						(MarketDataPublishingGateway<?>) context
								.getBean(publishingGatewayBeanName));
				jobData.put("dao", (IndexDao) context.getBean(indexDaoBeanName));
				jobData.put("executorService",
						(ExecutorService) context.getBean("executorService"));
				jobData.put("exchSymb", exchange.getSourceSymbol());
				jobData.put("provider", provider);
				jobData.put("bucketSize", bucket);
				jobData.put("startDate", startDate);
				jobData.put("endDate", endDate);

				this.startJob(
						com.finaxys.rd.dataextraction.job.HistIndexQuotesJob.class,
						jobData);
			}
		}

	}

	public void startInterbankRateDataJob(char provider, String startDate,
			String endDate, String interbankRateDaoBeanName,
			String serviceBeanName, String publishingGatewayBeanName)
			throws IOException, SchedulerException {

		Map<String, Object> jobData = new HashMap<String, Object>();
		jobData.put("service",
				(RefDataService<?>) context.getBean(serviceBeanName));
		jobData.put("publishingGateway",
				(MarketDataPublishingGateway<?>) context
						.getBean(publishingGatewayBeanName));
		jobData.put("interbankRateDao",
				(InterbankRateDao) context.getBean(interbankRateDaoBeanName));
		jobData.put("provider", provider);
		jobData.put("startDate", startDate);
		jobData.put("endDate", endDate);
		this.startJob(
				com.finaxys.rd.dataextraction.job.HistInterbankRateDataJob.class,
				jobData);
	}

	

	

	private void startJob(Class<?> jobClass, Map<String, Object> jobData)
			throws SchedulerException {

		Scheduler sfb = (Scheduler) context.getBean("marketDataJobsScheduler");

		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setGroup("marketDataJobsGroup");
		jobDetailFactory.setBeanName("jobDetail-" + jobClass.getSimpleName()
				+ "-" + jobData.hashCode());
		jobDetailFactory.setJobClass(jobClass);

		jobDetailFactory.setJobDataAsMap(jobData);

		jobDetailFactory.setDurability(true);
		jobDetailFactory.afterPropertiesSet();
		JobDetail jobDetail = (JobDetail) jobDetailFactory.getObject();

		SimpleTrigger trigger = createSimpleTrigger(
				"trigger-" + jobClass.getSimpleName() + "-"
						+ jobData.hashCode(), jobDetail);

		sfb.scheduleJob(jobDetail, trigger);

		if (!sfb.isStarted())
			sfb.start();

	}

	private SimpleTrigger createSimpleTrigger(String name, JobDetail jobDetail) {

		SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
		trigger.setGroup("marketDataJobsGroup");
		trigger.setJobDetail(jobDetail);
		trigger.setBeanName(name);
		trigger.setRepeatCount(0);
		trigger.setRepeatInterval(0);
		trigger.afterPropertiesSet();

		// check what time the trigger will first fire
		List<?> fireTimes = TriggerUtils.computeFireTimes(
				(OperableTrigger) trigger.getObject(), null, 1);
		Date firstFireTime = (Date) fireTimes.iterator().next();

		logger.info("First fire time: " + firstFireTime);

		return (SimpleTrigger) trigger.getObject();

	}

}
