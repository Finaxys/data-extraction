package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.splitter.XmlSplitter;

public class XmlSplitterTest {

	XmlSplitter splitter;

	@Before
	public void setUp() {
		splitter = new XmlSplitter("rate");
	}

	@Test
	public void test1() throws Exception {
		String input = "<rates><ratesList><rate>1</rate><rate>2</rate></ratesList></rates>";
		// String output = "\"test1\";\"test2\"";
		Message m = new Message(new Document(input.getBytes()));
		List<Message> l = splitter.split(m);
		assertEquals(2, l.size());
		assertEquals("<rate>1</rate>", new String(l.get(0).getBody().getContent()));
		assertEquals("<rate>2</rate>", new String(l.get(1).getBody().getContent()));
	}

}