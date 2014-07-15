package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.finaxys.rd.dataextraction.converter.csv.XmlToCsvConverter;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class XmlToCsvConverterTest {

	XmlToCsvConverter c;

	@Before
	public void setUp() {
		c = new XmlToCsvConverter(null, ";", "rate");
	}

	@Test
	public void test1() throws Exception {
		String input = "<rates><ratesList><rate><a>test1</a><b>test2</b></rate></ratesList></rates>";
		String output = "\"test1\";\"test2\"";
		Message m = new Message(new Document(input.getBytes()));
		c.convertXmlToCsv(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void test2() throws Exception {
		String input = "<rates><ratesList><rate><a>test1</a><b></b></rate></ratesList></rates>";
		String output = "\"test1\";\"\"";
		Message m = new Message(new Document(input.getBytes()));
		c.convertXmlToCsv(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void test3() throws Exception {
		String input = "<rates><ratesList><rate><a>test1</a><b/></rate></ratesList></rates>";
		String output = "\"test1\";\"\"";
		Message m = new Message(new Document(input.getBytes()));
		c.convertXmlToCsv(m);
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}

	@Test
	public void test4() throws Exception {
		String input = "<rates><ratesList><rate><a>test1</a><b/><c>test2</c></rate></ratesList></rates>";
		String output = "\"test1\";\"\";\"test2\"";
		Message m = new Message(new Document(input.getBytes()));
		c.convertXmlToCsv(m, ";", "rate");
		assertEquals(output.replaceAll("[\\n]+\\s*", ""),
				new String(m.getBody().getContent()).replaceAll("[\\n]+\\s*", ""));
	}
}