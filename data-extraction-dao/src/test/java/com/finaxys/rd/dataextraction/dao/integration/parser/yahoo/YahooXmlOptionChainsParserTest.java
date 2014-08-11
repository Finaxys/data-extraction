package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import static org.junit.Assert.assertEquals;

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
import com.finaxys.rd.dataextraction.domain.OptionChain;

public class YahooXmlOptionChainsParserTest {

	YahooXmlOptionChainsParser target;
	String dateFormat;

	@Before
	public void setUp() throws Exception {
		target = new YahooXmlOptionChainsParser();
		dateFormat = "yyyy-MM";
		ReflectionTestUtils.setField(target, "DATE_FORMAT", dateFormat);
		ReflectionTestUtils.setField(target, "MAIN_OPTION_EL", "option");
		ReflectionTestUtils.setField(target, "SYMBOL_ATT", "symbol");
		ReflectionTestUtils.setField(target, "CONTRACT_EL", "contract");
		ReflectionTestUtils.setField(target, "NO_DATA", "N/A");
	}

	@Test
	public void test_convert() throws Exception {

		// Setup
		DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
		OptionChain optionChain = new OptionChain('0', new DateTime(), "YHOO",
				DataType.REF, '0', formatter.parseDateTime("2014-07")
						.toLocalDate());

		List<OptionChain> outList = new ArrayList<OptionChain>();
		outList.add(optionChain);
		commonTest(outList, "/YahooXmlOptionChainsParser/test_convert.xml");
	}

	@Test
	public void test_convert_many_contracts() throws Exception {

		// Setup
		DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
		OptionChain optionChain1 = new OptionChain('0', new DateTime(), "YHOO",
				DataType.REF, '0', formatter.parseDateTime("2014-07")
						.toLocalDate());
		OptionChain optionChain2 = new OptionChain('0', new DateTime(), "AAPL",
				DataType.REF, '0', formatter.parseDateTime("2014-07")
						.toLocalDate());
		OptionChain optionChain3 = new OptionChain('0', new DateTime(), "AAPL",
				DataType.REF, '0', formatter.parseDateTime("2014-08")
						.toLocalDate());

		List<OptionChain> outList = new ArrayList<OptionChain>();
		outList.add(optionChain1);
		outList.add(optionChain2);
		outList.add(optionChain3);
		commonTest(outList,
				"/YahooXmlOptionChainsParser/test_convert_many_contracts.xml");
	}

	@Test
	public void test_convert_no_symbol() throws Exception {
		commonTest("/YahooXmlOptionChainsParser/test_convert_no_symbol.xml");
	}

	@Test
	public void test_convert_no_contract() throws Exception {
		commonTest("/YahooXmlOptionChainsParser/test_convert_no_contract.xml");
	}

	@Test
	public void test_convert_without_option_element() throws Exception {
		commonTest("/YahooXmlOptionChainsParser/test_convert_without_option_element.xml");
	}

	@Test
	public void test_convert_fields_not_wellformedxml() throws Exception {
		commonTest("/YahooXmlOptionChainsParser/test_convert_not_wellformedxml.xml");
	}

	private void commonTest(List<OptionChain> list, String testFile)
			throws Exception {
		// Setup
		DataWrapper<OptionChain> out = null;
		if (list != null) {
			out = new DataWrapper<OptionChain>(list);
		} else {
			out = new DataWrapper<OptionChain>();
		}

		byte[] inData = TestHelper.getResourceAsBytes(testFile);
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.REF);

		// Execution
		List<OptionChain> inList = target.parse(inMessageFixture);
		DataWrapper<OptionChain> in = new DataWrapper<OptionChain>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	private void commonTest(String testFile) throws Exception {
		commonTest(null, testFile);
	}
}