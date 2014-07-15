package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class YahooXmlStockQuotesConverterTest {

	YahooXmlStockQuotesConverter f;
	String xmlHeader;

	@Before
	public void setUp() throws Exception {
		f = new YahooXmlStockQuotesConverter();

		ReflectionTestUtils.setField(f, "QUERY", "query");
		ReflectionTestUtils.setField(f, "NAMESPACE", "yahoo");
		ReflectionTestUtils.setField(f, "CREATED_EL", "created");
		ReflectionTestUtils.setField(f, "RESULTS_EL", "results");
		ReflectionTestUtils.setField(f, "QUOTES_EL", "quotes");
		ReflectionTestUtils.setField(f, "QUOTES_LIST_EL", "quotesList");
		ReflectionTestUtils.setField(f, "O_QUOTE_EL", "quote");
		ReflectionTestUtils.setField(f, "N_QUOTE_EL", "quote");
		ReflectionTestUtils.setField(f, "SYMBOL_AT", "symbol");
		ReflectionTestUtils.setField(f, "PROVIDER_EL", "Provider");
		ReflectionTestUtils.setField(f, "DATA_TYPE_EL", "DataType");
		ReflectionTestUtils.setField(f, "EXCH_SYMB_EL", "ExchSymb");
		ReflectionTestUtils.setField(f, "DEFAULT_MARKET", "NY");
		ReflectionTestUtils.setField(f, "NO_DATA", "N/A");
		ReflectionTestUtils.setField(f, "STOCK_EXCHANGE_EL", "StockExchange");
		ReflectionTestUtils.setField(f, "TS_EL", "ts");
		xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	}

	@Test
	public void ResultToQuotesAndQuotesListtest() throws Exception {
		String output = xmlHeader + "<quotes><quotesList></quotesList></quotes>";
		InputStream in = this.getClass().getResourceAsStream("/resultsToQuotesAndQuotesListTest.xml");
		byte[] b = new byte[4096];
		in.read(b);
		Message m = new Message(new Document(b));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void StockQuoteToQuoteAndSymbolAndProviderAndDataTypeTest() throws Exception {
		String output = xmlHeader
				+ "<quotes><quotesList><quote><DataType>EOD</DataType><ExchSymb>NY</ExchSymb><ts>1402623779000</ts></quote></quotesList></quotes>";
		InputStream in = this.getClass().getResourceAsStream(
				"/StockQuoteToQuoteAndSymbolAndProviderAndDataTypeTest.xml");
		byte[] b = new byte[8192];
		in.read(b);
		Message m = new Message(new Document(b, DataType.EOD));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void DateToTimeStampTest() throws Exception {
		String output = xmlHeader
				+ "<quotes><quotesList><quote><ExchSymb>NY</ExchSymb><ts>1402623779000</ts></quote></quotesList></quotes>";
		InputStream in = this.getClass().getResourceAsStream("/StockDateToTimeStampTest.xml");
		byte[] b = new byte[8192];
		in.read(b);
		Message m = new Message(new Document(b));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void DateToTimeStampNATest() throws Exception {
		String output = xmlHeader + "<quotes><quotesList><x>0</x></quotesList></quotes>";
		InputStream in = this.getClass().getResourceAsStream("/StockDateToTimeStampNATest.xml");
		byte[] b = new byte[8192];
		in.read(b);
		Message m = new Message(new Document(b));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}
}