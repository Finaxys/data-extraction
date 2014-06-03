package app;

import java.io.IOException;

import job.CurrencyPairsJob;
import job.ExchangesJob;
import job.FXRatesJob;
import job.IndexInfosJob;
import job.IndexQuotesJob;
import job.StockQuotesJob;
import job.StocksJob;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import receiver.CurrencyPairReceiver;
import receiver.ExchangeReceiver;
import receiver.FXRateReceiver;
import receiver.IndexInfoReceiver;
import receiver.IndexQuoteReceiver;
import receiver.Receiver;
import receiver.StockQuoteReceiver;
import receiver.StockReceiver;
import subscriber.MomSubscriber;
import subscriber.Subscriber;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import dao.impl.CurrencyPairDaoImpl;
import dao.impl.ExchangeDaoImpl;
import dao.impl.FXRateDaoImpl;
import dao.impl.IndexInfoDaoImpl;
import dao.impl.IndexQuoteDaoImpl;
import dao.impl.StockDaoImpl;
import dao.impl.StockQuoteDaoImpl;

public class MyApp {

	private HConnection hConnection;
	private Connection connection;

	public MyApp(HConnection hConnection, Connection connection) {
		super();
		this.hConnection = hConnection;
		this.connection = connection;
	}

	public HConnection gethConnection() {
		return hConnection;
	}

	public void sethConnection(HConnection hConnection) {
		this.hConnection = hConnection;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void testGetExchanges() {
		try {

			Receiver receiver = new ExchangeReceiver(new ExchangeDaoImpl(hConnection));
			Subscriber subscriber = new MomSubscriber(connection, receiver, ExchangeReceiver.BINDING_KEY);
			subscriber.subscribe();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			JobDetail job = JobBuilder.newJob(ExchangesJob.class).withIdentity("ExchangesJob", "group1").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("ExchangesJob", "group1")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever())
					.build();
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException se) {
			se.printStackTrace();
		}

	}

	public void testGetStocks() {

		try {
			Receiver receiver = new StockReceiver(new StockDaoImpl(hConnection));
			MomSubscriber subscriber = new MomSubscriber(connection, receiver, StockReceiver.BINDING_KEY);
			subscriber.subscribe();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			JobDetail job = JobBuilder.newJob(StocksJob.class).withIdentity("StocksJob", "group1").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("StocksJob", "group1")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever())
					.build();
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException se) {
			se.printStackTrace();
		}

	}

	public void testGetStockQuotes() {

		try {

			Receiver receiver = new StockQuoteReceiver(new StockQuoteDaoImpl(hConnection));
			MomSubscriber subscriber = new MomSubscriber(connection, receiver, StockQuoteReceiver.BINDING_KEY);
			subscriber.subscribe();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			JobDetail job = JobBuilder.newJob(StockQuotesJob.class).withIdentity("StockQuotesJob", "group1").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("StockQuotesJob", "group1")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever())
					.build();
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException se) {
			se.printStackTrace();
		}

	}

	public void testGetFXRates() {

		try {

			Receiver receiver = new FXRateReceiver(new FXRateDaoImpl(hConnection));
			MomSubscriber subscriber = new MomSubscriber(connection, receiver, FXRateReceiver.BINDING_KEY);
			subscriber.subscribe();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			JobDetail job = JobBuilder.newJob(FXRatesJob.class).withIdentity("FXRatesJob", "group1").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("FXRatesJob", "group1")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever())
					.build();
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException se) {
			se.printStackTrace();
		}

	}

	public void testGetIndexQuotes() {

		try {

			// Receiver receiver = new CurrencyPairReceiver(new
			// CurrencyPairDaoImpl(hConnection));
			// MomSubscriber subscriber = new MomSubscriber(connection,
			// receiver, CurrencyPairReceiver.BINDING_KEY);
			// subscriber.subscribe();

			// Receiver receiver = new IndexInfoReceiver(new
			// IndexInfoDaoImpl(hConnection));
			// MomSubscriber subscriber = new MomSubscriber(connection,
			// receiver, IndexInfoReceiver.BINDING_KEY);
			// subscriber.subscribe();

			Receiver receiver = new IndexQuoteReceiver(new IndexQuoteDaoImpl(hConnection));
			MomSubscriber subscriber = new MomSubscriber(connection, receiver, IndexQuoteReceiver.BINDING_KEY);
			subscriber.subscribe();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			JobDetail job = JobBuilder.newJob(IndexQuotesJob.class).withIdentity("IndexQuotesJob", "group1").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("IndexQuotesJob", "group1")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever())
					.build();
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException se) {
			se.printStackTrace();
		}

	}

	public void testGetCurrencyPairs() {

		try {

			Receiver receiver = new CurrencyPairReceiver(new CurrencyPairDaoImpl(hConnection));
			MomSubscriber subscriber = new MomSubscriber(connection, receiver, CurrencyPairReceiver.BINDING_KEY);
			subscriber.subscribe();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			JobDetail job = JobBuilder.newJob(CurrencyPairsJob.class).withIdentity("CurrencyPairsJob", "group1")
					.build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CurrencyPairsJob", "group1")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever())
					.build();
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException se) {
			se.printStackTrace();
		}

	}

	public void testGetIndexInfos() {

		try {

			Receiver receiver = new IndexInfoReceiver(new IndexInfoDaoImpl(hConnection));
			MomSubscriber subscriber = new MomSubscriber(connection, receiver, IndexInfoReceiver.BINDING_KEY);
			subscriber.subscribe();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			JobDetail job = JobBuilder.newJob(IndexInfosJob.class).withIdentity("IndexInfosJob", "group1").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("IndexInfosJob", "group1")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever())
					.build();
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException se) {
			se.printStackTrace();
		}

	}

//	public static void main(String[] args) throws IOException {
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost("localhost");
//		Connection connection = factory.newConnection();
//		HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
//
//		MyApp app = new MyApp(hConnection, connection);
//		app.testGetStockQuotes();
//	}

}
