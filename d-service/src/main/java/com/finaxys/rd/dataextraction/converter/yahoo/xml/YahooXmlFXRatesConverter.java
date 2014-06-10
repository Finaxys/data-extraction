/*
 * 
 */
package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class YahooXmlFXRatesConverter.
 */
public class YahooXmlFXRatesConverter implements Converter {

	/** The date format. */
	@Value("${converter.yahoo.fx_rates.date_format}")
	public String DATE_FORMAT;
	
	/** The results el. */
	@Value("${converter.yahoo.fx_rates.old.results_el}")
	public String RESULTS_EL;
	
	/** The rates el. */
	@Value("${converter.yahoo.fx_rates.new.rates_el}")
	public String RATES_EL;
	
	/** The rates list el. */
	@Value("${converter.yahoo.fx_rates.new.rates_list_el}")
	public String RATES_LIST_EL;
	
	/** The o rate el. */
	@Value("${converter.yahoo.fx_rates.old.rate_el}")
	public String O_RATE_EL;
	
	/** The n rate el. */
	@Value("${converter.yahoo.fx_rates.new.rate_el}")
	public String N_RATE_EL;
	
	/** The id att. */
	@Value("${converter.yahoo.fx_rates.id_att}")
	public String ID_ATT;
	
	/** The symbol el. */
	@Value("${converter.yahoo.fx_rates.new.symbol_el}")
	public String SYMBOL_EL;
	
	/** The provider el. */
	@Value("${converter.yahoo.fx_rates.new.provider_el}")
	public String PROVIDER_EL;
	
	/** The data type el. */
	@Value("${converter.yahoo.fx_rates.new.data_type_el}")
	public String DATA_TYPE_EL;
	
	/** The date el. */
	@Value("${converter.yahoo.fx_rates.old.date_el}")
	public String DATE_EL;
	
	/** The time el. */
	@Value("${converter.yahoo.fx_rates.old.time_el}")
	public String TIME_EL;
	
	/** The name el. */
	@Value("${converter.yahoo.fx_rates.old.name_el}")
	public String NAME_EL;
	
	/** The no data. */
	@Value("${converter.yahoo.fx_rates.old.no_data}")
	public String NO_DATA;
	
	/** The date reg. */
	@Value("${converter.yahoo.fx_rates.date_reg}")
	public String DATE_REG;
	
	/** The time reg. */
	@Value("${converter.yahoo.fx_rates.time_reg}")
	public String TIME_REG;
	
	/** The name reg. */
	@Value("${converter.yahoo.fx_rates.old.name_reg}")
	public String NAME_REG;
	
	/** The ts el. */
	@Value("${converter.yahoo.fx_rates.new.ts_el}")
	public String TS_EL;
	
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
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);

		while (reader.hasNext()) {
			XMLEvent ev = reader.nextEvent();

			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(RESULTS_EL)) {

				writer.add(eventFactory.createStartDocument());

				writer.add(eventFactory.createStartElement("", "", RATES_EL));
				writer.add(eventFactory.createStartElement("", "", RATES_LIST_EL));
				while (reader.hasNext()) {
					XMLEvent e = reader.nextEvent();
					if (e.isStartElement() && ((StartElement) e).getName().getLocalPart().equals(O_RATE_EL)) {

						writer.add(eventFactory.createStartElement("", "", N_RATE_EL));
						Attribute a = ((StartElement) e).getAttributeByName(new QName(ID_ATT));
						writer.add(eventFactory.createStartElement("", "", SYMBOL_EL));
						writer.add(eventFactory.createCharacters(a.getValue()));
						writer.add(eventFactory.createEndElement("", "", SYMBOL_EL));
						writer.add(eventFactory.createStartElement("", "", PROVIDER_EL));
						writer.add(eventFactory.createCharacters(message.getBody().getProvider() + ""));
						writer.add(eventFactory.createEndElement("", "", PROVIDER_EL));
						writer.add(eventFactory.createStartElement("", "", DATA_TYPE_EL));
						writer.add(eventFactory.createCharacters(message.getBody().getDataType().getName() + ""));
						writer.add(eventFactory.createEndElement("", "", DATA_TYPE_EL));

					} else if ((e.isStartElement() && (((StartElement) e).getName().getLocalPart().equals(DATE_EL)
							|| ((StartElement) e).getName().getLocalPart().equals(TIME_EL) || ((StartElement) e)
							.getName().getLocalPart().equals(NAME_EL)))
							|| (e.isEndElement() && (((EndElement) e).getName().getLocalPart().equals(DATE_EL)
									|| ((EndElement) e).getName().getLocalPart().equals(TIME_EL) || ((EndElement) e)
									.getName().getLocalPart().equals(NAME_EL))))
						continue;
					else if (e.isCharacters()) {
						if (e.asCharacters().getData().equals(NO_DATA))
							writer.add(eventFactory.createCharacters("0"));
						else if (e.asCharacters().getData().matches(DATE_REG))
							date = e.asCharacters().getData();
						else if (e.asCharacters().getData().matches(TIME_REG))
							time = e.asCharacters().getData();
						else if (e.asCharacters().getData().matches(NAME_REG))
							continue;
						else
							writer.add(e);
					} else if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals("rate")) {
						DateTime dt = formatter.parseDateTime(date + " " + time);
						ts = dt.getMillis();

						writer.add(eventFactory.createStartElement("", "", TS_EL));
						writer.add(eventFactory.createCharacters(ts + ""));
						writer.add(eventFactory.createEndElement("", "", TS_EL));
						writer.add(e);
					} else if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals("results")) {

						writer.add(eventFactory.createEndElement("", "", RATES_LIST_EL));
						writer.add(eventFactory.createEndElement("", "", RATES_EL));
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
