/*
 * 
 */
package com.finaxys.rd.dataextraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.finaxys.rd.dataextraction.dao.integration.ebf.EBFInterbankRateDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.file.FileCurrencyPairGateway;
import com.finaxys.rd.dataextraction.dao.integration.file.FileExchangeGateway;
import com.finaxys.rd.dataextraction.dao.integration.file.FileIndexGateway;
import com.finaxys.rd.dataextraction.dao.integration.file.FileInterbankRateDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.file.FileInterbankRateGateway;
import com.finaxys.rd.dataextraction.dao.integration.file.FileOptionGateway;
import com.finaxys.rd.dataextraction.dao.integration.file.FileStockGateway;
import com.finaxys.rd.dataextraction.dao.integration.yahoo.YahooFXRateGateway;
import com.finaxys.rd.dataextraction.dao.integration.yahoo.YahooIndexQuoteGateway;
import com.finaxys.rd.dataextraction.dao.integration.yahoo.YahooOptionChainGateway;
import com.finaxys.rd.dataextraction.dao.integration.yahoo.YahooOptionQuoteGateway;
import com.finaxys.rd.dataextraction.dao.integration.yahoo.YahooStockGateway;
import com.finaxys.rd.dataextraction.dao.integration.yahoo.YahooStockQuoteGateway;
import com.finaxys.rd.dataextraction.domain.CurrencyPair;
import com.finaxys.rd.dataextraction.domain.Exchange;
import com.finaxys.rd.dataextraction.domain.FXRate;
import com.finaxys.rd.dataextraction.domain.Index;
import com.finaxys.rd.dataextraction.domain.IndexQuote;
import com.finaxys.rd.dataextraction.domain.InterbankRate;
import com.finaxys.rd.dataextraction.domain.InterbankRateData;
import com.finaxys.rd.dataextraction.domain.Option;
import com.finaxys.rd.dataextraction.domain.Stock;
import com.finaxys.rd.dataextraction.domain.StockQuote;
import com.finaxys.rd.dataextraction.service.impl.EODDataServiceImpl;
import com.finaxys.rd.dataextraction.service.impl.EODOptionQuoteServiceImpl;
import com.finaxys.rd.dataextraction.service.impl.HistDataServiceImpl;
import com.finaxys.rd.dataextraction.service.impl.IntradayDataServiceImpl;
import com.finaxys.rd.dataextraction.service.impl.OptionChainServiceImpl;
import com.finaxys.rd.dataextraction.service.impl.OptionQuoteServiceImpl;
import com.finaxys.rd.dataextraction.service.impl.RefDataServiceImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class Application.
 */
@Configuration
public class ServicesConfig {

	@Autowired
	@Qualifier("yahooXmlFXRateGateway")
	YahooFXRateGateway yahooXmlFXRateGateway;
	
	
	@Autowired
	@Qualifier("yahooXmlIndexQuoteGateway")
	YahooIndexQuoteGateway yahooXmlIndexQuoteGateway;
	
	@Autowired
	@Qualifier("yahooXmlStockGateway")
	YahooStockGateway yahooXmlStockGateway;
	
	@Autowired
	@Qualifier("yahooXmlStockQuoteGateway")
	YahooStockQuoteGateway yahooXmlStockQuoteGateway;
	
	@Autowired
	@Qualifier("yahooXmlOptionChainGateway")
	YahooOptionChainGateway yahooXmlOptionChainGateway;
	
	@Autowired
	@Qualifier("yahooXmlOptionQuoteGateway")
	YahooOptionQuoteGateway yahooXmlOptionQuoteGateway;
	

	@Autowired
	@Qualifier("fileXlsCurrencyPairGateway")
	FileCurrencyPairGateway fileXlsCurrencyPairGateway;
	
	

	@Autowired
	@Qualifier("fileXlsExchangeGateway")
	FileExchangeGateway fileXlsExchangeGateway;
	
	

	@Autowired
	@Qualifier("fileXlsIndexGateway")
	FileIndexGateway fileXlsIndexGateway;
	
	

	@Autowired
	@Qualifier("fileXlsInterbankRateGateway")
	FileInterbankRateGateway fileXlsInterbankRateGateway;
	
	

	@Autowired
	@Qualifier("fileXlsInterbankRateDataGateway")
	FileInterbankRateDataGateway fileXlsInterbankRateDataGateway;
	

	@Autowired
	@Qualifier("fileXlsStockGateway")
	FileStockGateway fileXlsStockGateway;
	

