package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.msg.Message;

public class YahooXmlFXRatesConverterTest 
{
	YahooXmlFXRatesConverter f;
	String xmlHeader;
	
	@Before
	public void setUp() throws Exception
	{
		f = new YahooXmlFXRatesConverter();
		xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	}

	@Test
	public void ResultToRatesAndRatesListtest() throws Exception 
	{
	  String output = xmlHeader + "<rates><ratesList></ratesList></rates>";
	  InputStream in = this.getClass().getResourceAsStream("/resultsToRatesAndRatesListTest.xml");
	  byte [] b = new byte[4096];
	  in.read(b);
	  Document d = new Document();
	  d.setContent(b);
	  Message m = new Message();
	  m.setBody(d);
	  assertTrue(m != null);
	  f.convert(m);
	  assertEquals(new String(m.getBody().getContent()), output);
	}
	
	@Test
	public void RateToRateAndIDAndSymbolAndProviderAndDataTypeTest() throws Exception
	{
		String output = xmlHeader + "<rates><ratesList>" + "\n"
				+ "<rate><Symbol>4</Symbol><Provider></Provider><DataType></DataType></rate>" + "\n"
				+ "</ratesList></rates>";
		  InputStream in = this.getClass().getResourceAsStream("/RateToRateAndIDAndSymbolAndProviderAndDataType.xml");
		  byte [] b = new byte[8192];
		  in.read(b);
		  Document d = new Document();
		  d.setContent(b);
		  d.setDataType(DataType.EOD);
		  Message m = new Message();
		  m.setBody(d);
		  assertTrue(m != null);
		  f.convert(m);
		  System.out.println(new String(m.getBody().getContent()));
		  System.out.println(output);
		  assertEquals(new String(m.getBody().getContent()), output);	  
	}
	
	@Test
	public void DateToTimeStampTest() throws Exception
	{
		String output = xmlHeader + "<rates><ratesList>" + "\n"
				+ "<rate><Symbol>4</Symbol><Provider></Provider><DataType></DataType></rate>" + "\n"
				+ "23/12/2014" + "\n" + "1:24pm" + "\n"
				+ "</ratesList></rates>";
		  InputStream in = this.getClass().getResourceAsStream("/DateToTimeStampTest.xml");
		  byte [] b = new byte[8192];
		  in.read(b);
		  Document d = new Document();
		  d.setContent(b);
		  d.setDataType(DataType.EOD);
		  Message m = new Message();
		  m.setBody(d);
		  assertTrue(m != null);
		  f.convert(m);
		  assertEquals(new String(m.getBody().getContent()), output);	  
	}
	
	@Test
	public void DateToTimeStampNATest() throws Exception
	{
		String output = xmlHeader + "<rates><ratesList>" + "\n"
				+ "0" + "\n"
				+ "</ratesList></rates>";
		  InputStream in = this.getClass().getResourceAsStream("/DateToTimeStampNATest.xml");
		  byte [] b = new byte[8192];
		  in.read(b);
		  Document d = new Document();
		  d.setContent(b);
		  d.setDataType(DataType.EOD);
		  Message m = new Message();
		  m.setBody(d);
		  assertTrue(m != null);
		  f.convert(m);
		  System.out.println(new String(m.getBody().getContent()));
		  System.out.println(output);
		  assertEquals(new String(m.getBody().getContent()), output);	  
	}
}