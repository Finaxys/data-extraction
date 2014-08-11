package com.finaxys.rd.dataextraction.dao.integration.yahoo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.helper.YahooGatewayHelper;
import com.finaxys.rd.dataextraction.dao.integration.RefOptionChainGateway;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Enum.ContentType;
import com.finaxys.rd.dataextraction.domain.Enum.DataClass;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.Document;
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

	
	public YahooOptionChainGateway(ContentType contentType,
			Parser<OptionChain> parser) {
		super();
		this.contentType = contentType;
		this.parser = parser;
		this.context = HttpClientContext.create();
	}

	public YahooOptionChainGateway(CloseableHttpClient httpClient,
			ContentType contentType, Parser<OptionChain> parser) {
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

	private String getSymbols(List<Stock> stocks) {
		StringBuilder sb = new StringBuilder();
		for (Stock stock : stocks) {
			if (stock.getExchSymb().equals("US"))
				sb.append("\"" + stock.getSymbol() + "\",");
			else
				sb.append("\"" + stock.getSymbol() + "." + stock.getExchSymb()
						+ "\",");
		}

		return sb.toString().replaceAll(",$", "");
	}

	@Override
	public List<OptionChain> getRefData(List<Stock> stocks) throws Exception {
		try {
			List<String> params = new ArrayList<String>(
					Arrays.asList(getSymbols(stocks)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(
					OPTION_CHAIN_QUERY, params, contentType, httpClient,
					context);
			if (data.length > 0)
				return parser.parse(new Document(contentType, DataType.INTRA,
						DataClass.OptionChain,
						YahooGatewayHelper.Y_PROVIDER_SYMB, data));
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
