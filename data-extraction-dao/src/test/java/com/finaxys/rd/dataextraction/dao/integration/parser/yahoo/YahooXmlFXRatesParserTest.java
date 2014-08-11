package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.finaxys.rd.dataextraction.domain.DataWrapper;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.FXRate;

public class YahooXmlFXRatesParserTest {

	YahooXmlFXRatesParser target;
	String dateFormat;

	@Before
	public void setUp() throws Exception {
		target = new YahooXmlFXRatesParser();
		dateFormat = "MM/dd/yyyy h:mmaa";
		ReflectionTestUtils.setField(target, "DATE_FORMAT", dateFormat);
		ReflectionTestUtils.setField(target, "MAIN_RATE_EL", "rate");
		ReflectionTestUtils.setField(target, "RATE_EL", "Rate");
		ReflectionTestUtils.setField(target, "ID_ATT", "id");
		ReflectionTestUtils.setField(target, "DATE_EL", "Date");
		ReflectionTestUtils.setField(target, "TIME_EL", "Time");
		ReflectionTestUtils.setField(target, "NO_DATA", "N/A");
		ReflectionTestUtils.setField(target, "ASK_EL", "Ask");
		ReflectionTestUtils.setField(target, "BID_EL", "Bid");
	}

	@Test
	public void test_convert() throws Exception {

		// Setup
		DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
		FXRate fxRate = new FXRate('0', new DateTime(), "EURUSD",
				DataType.INTRA, formatter.parseDateTime("7/23/2014 10:35pm"),
				new BigDecimal("1.3463"), new BigDecimal("1.3463"),
				new BigDecimal("1.3462"));
		List<FXRate> outList = new ArrayList<FXRate>();
		outList.add(fxRate);
		DataWrapper<FXRate> out = new DataWrapper<FXRate>(outList);

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	@Test
	public void test_convert_no_id() throws Exception {

		// Setup
		DataWrapper<FXRate> out = new DataWrapper<FXRate>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert_no_id.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	@Test
	public void test_convert_no_ts() throws Exception {

		// Setup
		DataWrapper<FXRate> out = new DataWrapper<FXRate>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert_no_ts.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	@Test
	public void test_convert_no_provider() throws Exception {

		// Setup
		DataWrapper<FXRate> out = new DataWrapper<FXRate>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert_no_provider.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	@Test
	public void test_convert_no_datatype() throws Exception {

		// Setup
		DataWrapper<FXRate> out = new DataWrapper<FXRate>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert_no_datatype.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	@Test
	public void test_convert_without_rate_element() throws Exception {

		// Setup
		DataWrapper<FXRate> out = new DataWrapper<FXRate>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert_without_rate_element.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	@Test
	public void test_convert_fields_not_wellformedxml() throws Exception {

		// Setup
		DataWrapper<FXRate> out = new DataWrapper<FXRate>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert_not_wellformedxml.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

}