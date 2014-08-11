package com.finaxys.rd.dataextraction.dao.integration.yahoo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.helper.YahooGatewayHelper;
import com.finaxys.rd.dataextraction.dao.integration.EODDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.IntradayDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.CurrencyPair;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Enum.ContentType;
import com.finaxys.rd.dataextraction.domain.Enum.DataClass;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.FXRate;

public class YahooFXRateGateway implements
		IntradayDataGateway<FXRate, CurrencyPair>,
		EODDataGateway<FXRate, CurrencyPair> {

	@Value("${gateway.yahoo.yqlFXRateQuery}")
	private String CURRENT_FXRATE_QUERY;

	@Value("${gateway.yahoo.yqlEODFXRateQuery}")
	private String EOD_FXRATE_QUERY;
	
	@Autowired
	private CloseableHttpClient httpClient;

	private final HttpContext context;

	/** The content type. */
	private ContentType contentType;

	private Parser<FXRate> intradayDataParser;

	private Parser<FXRate> eodDataParser;

	
	public YahooFXRateGateway(ContentType contentType,
			Parser<FXRate> intradayDataParser, Parser<FXRate> eodDataParser) {
		super();
		this.contentType = contentType;
		this.intradayDataParser = intradayDataParser;
		this.eodDataParser = eodDataParser;
		this.context = HttpClientContext.create();
	}

	public YahooFXRateGateway(CloseableHttpClient httpClient,
			ContentType contentType, Parser<FXRate> intradayDataParser,
			Parser<FXRate> eodDataParser) {
		super();
		this.httpClient = httpClient;
		this.contentType = contentType;
		this.intradayDataParser = intradayDataParser;
		this.eodDataParser = eodDataParser;
		this.context = HttpClientContext.create();
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Parser<FXRate> getIntradayDataParser() {
		return intradayDataParser;
	}

	public void setIntradayDataParser(Parser<FXRate> intradayDataParser) {
		this.intradayDataParser = intradayDataParser;
	}

	public Parser<FXRate> getEodDataParser() {
		return eodDataParser;
	}

	public void setEodDataParser(Parser<FXRate> eodDataParser) {
		this.eodDataParser = eodDataParser;
	}

	private String getSymbols(List<CurrencyPair> currencyPairs) {
		StringBuilder sb = new StringBuilder();
		for (CurrencyPair currencyPair : currencyPairs)
			sb.append("\"" + currencyPair.getSymbol() + "\",");

		return sb.toString().replaceAll(",$", "");
	}

	@Override
	public List<FXRate> getEODData(List<CurrencyPair> products)
			throws Exception {
		try {
			List<String> params = new ArrayList<String>(
					Arrays.asList(getSymbols(products)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(EOD_FXRATE_QUERY,
					params, contentType, httpClient, context);
			if (data.length > 0)
				return eodDataParser.parse(new Document(contentType,
						DataType.EOD, DataClass.FXRate,
						YahooGatewayHelper.Y_PROVIDER_SYMB, data));
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<FXRate> getCurrentData(List<CurrencyPair> products)
			throws Exception {

		try {
			List<String> params = new ArrayList<String>(
					Arrays.asList(getSymbols(products)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(CURRENT_FXRATE_QUERY,
					params, contentType, httpClient, context);
			if (data.length > 0)
				return intradayDataParser.parse(new Document(contentType,
						DataType.INTRA, DataClass.FXRate,
						YahooGatewayHelper.Y_PROVIDER_SYMB, data));
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}