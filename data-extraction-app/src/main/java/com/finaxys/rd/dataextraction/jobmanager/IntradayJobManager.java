package com.finaxys.rd.dataextraction.jobmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.hadoop.hbase.util.Bytes;
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
import com.finaxys.rd.dataextraction.domain.CurrencyPair;
import com.finaxys.rd.dataextraction.domain.Exchange;
import com.finaxys.rd.dataextraction.domain.Index;
import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.domain.Option;
import com.finaxys.rd.dataextraction.domain.Stock;
import com.finaxys.rd.dataextraction.service.IntradayDataService;
import com.finaxys.rd.dataextraction.service.integration.gateway.MarketDataPublishingGateway;

public class IntradayJobManager {

	ApplicationContext context;

	public IntradayJobManager(ApplicationContext context) {
		super();
		this.context = context;
	}

	public void scheduleOptionQuotesJobs(char provider,
			String exchangeDaoBeanName, String productDaoBeanName,
			String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context
				.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {

				List<Option> products = ((OptionDao) context
						.getBean(productDaoBeanName)).list(provider,
						exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products,
						serviceBeanName, publishingGatewayBeanName, bucket);

				scheduleJob(
						com.finaxys.rd.dataextraction.job.OptionQuotesJob.class,
						jobData, exchange.getOpenTime(),
						exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleOQuotesJobs(char provider, String exchangeDaoBeanName,
			String productDaoBeanName, String serviceBeanName,
			String publishingGatewayBeanName, Integer bucket)
			throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context
				.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {
				List<Option> products = ((OptionDao) context
						.getBean(productDaoBeanName)).list(provider,
						exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products,
						serviceBeanName, publishingGatewayBeanName, bucket);
				scheduleJob(com.finaxys.rd.dataextraction.job.OQuotesJob.class,
						jobData, exchange.getOpenTime(),
						exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleIndexQuotesJobs(char provider,
			String exchangeDaoBeanName, String productDaoBeanName,
			String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context
				.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {
				List<Index> products = ((IndexDao) context.getBean(productDaoBeanName))
						.list(provider, exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products,
						serviceBeanName, publishingGatewayBeanName, bucket);

				scheduleJob(
						com.finaxys.rd.dataextraction.job.IndexQuotesJob.class,
						jobData, exchange.getOpenTime(),
						exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleStockQuotesJobs(char provider,
			String exchangeDaoBeanName, String productDaoBeanName,
			String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context
				.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {
				List<Stock> products = ((StockDao) context.getBean(productDaoBeanName))
						.list(provider, exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products,
						serviceBeanName, publishingGatewayBeanName, bucket);
				scheduleJob(
						com.finaxys.rd.dataextraction.job.StockQuotesJob.class,
						jobData, exchange.getOpenTime(),
						exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleFXRatesJobs(char provider, String exchangeDaoBeanName,
			String productDaoBeanName, String serviceBeanName,
			String publishingGatewayBeanName, Integer bucket)
			throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context
				.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);
		List<CurrencyPair> products = ((CurrencyPairDao) context
				.getBean(productDaoBeanName)).list(provider);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {

				Map<String, Object> jobData = getJobData(products,
						serviceBeanName, publishingGatewayBeanName, bucket);
				scheduleJob(com.finaxys.rd.dataextraction.job.FXRatesJob.class,
						jobData, exchange.getOpenTime(),
						exchange.getCloseTime(), exchange.getDelay());
			}
		}

	}

	private Map<String, Object> getJobData(List<? extends MarketData> products,
			String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) {
		Map<String, Object> jobData = new HashMap<String, Object>();
		jobData.put("products", products);
		jobData.put("service",
				(IntradayDataService<?, ?>) context.getBean(serviceBeanName));
		jobData.put("publishingGateway",
				(MarketDataPublishingGateway<?>) context
						.getBean(publishingGatewayBeanName));
		jobData.put("executorService",
				(ExecutorService) context.getBean("executorService"));
		jobData.put("bucketSize", bucket);

		return jobData;

	}

	private void scheduleJob(Class<?> jobClass, Map<String, Object> jobData,
			LocalTime openTime, LocalTime closeTime, Integer delay)
			throws SchedulerException {

		Scheduler scheduler = (Scheduler) context
				.getBean("marketDataJobsScheduler");

		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setGroup("marketDataJobsGroup");
		jobDetailFactory.setBeanName("jobDetail-" + jobClass.getSimpleName()
				+ "-" + jobData.hashCode());
		jobDetailFactory.setJobClass(jobClass);

		jobDetailFactory.setJobDataAsMap(jobData);

		jobDetailFactory.setDurability(true);
		jobDetailFactory.afterPropertiesSet();
		JobDetail jobDetail = (JobDetail) jobDetailFactory.getObject();

		scheduler.addJob(jobDetail, true);

		List<CronTrigger> triggers = createTriggers(
				"trigger-" + jobClass.getSimpleName() + "-"
						+ jobData.hashCode(), jobDetail, openTime, closeTime,
				delay);
try{
		for (CronTrigger trigger : triggers)
			scheduler.scheduleJob(trigger);
}catch(Exception e){
	
}
		if (!scheduler.isStarted())
			scheduler.start();

	}

	private List<CronTrigger> createTriggers(String name, JobDetail jobDetail,
			LocalTime openTime, LocalTime closeTime, Integer delay) {
		List<CronTrigger> triggers = new ArrayList<CronTrigger>();
		List<String> cronExpressions = createCronExpressions(openTime,
				closeTime, delay);
		for (Integer i = 0; i < cronExpressions.size(); i++) {

			CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
			cronTrigger.setGroup("marketDataJobsGroup");
			cronTrigger.setJobDetail(jobDetail);
			cronTrigger.setBeanName(name + "-" + i);
			cronTrigger.setCronExpression(cronExpressions.get(i));
			cronTrigger.afterPropertiesSet();

			// check what time the trigger will first fire
			List<?> fireTimes = TriggerUtils.computeFireTimes(
					(OperableTrigger) cronTrigger.getObject(), null, 1);
			Date firstFireTime = (Date) fireTimes.iterator().next();

			System.out.println("First fire time: " + firstFireTime);

			triggers.add((CronTrigger) cronTrigger.getObject());
		}
		return triggers;
	}

	private List<String> createCronExpressions(LocalTime openTime,
			LocalTime closeTime, Integer delay) {
		List<String> expressions = new ArrayList<String>();
		openTime = openTime.plusSeconds(delay);
		closeTime = closeTime.plusSeconds(delay);
		if (openTime.getMinuteOfHour() == 0 && closeTime.getMinuteOfHour() == 0) {
			expressions.add("0 0/1 " + openTime.getHourOfDay() + "-"
					+ closeTime.getHourOfDay() + " ? * MON-FRI *");
		} else if (openTime.getMinuteOfHour() > 0
				&& closeTime.getMinuteOfHour() == 0) {
			expressions.add("0 " + openTime.getMinuteOfHour() + "-59/1 "
					+ openTime.getHourOfDay() + " ? * MON-FRI *");
			expressions.add("0 0/1 " + (openTime.getHourOfDay() + 1) + "-"
					+ closeTime.getHourOfDay() + " ? * MON-FRI *");
		} else if (openTime.getMinuteOfHour() == 0
				&& closeTime.getMinuteOfHour() > 0) {
			expressions.add("0 0/1 " + openTime.getHourOfDay() + "-"
					+ closeTime.getHourOfDay() + " ? * MON-FRI *");
			expressions.add("0  0-" + closeTime.getMinuteOfHour() + "/1 "
					+ closeTime.getHourOfDay() + " ? * MON-FRI *");
		} else {
			expressions.add("0 " + openTime.getMinuteOfHour() + "-59/1 "
					+ openTime.getHourOfDay() + " ? * MON-FRI *");
			expressions.add("0 0/1 " + (openTime.getHourOfDay() + 1) + "-"
					+ closeTime.getHourOfDay() + " ? * MON-FRI *");
			expressions.add("0  0-" + closeTime.getMinuteOfHour() + "/1 "
					+ closeTime.getHourOfDay() + " ? * MON-FRI *");
		}
		return expressions;
	}

}
