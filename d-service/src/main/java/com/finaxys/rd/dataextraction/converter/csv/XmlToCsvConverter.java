package com.finaxys.rd.dataextraction.converter.csv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class XmlToCsvConverter implements Converter {

	private Converter xmlConverter;

	private String seperator;

	private String splitElement;

	public XmlToCsvConverter() {
		super();
	}

	public XmlToCsvConverter(Converter xmlConverter, String seperator, String splitElement) {
		super();
		this.xmlConverter = xmlConverter;
		this.seperator = seperator;
		this.splitElement = splitElement;
	}

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public String getSplitElement() {
		return splitElement;
	}

	public void setSplitElement(String splitElement) {
		this.splitElement = splitElement;
	}

	public Converter getXmlConverter() {
		return xmlConverter;
	}

	public void setXmlConverter(Converter xmlConverter) {
		this.xmlConverter = xmlConverter;
	}

	public void convertXmlToCsv(Message message) throws Exception {

		this.convertXmlToCsv(message, seperator, splitElement);
	}

	public void convertXmlToCsv(Message message, String seperator, String element) throws Exception {

		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		StreamSource source = new StreamSource(is);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(os);

		while (reader.hasNext()) {
			XMLEvent ev = reader.nextEvent();
			boolean start = true;
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(element)) {

				while (reader.hasNext()) {
					XMLEvent e = reader.nextEvent();
					if (e.isEndElement() && ((EndElement) e).getName().getLocalPart().equals(element)) {
						writer.println("");
						break;
					}

					else if (e.isStartElement()) {
						if (start)
							start = false;
						else
							writer.print(seperator);

						writer.print("\"");

					} else if (e.isEndElement())
						writer.print("\"");

					else if (e.isCharacters())

						writer.print(e.asCharacters().getData());

				}
			}
		}
		writer.flush();
		message.getBody().setContent(os.toByteArray());
		// System.out.println(new String(os.toByteArray()));

	}

	@Override
	public void convert(Message message) throws Exception {
		xmlConverter.convert(message);
		this.convertXmlToCsv(message);

	}
}
