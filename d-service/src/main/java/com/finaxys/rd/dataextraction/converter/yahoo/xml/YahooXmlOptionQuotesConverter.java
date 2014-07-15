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
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.domain.OptionQuote;
import com.finaxys.rd.dataextraction.domain.OptionsQuotes;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-genequoted Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlOptionQuotesConverter implements Converter {
	/** The date format. */
	@Value("${converter.yahoo.option_quotes.date_format}")
	private String DATE_FORMAT;

	@Value("${converter.yahoo.option_quotes.old.no_data}")
	private String NO_DATA;

	@Value("${converter.yahoo.option_quotes.old.option_el}")
	private String OPTION_EL;

	@Value("${converter.yahoo.option_quotes.old.symbol_att}")
	private String SYMBOL_ATT;

	@Value("${converter.yahoo.option_quotes.old.type_att}")
	private String TYPE_ATT;

	@Value("${converter.yahoo.option_quotes.old.change_el}")
	private String CHANGE_EL;

	@Value("${converter.yahoo.option_quotes.old.price_el}")
	private String PRICE_EL;

	@Value("${converter.yahoo.option_quotes.old.prevclose_el}")
	private String PREVCLOSE_EL;

	@Value("${converter.yahoo.option_quotes.old.open_el}")
	private String OPEN_EL;

	@Value("${converter.yahoo.option_quotes.old.bid_el}")
	private String BID_EL;

	@Value("${converter.yahoo.option_quotes.old.ask_el}")
	private String ASK_EL;

	@Value("${converter.yahoo.option_quotes.old.strike_el}")
	private String STRIKE_EL;

	@Value("${converter.yahoo.option_quotes.old.volume_el}")
	private String VOLUME_EL;

	@Value("${converter.yahoo.option_quotes.old.openinterest_el}")
	private String OPENINTEREST_EL;

	public void convert(Message message) throws Exception {
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		StreamSource source = new StreamSource(is);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		List<OptionQuote> list = new ArrayList<OptionQuote>();
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
		String date = "6/21/2014";
		String time = "7:23am";
		OptionQuote optionQuote = null;
		while (reader.hasNext()) {
			try {
				XMLEvent ev = reader.nextEvent();

				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(OPTION_EL)) {
					optionQuote = new OptionQuote();
					Attribute a = ((StartElement) ev).getAttributeByName(new QName(SYMBOL_ATT));
					if (a != null && !a.getValue().equals(""))
						optionQuote.setSymbol(a.getValue());
					else
						message.setSend(false);

					a = ((StartElement) ev).getAttributeByName(new QName(TYPE_ATT));
					if (a != null && !a.getValue().equals(""))
						optionQuote.setType(a.getValue());
					else
						message.setSend(false);
				}

	
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(CHANGE_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						optionQuote.setChange(new BigDecimal(evt.asCharacters().getData()));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(PRICE_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						optionQuote.setPrice(new BigDecimal(evt.asCharacters().getData()));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(PREVCLOSE_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						optionQuote.setPrevClose(new BigDecimal(evt.asCharacters().getData()));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(OPEN_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						optionQuote.setOpen(new BigDecimal(evt.asCharacters().getData()));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(BID_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						optionQuote.setBid(new BigDecimal(evt.asCharacters().getData()));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(ASK_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						optionQuote.setAsk(new BigDecimal(evt.asCharacters().getData()));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(STRIKE_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						optionQuote.setStrike(new BigDecimal(evt.asCharacters().getData()));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(VOLUME_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						optionQuote.setVolume(new BigInteger(evt.asCharacters().getData()));
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(OPENINTEREST_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						optionQuote.setOpenInterest(new Integer(evt.asCharacters().getData()));
				}

			
				if (ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(OPTION_EL)) {
					if (message.isSend()) {
						if (date != null && time != null) {
							DateTime ts = formatter.parseDateTime(date + " " + time);
							optionQuote.setTs(ts);
							
						} else
							message.setSend(false);
						if (message.getBody().getProvider() != Character.UNASSIGNED)
							optionQuote.setProvider(message.getBody().getProvider());
						else
							message.setSend(false);
						if (message.getBody().getDataType() != null)
							optionQuote.setDataType(message.getBody().getDataType());
						else
							message.setSend(false);

						if (message.isSend()) {
							//set expiration date from symbol 

							list.add(optionQuote);
							message.setSend(true);
							optionQuote = new OptionQuote();

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				message.setSend(false);
			}
		}
		OptionsQuotes optionsQuotes = new OptionsQuotes();
		optionsQuotes.setQuotesList(list);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(OptionsQuotes.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
			// true); Sinon erreur
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			jaxbMarshaller.marshal(optionsQuotes, os);
			message.setBody(new Document(message.getBody().getContentType(), message.getBody().getDataType(), message.getBody().getDataClass(), message.getBody().getProvider(), os
					.toByteArray()));

		} catch (JAXBException e) {
			e.printStackTrace();
			message.setSend(false);
		}

	}
}
