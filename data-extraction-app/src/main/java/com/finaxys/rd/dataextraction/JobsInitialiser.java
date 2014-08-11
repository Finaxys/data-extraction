package com.finaxys.rd.dataextraction;

import java.io.IOException;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.finaxys.rd.dataextraction.jobmanager.EODJobManager;
import com.finaxys.rd.dataextraction.jobmanager.IntradayJobManager;
import com.finaxys.rd.dataextraction.jobmanager.OneTimeJobManager;

@Component("jobsInitialiser")
@Scope("prototype")
public class JobsInitialiser {

	@Value("${gateway.yahoo.symbol}")
	private char YAHOO_PROVIDER_SYMBOL;

	@Value("${gateway.file.symbol}")
	private char FILE_PROVIDER_SYMBOL;

	@Value("${gateway.ebf.symbol}")
	private char EBF_PROVIDER_SYMBOL;

	OneTimeJobManager oneTimeJobManager;
	IntradayJobManager intradayJobManager;
	EODJobManager eodJobManager;

	public JobsInitialiser(OneTimeJobManager oneTimeJobManager,
			IntradayJobManager intradayJobManager, EODJobManager eodJobManager) {
		super();
		this.oneTimeJobManager = oneTimeJobManager;
		this.intradayJobManager = intradayJobManager;
		this.eodJobManager = eodJobManager;
	}

	public void init() throws IOException, SchedulerException {

//		intradayJobManager.scheduleStockQuotesJobs(YAHOO_PROVIDER_SYMBOL,
//				"exchangeDao", "stockDao", "yahooXmlStockQuoteService",
//				"marketDataPublishingGateway", 100);
//		eodJobManager.scheduleEODStockQuotesJobs(YAHOO_PROVIDER_SYMBOL,
//				"exchangeDao", "stockDao", "yahooXmlEODStockQuoteService",
//				"marketDataPublishingGateway", 100);
//
//		intradayJobManager.scheduleFXRatesJobs(YAHOO_PROVIDER_SYMBOL,
//				"exchangeDao", "currencyPairDao", "yahooXmlFXRateService",
//				"marketDataPublishingGateway", 100);
//
//		eodJobManager.scheduleEODFXRatesJobs(YAHOO_PROVIDER_SYMBOL,
//				"exchangeDao", "currencyPairDao", "yahooXmlEODFXRateService",
//				"marketDataPublishingGateway", 100);
//
//		intradayJobManager.scheduleIndexQuotesJobs(YAHOO_PROVIDER_SYMBOL,
//				"exchangeDao", "indexDao", "yahooXmlIndexQuoteService",
//				"marketDataPublishingGateway", 100);
//
//		eodJobManager.scheduleEODIndexQuotesJobs(YAHOO_PROVIDER_SYMBOL,
//				"exchangeDao", "indexDao", "yahooXmlEODIndexQuoteService",
//				"marketDataPublishingGateway", 100);
//
		intradayJobManager.scheduleOQuotesJobs(YAHOO_PROVIDER_SYMBOL,
				"exchangeDao", "optionDao", "yahooXmlOptionQuoteService",
				"marketDataPublishingGateway", 100);
//
//		eodJobManager.scheduleEODOQuotesJobs(YAHOO_PROVIDER_SYMBOL,
//				"exchangeDao", "optionDao", "yahooXmlEODOptionQuoteService",
//				"marketDataPublishingGateway", 200);

//		intradayJobManager.scheduleOptionQuotesJobs(YAHOO_PROVIDER_SYMBOL,
//				"exchangeDao", "optionChainDao", "yahooXmlOptionQuoteService",
//				"marketDataPublishingGateway", 100);
//
//		eodJobManager.scheduleEODOptionQuotesJobs(YAHOO_PROVIDER_SYMBOL,
//				"exchangeDao", "optionChainDao",
//				"yahooXmlEODOptionQuoteService", "marketDataPublishingGateway",
//				100);

//		oneTimeJobManager.startOptionChainsJob(YAHOO_PROVIDER_SYMBOL,
//				"exchangeDao", "stockDao", "yahooXmlOptionChainService",
//				"marketDataPublishingGateway", 100);
//
//		oneTimeJobManager.startRefDataJob("fileXlsExchangeService",
//				"marketDataPublishingGateway");
//		oneTimeJobManager.startRefDataJob("fileXlsCurrencyPairService",
//				"marketDataPublishingGateway");
//		oneTimeJobManager.startRefDataJob("fileXlslIndexService",
//				"marketDataPublishingGateway");
//		oneTimeJobManager.startRefDataJob("fileXlsInterbankRateService",
//				"marketDataPublishingGateway");
//		oneTimeJobManager.startRefDataJob("fileXlsOptionService",
//				"marketDataPublishingGateway");
//		oneTimeJobManager.startRefDataJob("fileXlsStockService",
//				"marketDataPublishingGateway");
		
//		oneTimeJobManager.startHistStockQuotesJob(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "2009-01-01", "2009-02-01", "stockDao", "yahooXmlHistStockQuoteService", "marketDataPublishingGateway", 10);
		
//		oneTimeJobManager.startHistIndexQuotesJob(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "2009-01-01", "2009-02-01", "indexDao", "yahooXmlHistIndexQuoteService", "marketDataPublishingGateway", 2);
	
	}
}