	@Autowired
	@Qualifier("fileXlsOptionGateway")
	FileOptionGateway fileXlsOptionGateway;
	

	@Autowired
	@Qualifier("ebfXlsInterbankRateDataGateway")
	EBFInterbankRateDataGateway ebfXlsInterbankRateDataGateway;
	
	

	
	
	
	
	@Bean
	RefDataServiceImpl<CurrencyPair> fileXlsCurrencyPairService(){
		return new RefDataServiceImpl<CurrencyPair>(fileXlsCurrencyPairGateway);
	}
	
	
	
	@Bean
	RefDataServiceImpl<Exchange> fileXlsExchangeService(){
		return new RefDataServiceImpl<Exchange>(fileXlsExchangeGateway);
	}	
	
	@Bean
	RefDataServiceImpl<Stock> fileXlsStockService(){
		return new RefDataServiceImpl<Stock>(fileXlsStockGateway);
	}	
	
	@Bean
	RefDataServiceImpl<Option> fileXlsOptionService(){
		return new RefDataServiceImpl<Option>(fileXlsOptionGateway);
	}	
	
	@Bean
	RefDataServiceImpl<Index> fileXlslIndexService(){
		return new RefDataServiceImpl<Index>(fileXlsIndexGateway);
	}	
	
	@Bean
	RefDataServiceImpl<InterbankRate> fileXlsInterbankRateService(){
		return new RefDataServiceImpl<InterbankRate>(fileXlsInterbankRateGateway);
	}	
	
	@Bean
	HistDataServiceImpl<InterbankRateData, InterbankRate> fileXlsHistInterbankRateDataService(){
		return new HistDataServiceImpl<InterbankRateData, InterbankRate>(fileXlsInterbankRateDataGateway);
	}	
	
	@Bean
	IntradayDataServiceImpl<FXRate, CurrencyPair> yahooXmlFXRateService(){
		return new IntradayDataServiceImpl<FXRate, CurrencyPair>(yahooXmlFXRateGateway);
	}	
	
	@Bean
	EODDataServiceImpl<FXRate, CurrencyPair> yahooXmlEODFXRateService(){
		return new EODDataServiceImpl<FXRate, CurrencyPair>(yahooXmlFXRateGateway);
	}	
	
	@Bean
	IntradayDataServiceImpl<IndexQuote,Index> yahooXmlIndexQuoteService(){
		return new IntradayDataServiceImpl<IndexQuote, Index>(yahooXmlIndexQuoteGateway);
	}	
	
	@Bean
	EODDataServiceImpl<IndexQuote,Index>  yahooXmlEODIndexQuoteService(){
		return new EODDataServiceImpl<IndexQuote,Index> (yahooXmlIndexQuoteGateway);
	}	
	
	@Bean
	HistDataServiceImpl<IndexQuote,Index>  yahooXmlHistIndexQuoteService(){
		return new HistDataServiceImpl<IndexQuote,Index> (yahooXmlIndexQuoteGateway);
	}	
	
	@Bean
	IntradayDataServiceImpl<StockQuote, Stock> yahooXmlStockQuoteService(){
		return new IntradayDataServiceImpl<StockQuote, Stock>(yahooXmlStockQuoteGateway);
	}	
	
	@Bean
	EODDataServiceImpl<StockQuote, Stock> yahooXmlEODStockQuoteService(){
		return new EODDataServiceImpl<StockQuote, Stock>(yahooXmlStockQuoteGateway);
	}	
	
	@Bean
	HistDataServiceImpl<StockQuote, Stock> yahooXmlHistStockQuoteService(){
		return new HistDataServiceImpl<StockQuote, Stock>(yahooXmlStockQuoteGateway);
	}	
	
	@Bean
	OptionChainServiceImpl yahooXmlOptionChainService(){
		return new OptionChainServiceImpl(yahooXmlOptionChainGateway);
	}	
	
	@Bean
	OptionQuoteServiceImpl yahooXmlOptionQuoteService(){
		return new OptionQuoteServiceImpl(yahooXmlOptionQuoteGateway);
	}	
	
	@Bean
	EODOptionQuoteServiceImpl yahooXmlEODOptionQuoteService(){
		return new EODOptionQuoteServiceImpl(yahooXmlOptionQuoteGateway);
	}	
	
	@Bean
	HistDataServiceImpl<InterbankRateData, InterbankRate> ebfXlsHistInterbankRateDataService(){
		return new HistDataServiceImpl<InterbankRateData, InterbankRate>(ebfXlsInterbankRateDataGateway);
	}	
	



}
