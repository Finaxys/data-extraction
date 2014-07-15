package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.finaxys.rd.dataextraction.converter.yahoo.xml.YahooXmlFXRatesConverter;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;

public class YahooXmlFXRatesConverterTest {
	YahooXmlFXRatesConverter f;
	String xmlHeader;

	@Before
	public void setUp() throws Exception {
		f = new YahooXmlFXRatesConverter();
		ReflectionTestUtils.setField(f, "DATE_FORMAT", "MM/dd/yyyy h:mmaa");
		ReflectionTestUtils.setField(f, "RESULTS_EL", "results");
		ReflectionTestUtils.setField(f, "RATES_EL", "rates");
		ReflectionTestUtils.setField(f, "RATES_LIST_EL", "ratesList");
		ReflectionTestUtils.setField(f, "O_RATE_EL", "rate");
		ReflectionTestUtils.setField(f, "N_RATE_EL", "rate");
		ReflectionTestUtils.setField(f, "ID_ATT", "id");
		ReflectionTestUtils.setField(f, "SYMBOL_EL", "Symbol");
		ReflectionTestUtils.setField(f, "PROVIDER_EL", "Provider");
		ReflectionTestUtils.setField(f, "DATA_TYPE_EL", "DataType");
		ReflectionTestUtils.setField(f, "DATE_EL", "Date");
		ReflectionTestUtils.setField(f, "TIME_EL", "Time");
		ReflectionTestUtils.setField(f, "NAME_EL", "Name");
		ReflectionTestUtils.setField(f, "NO_DATA", "N/A");
		ReflectionTestUtils.setField(f, "DATE_REG", "(\\d{1,2})/(\\d{1,2})/(\\d{4})");
		ReflectionTestUtils.setField(f, "TIME_REG", "(\\d{1,2}):(\\d{2})(am|pm)");
		ReflectionTestUtils.setField(f, "NAME_REG", "(\\w{3}) to (\\w{3})");
		ReflectionTestUtils.setField(f, "TS_EL", "ts");
		xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	}

	@Test
	public void ResultToRatesAndRatesListtest() throws Exception {
		String output = xmlHeader + "<rates><ratesList></ratesList></rates>";
		InputStream in = this.getClass().getResourceAsStream("/resultsToRatesAndRatesListTest.xml");
		byte[] b = new byte[4096];
		in.read(b);
		Message m = new Message(new Document(b));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void RateToRateAndIDAndSymbolAndProviderAndDataTypeTest() throws Exception {
		// String output = xmlHeader + "<rates><ratesList>" + "\n"
		// + "<rate><Symbol>4</Symbol><DataType>EOD</DataType></rate>" + "\n"
		// + "</ratesList></rates>";
		String output = xmlHeader + "<rates><ratesList>" + "\n"
				+ "<rate><Symbol>4</Symbol><Provider></Provider><DataType>EOD</DataType></rate>" + "\n"
				+ "</ratesList></rates>";
		InputStream in = this.getClass().getResourceAsStream("/RateToRateAndIDAndSymbolAndProviderAndDataType.xml");
		byte[] b = new byte[8192];
		in.read(b);
		Message m = new Message(new Document(b, DataType.EOD));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void DateToTimeStampTest() throws Exception {
		// String output = xmlHeader + "<rates><ratesList>"
		// + "<rate><Symbol>4</Symbol><ts>1408706640000</ts></rate>"
		// + "</ratesList></rates>";
		String output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rates><ratesList><rate><Symbol>4</Symbol><Provider></Provider><DataType></DataType><ts>2014-08-22T13:24:00.000+02:00</ts></rate></ratesList></rates>";
		InputStream in = this.getClass().getResourceAsStream("/DateToTimeStampTest.xml");
		byte[] b = new byte[8192];
		in.read(b);
		Message m = new Message(new Document(b));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void DateToTimeStampNATest() throws Exception {
		String output = xmlHeader + "<rates><ratesList><x>0</x></ratesList></rates>";
		InputStream in = this.getClass().getResourceAsStream("/DateToTimeStampNATest.xml");
		byte[] b = new byte[8192];
		in.read(b);
		Message m = new Message(new Document(b));
		f.convert(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}
}