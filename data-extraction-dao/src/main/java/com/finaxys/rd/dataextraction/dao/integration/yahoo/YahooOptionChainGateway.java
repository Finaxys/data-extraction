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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.finaxys.rd.dataextraction.dao.exception.GatewayCommunicationException;
import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.dao.exception.GatewaySecurityException;
import com.finaxys.rd.dataextraction.dao.exception.ParserException;
import com.finaxys.rd.dataextraction.dao.helper.YahooGatewayHelper;
import com.finaxys.rd.dataextraction.dao.integration.RefOptionChainGateway;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Enum.ContentType;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.Stock;

public class YahooOptionChainGateway implements RefOptionChainGateway {

	/** The logger. */
	static Logger logger = Logger.getLogger(YahooOptionChainGateway.class);

	/** The yql option chain query. */
	@Value("${gateway.yahoo.yqlOptionChainQuery}")
	private String OPTION_CHAIN_QUERY;

	@Autowired
	private CloseableHttpClient httpClient;

	private final HttpContext context;

	/** The content type. */
	private ContentType contentType;

	private Parser<OptionChain> parser;

	public YahooOptionChainGateway(ContentType contentType, Parser<OptionChain> parser) {
		super();
		this.contentType = contentType;
		this.parser = parser;
		this.context = HttpClientContext.create();
	}

	public YahooOptionChainGateway(CloseableHttpClient httpClient, ContentType contentType, Parser<OptionChain> parser) {
		super();
		this.httpClient = httpClient;
		this.contentType = contentType;
		this.parser = parser;
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

	public Parser<OptionChain> getParser() {
		return parser;
	}

	public void setParser(Parser<OptionChain> parser) {
		this.parser = parser;
	}



	@Override
	public List<OptionChain> getRefData(List<Stock> products) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			List<String> params = new ArrayList<String>(Arrays.asList(YahooGatewayHelper.getStocksSymbols(products)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(OPTION_CHAIN_QUERY, params, contentType, httpClient, context);
			if (data.length > 0)
				return parser.parse(new Document(data, DataType.INTRA));
			else
				return null;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException e) {
			throw new GatewaySecurityException(e);
		} catch (OAuthCommunicationException | URISyntaxException | IOException e) {
			throw new GatewayCommunicationException(e);
		} catch (NullPointerException | ParserException e) {
			throw new GatewayException(e);
		}
	}

}
