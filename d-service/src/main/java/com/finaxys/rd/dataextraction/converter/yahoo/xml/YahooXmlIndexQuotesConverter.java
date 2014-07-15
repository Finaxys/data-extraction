/*
 * 
 */
package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.domain.IndexQuote;
import com.finaxys.rd.dataextraction.domain.IndexQuotes;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-genequoted Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlIndexQuotesConverter implements Converter {
	/** The date format. */
	@Value("${converter.yahoo.index_quotes.date_format}")
	private String DATE_FORMAT;

	@Value("${converter.yahoo.index_quotes.old.no_data}")
	private String NO_DATA;

	@Value("${converter.yahoo.index_quotes.old.quote_el}")
	private String QUOTE_EL;

	@Value("${converter.yahoo.index_quotes.old.symbol_el}")
	private String SYMBOL_EL;

	@Value("${converter.yahoo.index_quotes.old.lasttradepriceonly_el}")
	private String LASTTRADEPRICEONLY_EL;

	@Value("${converter.yahoo.index_quotes.old.lasttradedate_el}")
	private String LASTTRADEDATE_EL;

	@Value("${converter.yahoo.index_quotes.old.lasttradetime_el}")
	private String LASTTRADETIME_EL;

	@Value("${converter.yahoo.index_quotes.old.change_el}")
	private String CHANGE_EL;

	@Value("${converter.yahoo.index_quotes.old.open_el}")
	private String OPEN_EL;

	@Value("${converter.yahoo.index_quotes.old.dayshigh_el}")
	private String DAYSHIGH_EL;

	@Value("${converter.yahoo.index_quotes.old.dayslow_el}")
	private String DAYSLOW_EL;

	@Value("${converter.yahoo.index_quotes.old.volume_el}")
	private String VOLUME_EL;

	public void convert(Message message) throws Exception {
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		StreamSource source = new StreamSource(is);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		List<IndexQuote> list = new ArrayList<IndexQuote>();
		String date = null;
		String time = null;
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);

		IndexQuote indexQuote = null;
		while (reader.hasNext()) {
			try {
				XMLEvent ev = reader.nextEvent();
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(QUOTE_EL))
					indexQuote = new IndexQuote();

				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(SYMBOL_EL)) {

					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						indexQuote.setSymbol(evt.asCharacters().getData());
					else
						message.setSend(false);
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(LASTTRADEPRICEONLY_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						indexQuote.setLastTradePriceOnly(new BigDecimal(evt.asCharacters().getData()));
					else
						indexQuote.setLastTradePriceOnly(new BigDecimal(0));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(LASTTRADEDATE_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						date = evt.asCharacters().getData();
					else
						message.setSend(false);
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(LASTTRADETIME_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						time = evt.asCharacters().getData();
					else
						message.setSend(false);
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(CHANGE_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						indexQuote.setChange(new BigDecimal(evt.asCharacters().getData()));
					else
						indexQuote.setChange(new BigDecimal(0));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(OPEN_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						indexQuote.setOpen(new BigDecimal(evt.asCharacters().getData()));
					else
						indexQuote.setOpen(new BigDecimal(0));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DAYSHIGH_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						indexQuote.setDaysHigh(new BigDecimal(evt.asCharacters().getData()));
					else
						indexQuote.setDaysHigh(new BigDecimal(0));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DAYSLOW_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						indexQuote.setDaysLow(new BigDecimal(evt.asCharacters().getData()));
					else
						indexQuote.setDaysLow(new BigDecimal(0));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(VOLUME_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						indexQuote.setVolume(new BigInteger(evt.asCharacters().getData()));
					else
						indexQuote.setVolume(new BigInteger("0"));
				}
				if (ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(QUOTE_EL)) {
					if (message.isSend()) {
						if (date != null && time != null) {
							DateTime ts = formatter.parseDateTime(date + " " + time);
							indexQuote.setTs(ts);
						} else
							message.setSend(false);
						if (message.getBody().getProvider() != Character.UNASSIGNED)
							indexQuote.setProvider(message.getBody().getProvider());
						else
							message.setSend(false);
						if (message.getBody().getDataType() != null)
							indexQuote.setDataType(message.getBody().getDataType());
						else
							message.setSend(false);
						if (message.isSend()) {
							list.add(indexQuote);
							message.setSend(true);
							indexQuote = new IndexQuote();

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				message.setSend(false);
			}
		}
			IndexQuotes indexQuotes = new IndexQuotes();
			indexQuotes.setIndQuotesList(list);
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(IndexQuotes.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
				// true); Sinon erreur
				jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				jaxbMarshaller.marshal(indexQuotes, os);
				message.setBody(new Document(message.getBody().getContentType(), message.getBody().getDataType(),
						message.getBody().getDataClass(), message.getBody().getProvider(), os.toByteArray()));

			} catch (JAXBException e) {
				e.printStackTrace();
				message.setSend(false);
			}

		
	}

}
