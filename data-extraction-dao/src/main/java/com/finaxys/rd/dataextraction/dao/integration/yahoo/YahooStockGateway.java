package com.finaxys.rd.dataextraction.dao.integration.yahoo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.exception.GatewayCommunicationException;
import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.dao.exception.GatewaySecurityException;
import com.finaxys.rd.dataextraction.dao.exception.ParserException;
import com.finaxys.rd.dataextraction.dao.helper.YahooGatewayHelper;
import com.finaxys.rd.dataextraction.dao.integration.RefDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Enum.ContentType;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.Stock;

public class YahooStockGateway implements RefDataGateway<Stock> {

	/** The yql stock query. */
	@Value("${gateway.yahoo.yqlStockQuery}")
	private String STOCK_QUERY;

	@Autowired
	private CloseableHttpClient httpClient;

	private final HttpContext context;

	/** The content type. */
	private ContentType contentType;

	private Parser<Stock> parser;

	public YahooStockGateway(ContentType contentType, Parser<Stock> parser) {
		super();
		this.contentType = contentType;
		this.parser = parser;
		this.context = HttpClientContext.create();
	}

	public YahooStockGateway(CloseableHttpClient httpClient, ContentType contentType, Parser<Stock> parser) {
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

	public Parser<Stock> getParser() {
		return parser;
	}

	public void setParser(Parser<Stock> parser) {
		this.parser = parser;
	}

	@Override
	public List<Stock> getRefData() throws GatewayException {
		try {
			List<String> params = new ArrayList<String>();
			byte[] data = YahooGatewayHelper.executeYQLQuery(STOCK_QUERY, params, contentType, httpClient, context);
			if (data.length > 0)
				return parser.parse(new Document(data, DataType.REF));

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
