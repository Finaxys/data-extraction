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
import com.finaxys.rd.dataextraction.dao.integration.EODOptionQuoteGateway;
import com.finaxys.rd.dataextraction.dao.integration.IntradayOptionQuoteGateway;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Enum.ContentType;
import com.finaxys.rd.dataextraction.domain.Enum.DataClass;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.Option;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.OptionQuote;

public class YahooOptionQuoteGateway implements IntradayOptionQuoteGateway, EODOptionQuoteGateway {

	/** The yql quote query. */
	@Value("${gateway.yahoo.yqlOptionQuoteQuery}")
	private String CURRENT_OPTION_QUERY;

	@Value("${gateway.yahoo.yqlOQuoteQuery}")
	private String CURRENT_OQUOTE_QUERY;

	/** The yql oquote query. */
	@Value("${gateway.yahoo.yqlEODOptionQuoteQuery}")
	private String EOD_OPTION_QUERY;

	@Value("${gateway.yahoo.yqlEODOQuoteQuery}")
	private String EOD_OQUOTE_QUERY;

	@Autowired
	private CloseableHttpClient httpClient;

	private final HttpContext context;

	/** The content type. */
	private ContentType contentType;

	private Parser<OptionQuote> intradayDataParser;

	private Parser<OptionQuote> eodDataParser;

	public YahooOptionQuoteGateway(ContentType contentType, Parser<OptionQuote> intradayDataParser, Parser<OptionQuote> eodDataParser) {
		super();
		this.contentType = contentType;
		this.intradayDataParser = intradayDataParser;
		this.eodDataParser = eodDataParser;
		this.context = HttpClientContext.create();
	}

	public YahooOptionQuoteGateway(CloseableHttpClient httpClient, ContentType contentType, Parser<OptionQuote> intradayDataParser, Parser<OptionQuote> eodDataParser) {
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

	public Parser<OptionQuote> getIntradayDataParser() {
		return intradayDataParser;
	}

	public void setIntradayDataParser(Parser<OptionQuote> intradayDataParser) {
		this.intradayDataParser = intradayDataParser;
	}

	public Parser<OptionQuote> getEodDataParser() {
		return eodDataParser;
	}

	public void setEodDataParser(Parser<OptionQuote> eodDataParser) {
		this.eodDataParser = eodDataParser;
	}

	@Override
	public List<OptionQuote> getCurrentData(List<Option> products) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			List<String> params = new ArrayList<String>(Arrays.asList(YahooGatewayHelper.getSymbols(products)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(CURRENT_OQUOTE_QUERY, params, contentType, httpClient, context);
			if (data.length > 0)
				return intradayDataParser.parse(new Document(contentType, DataType.INTRA, DataClass.OptionQuote, YahooGatewayHelper.Y_PROVIDER_SYMB, data));

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
	public List<OptionQuote> getCurrentData(List<OptionChain> products, LocalDate expiration) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			 DateTimeFormatter dformatter = DateTimeFormat.forPattern("yyyy-MM");
			List<String> params = new ArrayList<String>(Arrays.asList(YahooGatewayHelper.getSymbols(products), dformatter.print(expiration)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(CURRENT_OPTION_QUERY, params, contentType, httpClient, context);
			if (data.length > 0)
				return intradayDataParser.parse(new Document(data, DataType.INTRA));

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
	public List<OptionQuote> getEODData(List<Option> products) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			List<String> params = new ArrayList<String>(Arrays.asList(YahooGatewayHelper.getSymbols(products)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(EOD_OQUOTE_QUERY, params, contentType, httpClient, context);
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
	public List<OptionQuote> getEODData(List<OptionChain> products, LocalDate expiration) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			 DateTimeFormatter dformatter = DateTimeFormat.forPattern("yyyy-MM");
			List<String> params = new ArrayList<String>(Arrays.asList(YahooGatewayHelper.getSymbols(products), dformatter.print(expiration)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(EOD_OPTION_QUERY, params, contentType, httpClient, context);
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

}
