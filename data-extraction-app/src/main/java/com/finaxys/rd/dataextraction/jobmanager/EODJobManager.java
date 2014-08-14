package com.finaxys.rd.dataextraction.jobmanager;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.joda.time.LocalTime;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerUtils;
import org.quartz.spi.OperableTrigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import com.finaxys.rd.dataextraction.dao.CurrencyPairDao;
import com.finaxys.rd.dataextraction.dao.ExchangeDao;
import com.finaxys.rd.dataextraction.dao.IndexDao;
import com.finaxys.rd.dataextraction.dao.OptionDao;
import com.finaxys.rd.dataextraction.dao.StockDao;
import com.finaxys.rd.dataextraction.dao.impl.AbstractBasicDao;
import com.finaxys.rd.dataextraction.domain.CurrencyPair;
import com.finaxys.rd.dataextraction.domain.Exchange;
import com.finaxys.rd.dataextraction.domain.Index;
import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.domain.Option;
import com.finaxys.rd.dataextraction.domain.Stock;
import com.finaxys.rd.dataextraction.service.EODDataService;
import com.finaxys.rd.dataextraction.service.integration.gateway.MarketDataPublishingGateway;

public class EODJobManager {
	
	private static Logger logger = Logger.getLogger(EODJobManager.class);

	ApplicationContext context;

	public EODJobManager(ApplicationContext context) {
		super();
		this.context = context;
	}

	public void scheduleEODOptionQuotesJobs(char provider, String exchangeDaoBeanName, String productDaoBeanName, String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {

				List<Option> products = ((OptionDao) context.getBean(productDaoBeanName)).list(provider, exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products, serviceBeanName, publishingGatewayBeanName, bucket);

				scheduleJob(com.finaxys.rd.dataextraction.job.OptionQuotesJob.class, jobData, exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleEODOQuotesJobs(char provider, String exchangeDaoBeanName, String productDaoBeanName, String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {
				List<Option> products = ((OptionDao) context.getBean(productDaoBeanName)).list(provider, exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products, serviceBeanName, publishingGatewayBeanName, bucket);
				scheduleJob(com.finaxys.rd.dataextraction.job.EODDataJob.class, jobData, exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleEODIndexQuotesJobs(char provider, String exchangeDaoBeanName, String productDaoBeanName, String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {
				List<Index> products = ((IndexDao) context.getBean(productDaoBeanName)).list(provider, exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products, serviceBeanName, publishingGatewayBeanName, bucket);

				scheduleJob(com.finaxys.rd.dataextraction.job.IndexQuotesJob.class, jobData, exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleEODStockQuotesJobs(char provider, String exchangeDaoBeanName, String productDaoBeanName, String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {
				List<Stock> products = ((StockDao) context.getBean(productDaoBeanName)).list(provider, exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products, serviceBeanName, publishingGatewayBeanName, bucket);
				scheduleJob(com.finaxys.rd.dataextraction.job.StockQuotesJob.class, jobData, exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleEODFXRatesJobs(char provider, String exchangeDaoBeanName, String productDaoBeanName, String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);
		List<CurrencyPair> products = ((CurrencyPairDao) context.getBean(productDaoBeanName)).list(provider);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {

				Map<String, Object> jobData = getJobData(products, serviceBeanName, publishingGatewayBeanName, bucket);
				scheduleJob(com.finaxys.rd.dataextraction.job.FXRatesJob.class, jobData, exchange.getCloseTime(), exchange.getDelay());
			}
		}

	}

	private Map<String, Object> getJobData(List<? extends MarketData> products, String serviceBeanName, String publishingGatewayBeanName, Integer bucket) {
		Map<String, Object> jobData = new HashMap<String, Object>();
		jobData.put("products", products);
		jobData.put("service", (EODDataService<?, ?>) context.getBean(serviceBeanName));
		jobData.put("publishingGateway", (MarketDataPublishingGateway<?>) context.getBean(publishingGatewayBeanName));
		jobData.put("executorService", (ExecutorService) context.getBean("executorService"));
		jobData.put("bucketSize", bucket);

		return jobData;

	}

	private void scheduleJob(Class<?> jobClass, Map<String, Object> jobData, LocalTime closeTime, Integer delay) throws SchedulerException {

		Scheduler sfb = (Scheduler) context.getBean("marketDataJobsScheduler");

		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setGroup("marketDataJobsGroup");
		jobDetailFactory.setBeanName("jobDetail-" + jobClass.getSimpleName() + "-" + jobData.hashCode());
		jobDetailFactory.setJobClass(jobClass);

		jobDetailFactory.setJobDataAsMap(jobData);

		jobDetailFactory.setDurability(true);
		jobDetailFactory.afterPropertiesSet();
		JobDetail jobDetail = (JobDetail) jobDetailFactory.getObject();

		CronTrigger trigger = createTrigger("trigger-" + jobClass.getSimpleName() + "-" + jobData.hashCode(), jobDetail, closeTime, delay);

		sfb.scheduleJob(jobDetail, trigger);

		if (!sfb.isStarted())
			sfb.start();

	}

	private CronTrigger createTrigger(String name, JobDetail jobDetail, LocalTime closeTime, Integer delay) {

		String cronExpression = createCronExpression(closeTime, delay);

		CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
		cronTrigger.setGroup("marketDataJobsGroup");
		cronTrigger.setJobDetail(jobDetail);
		cronTrigger.setBeanName(name);
		cronTrigger.setCronExpression(cronExpression);
		cronTrigger.afterPropertiesSet();

		// check what time the trigger will first fire
		List<?> fireTimes = TriggerUtils.computeFireTimes((OperableTrigger) cronTrigger.getObject(), null, 1);
		Date firstFireTime = (Date) fireTimes.iterator().next();

		logger.info("First fire time: " + firstFireTime);

		return (CronTrigger) cronTrigger.getObject();

	}

	private String createCronExpression(LocalTime closeTime, Integer delay) {
		closeTime = closeTime.plusSeconds(delay);
		return closeTime.getSecondOfMinute() + " " + (closeTime.getMinuteOfHour() + 2) + " " + closeTime.getHourOfDay() + " ? * MON-FRI *";
	}

}
