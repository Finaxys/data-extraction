package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class YahooXmlIndexQuotesConverterTest {
	YahooXmlIndexQuotesConverter f;
	String xmlHeader;

	@Before
	public void setUp() throws Exception {
		f = new YahooXmlIndexQuotesConverter();
		ReflectionTestUtils.setField(f, "DATE_FORMAT", "MM/dd/yyyy h:mmaa");
		ReflectionTestUtils.setField(f, "RESULTS_EL", "results");
		ReflectionTestUtils.setField(f, "QUOTES_EL", "quotes");
		ReflectionTestUtils.setField(f, "QUOTES_LIST_EL", "quotesList");
		ReflectionTestUtils.setField(f, "O_QUOTE_EL", "quote");
		ReflectionTestUtils.setField(f, "N_QUOTE_EL", "quote");
		ReflectionTestUtils.setField(f, "PROVIDER_EL", "Provider");
		ReflectionTestUtils.setField(f, "DATA_TYPE_EL", "DataType");
		ReflectionTestUtils.setField(f, "DATE_EL", "LastTradeDate");
		ReflectionTestUtils.setField(f, "TIME_EL", "LastTradeTime");
		ReflectionTestUtils.setField(f, "NO_DATA", "N/A");
		ReflectionTestUtils.setField(f, "DATE_REG", "(\\d{1,2})/(\\d{1,2})/(\\d{4})");
		ReflectionTestUtils.setField(f, "TIME_REG", "(\\d{1,2}):(\\d{2})(am|pm)");
		ReflectionTestUtils.setField(f, "NAME_REG", "(\\w{3}) to (\\w{3})");
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
	public void QuoteToQuoteAndSymbolAndProviderAndDataTypeTest() throws Exception {
		String output = xmlHeader + "<quotes><quotesList>" + "\n"
				+ "<quote><DataType>EOD</DataType></quote>" + "\n" + "</quotesList></quotes>";
		InputStream in = this.getClass().getResourceAsStream("/QuoteToQuoteAndSymbolAndProviderAndDataTypeTest.xml");
		byte[] b = new byte[8192];
		in.read(b);
		Message m = new Message(new Document(b, DataType.EOD));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void QuotesDateToTimeStampTest() throws Exception {
		String output = xmlHeader + "<quotes><quotesList><quote><ts>1393331040000</ts></quote></quotesList></quotes>";
		InputStream in = this.getClass().getResourceAsStream("/QuotesDateToTimeStampTest.xml");
		byte[] b = new byte[8192];
		in.read(b);
		Message m = new Message(new Document(b));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void QuotesDateToTimeStampNATest() throws Exception {
		String output = xmlHeader + "<quotes><quotesList><x>0</x></quotesList></quotes>";
		InputStream in = this.getClass().getResourceAsStream("/QuotesDateToTimeStampNATest.xml");
		byte[] b = new byte[8192];
		in.read(b);
		Message m = new Message(new Document(b));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

}