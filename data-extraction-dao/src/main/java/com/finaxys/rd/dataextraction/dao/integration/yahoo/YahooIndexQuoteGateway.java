package com.finaxys.rd.dataextraction.dao.integration.yahoo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.finaxys.rd.dataextraction.dao.exception.GatewayCommunicationException;
import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.dao.exception.GatewaySecurityException;
import com.finaxys.rd.dataextraction.dao.exception.ParserException;
import com.finaxys.rd.dataextraction.dao.helper.YahooGatewayHelper;
import com.finaxys.rd.dataextraction.dao.integration.EODDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.HistDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.IntradayDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Enum.ContentType;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.Index;
import com.finaxys.rd.dataextraction.domain.IndexQuote;

public class YahooIndexQuoteGateway implements IntradayDataGateway<IndexQuote, Index>, EODDataGateway<IndexQuote, Index>, HistDataGateway<IndexQuote, Index> {

	@Value("${gateway.yahoo.yqlIndexQuotesQuery}")
	private String CURRENT_INDEX_QUOTES_QUERY;

	@Value("${gateway.yahoo.yqlEODIndexQuotesQuery}")
	private String EOD_INDEX_QUOTES_QUERY;

	@Value("${gateway.yahoo.yqlHistIndexQuotesQuery}")
	private String HIST_INDEX_QUOTES_QUERY;

	@Autowired
	private CloseableHttpClient httpClient;

	private final HttpContext context;
	/** The content type. */
	private ContentType contentType;

	private Parser<IndexQuote> intradayDataParser;

	private Parser<IndexQuote> eodDataParser;

	private Parser<IndexQuote> histDataParser;

	public YahooIndexQuoteGateway(ContentType contentType, Parser<IndexQuote> intradayDataParser, Parser<IndexQuote> eodDataParser, Parser<IndexQuote> histDataParser) {
		super();
		this.contentType = contentType;
		this.intradayDataParser = intradayDataParser;
		this.eodDataParser = eodDataParser;
		this.histDataParser = histDataParser;
		this.context = HttpClientContext.create();
	}

	public YahooIndexQuoteGateway(CloseableHttpClient httpClient, ContentType contentType, Parser<IndexQuote> intradayDataParser, Parser<IndexQuote> eodDataParser,
			Parser<IndexQuote> histDataParser) {
		super();
		this.httpClient = httpClient;
		this.contentType = contentType;
		this.intradayDataParser = intradayDataParser;
		this.eodDataParser = eodDataParser;
		this.histDataParser = histDataParser;
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

	public Parser<IndexQuote> getIntradayDataParser() {
		return intradayDataParser;
	}

	public void setIntradayDataParser(Parser<IndexQuote> intradayDataParser) {
		this.intradayDataParser = intradayDataParser;
	}

	public Parser<IndexQuote> getEodDataParser() {
		return eodDataParser;
	}

	public void setEodDataParser(Parser<IndexQuote> eodDataParser) {
		this.eodDataParser = eodDataParser;
	}

	public Parser<IndexQuote> getHistDataParser() {
		return histDataParser;
	}

	public void setHistDataParser(Parser<IndexQuote> histDataParser) {
		this.histDataParser = histDataParser;
	}

	private String getSymbols(List<Index> indexes) {
		StringBuilder sb = new StringBuilder();
		if (indexes != null && indexes.size() > 0)
			for (Index index : indexes) {
				if (index.getExchSymb().equals("US")) {
					sb.append("\"" + index.getSymbol() + "\",");
				} else {
					sb.append("\"" + index.getSymbol() + "." + index.getExchSymb() + "\",");
				}
			}

		return sb.toString().replaceAll(",$", "");
	}

	@Override
	public List<IndexQuote> getEODData(List<Index> products) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			List<String> params = new ArrayList<String>(Arrays.asList(getSymbols(products)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(EOD_INDEX_QUOTES_QUERY, params, contentType, httpClient, context);
			if (data.length > 0)
				return eodDataParser.parse(new Document(data, DataType.EOD));

			return null;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException e) {
			throw new GatewaySecurityException(e);
		} catch (OAuthCommunicationException | URISyntaxException | IOException e) {
			throw new GatewayCommunicationException(e);
		} catch ( ParserException e) {
			throw new GatewayException(e);
		}
	}

	@Override
	public List<IndexQuote> getCurrentData(List<Index> products) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			List<String> params = new ArrayList<String>(Arrays.asList(getSymbols(products)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(CURRENT_INDEX_QUOTES_QUERY, params, contentType, httpClient, context);
			if (data.length > 0)
				return intradayDataParser.parse(new Document(data, DataType.INTRA));
			else
				return null;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException e) {
			throw new GatewaySecurityException(e);
		} catch (OAuthCommunicationException | URISyntaxException | IOException e) {
			throw new GatewayCommunicationException(e);
		} catch ( ParserException e) {
			throw new GatewayException(e);
		}
	}

	@Override
	public List<IndexQuote> getHistData(List<Index> products, LocalDate startDate, LocalDate endDate) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			Assert.notNull(startDate, "Cannot execute data extraction. Start date is null.");
			Assert.notNull(endDate, "Cannot execute data extraction. End date is null.");

			 DateTimeFormatter dformatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			List<String> params = new ArrayList<String>(Arrays.asList(getSymbols(products), dformatter.print(startDate), dformatter.print(endDate)));

			byte[] data = YahooGatewayHelper.executeYQLQuery(HIST_INDEX_QUOTES_QUERY, params, contentType, httpClient, context);
			if (data.length > 0)
				return intradayDataParser.parse(new Document(data, DataType.HIST));
			else
				return null;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException e) {
			throw new GatewaySecurityException(e);
		} catch (OAuthCommunicationException | URISyntaxException | IOException e) {
			throw new GatewayCommunicationException(e);
		} catch ( ParserException e) {
			throw new GatewayException(e);
		}
	}

}