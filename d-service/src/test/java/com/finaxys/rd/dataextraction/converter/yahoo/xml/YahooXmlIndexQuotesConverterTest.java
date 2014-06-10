package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.msg.Message;

public class YahooXmlIndexQuotesConverterTest 
{
	YahooXmlIndexQuotesConverter f;
	String xmlHeader;
	@Before
	public void setUp() throws Exception 
	{
		f = new YahooXmlIndexQuotesConverter();
		xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	}

	@Test
	public void ResultToQuotesAndQuotesListtest() throws Exception 
	{
	  String output = xmlHeader + "<quotes><quotesList></quotesList></quotes>";
	  InputStream in = this.getClass().getResourceAsStream("/resultsToQuotesAndQuotesListTest.xml");
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
	public void QuoteToQuoteAndSymbolAndProviderAndDataTypeTest() throws Exception
	{
		String output = xmlHeader + "<quotes><quotesList>" + "\n"
				+ "<quote><Provider></Provider><DataType></DataType></quote>" + "\n"
				+ "</quotesList></quotes>";
		  InputStream in = this.getClass().getResourceAsStream("/QuoteToQuoteAndSymbolAndProviderAndDataTypeTest.xml");
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
	public void QuotesDateToTimeStampTest() throws Exception
	{
		String output = xmlHeader + "<quotes><quotesList>" + "\n"
				+ "<quote><Provider></Provider><DataType></DataType></rate>" + "\n"
				+ "23/12/2014" + "\n" + "1:24pm" + "\n"
				+ "</quotesList></quotes>";
		  InputStream in = this.getClass().getResourceAsStream("/QuotesDateToTimeStampTest.xml");
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
	public void QuotesDateToTimeStampNATest() throws Exception
	{
		String output = xmlHeader + "<quotes><quotesList>" + "\n"
				+ "0" + "\n"
				+ "</quotesList></quotes>";
		  InputStream in = this.getClass().getResourceAsStream("/QuotesDateToTimeStampNATest.xml");
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