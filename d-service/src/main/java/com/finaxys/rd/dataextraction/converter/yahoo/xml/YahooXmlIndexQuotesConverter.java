/*
 * 
 */
package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlIndexQuotesConverter implements Converter {

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.converter.Converter#convert(com.finaxys.rd.dataextraction.msg.Message)
	 */
	public void convert(Message message) throws Exception {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		XMLOutputFactory ofactory = XMLOutputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		StreamSource source = new StreamSource(is);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(os);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		XMLEventWriter writer = ofactory.createXMLEventWriter(result);
		boolean end = false;

		Long ts = null;
		String date = null;
		String time = null;
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy h:mmaa");

		while (reader.hasNext()) {
			XMLEvent ev = reader.nextEvent();

			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals("results")) {

				writer.add(eventFactory.createStartDocument());

				writer.add(eventFactory.createStartElement("", "", "quotes"));
				writer.add(eventFactory.createStartElement("", "", "quotesList"));
				while (reader.hasNext()) {
					XMLEvent e = reader.nextEvent();
					if (e.isStartElement() && ((StartElement) e).getName().getLocalPart().equals("quote")) {

						writer.add(eventFactory.createStartElement("", "", "quote"));
						writer.add(eventFactory.createStartElement("", "", "Provider"));
						writer.add(eventFactory.createCharacters(message.getBody().getProvider() + ""));
						writer.add(eventFactory.createEndElement("", "", "Provider"));
						writer.add(eventFactory.createStartElement("", "", "DataType"));
						writer.add(eventFactory.createCharacters(message.getBody().getDataType().getName() + ""));
						writer.add(eventFactory.createEndElement("", "", "DataType"));

					} else if ((e.isStartElement() && (((StartElement) e).getName().getLocalPart()
							.equals("LastTradeDate") || ((StartElement) e).getName().getLocalPart()
							.equals("LastTradeTime")))
							|| (e.isEndElement() && (((EndElement) e).getName().getLocalPart().equals("LastTradeDate") || ((EndElement) e)
									.getName().getLocalPart().equals("LastTradeTime"))))
						continue;
					else if (e.isCharacters()) {

						if (e.asCharacters().getData().equals("N/A"))
							writer.add(eventFactory.createCharacters("0"));
						else if (e.asCharacters().getData().matches("(\\d{1,2})/(\\d{1,2})/(\\d{4})"))
							date = e.asCharacters().getData();
						else if (e.asCharacters().getData().matches("(\\d{1,2}):(\\d{2})(am|pm)"))
							time = e.asCharacters().getData();
						else if (e.asCharacters().getData().matches("(\\w{3}) to (\\w{3})"))
							continue;
						else
							writer.add(e);
					} else if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals("rate")) {
						DateTime dt = formatter.parseDateTime(date + " " + time);
						ts = dt.getMillis();

						writer.add(eventFactory.createStartElement("", "", "ts"));
						writer.add(eventFactory.createCharacters(ts + ""));
						writer.add(eventFactory.createEndElement("", "", "ts"));
						writer.add(e);
					} else if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals("results")) {

						writer.add(eventFactory.createEndElement("", "", "quotesList"));
						writer.add(eventFactory.createEndElement("", "", "quotes"));
						end = true;
						break;
					} else
						writer.add(e);
				}
			}
			if (end)
				break;
		}
		writer.flush();
		reader.close();
		writer.close();
		message.getBody().setContent(os.toByteArray());
	}
}
