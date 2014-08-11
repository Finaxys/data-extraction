/*
 * 
 */
package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.IndexQuote;

// TODO: Auto-genequoted Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlIndexQuotesParser implements Parser<IndexQuote> {
	/** The date format. */
	@Value("${parser.yahoo.index_quotes.date_format}")
	private String DATE_FORMAT;

	@Value("${parser.yahoo.index_quotes.old.no_data}")
	private String NO_DATA;

	@Value("${parser.yahoo.index_quotes.old.quote_el}")
	private String QUOTE_EL;

	@Value("${parser.yahoo.index_quotes.old.symbol_att}")
	private String SYMBOL_ATT;

	@Value("${parser.yahoo.index_quotes.old.lasttradepriceonly_el}")
	private String LASTTRADEPRICEONLY_EL;

	@Value("${parser.yahoo.index_quotes.old.lasttradedate_el}")
	private String LASTTRADEDATE_EL;

	@Value("${parser.yahoo.index_quotes.old.lasttradetime_el}")
	private String LASTTRADETIME_EL;

	@Value("${parser.yahoo.index_quotes.old.change_el}")
	private String CHANGE_EL;

	@Value("${parser.yahoo.index_quotes.old.open_el}")
	private String OPEN_EL;

	@Value("${parser.yahoo.index_quotes.old.dayshigh_el}")
	private String DAYSHIGH_EL;

	@Value("${parser.yahoo.index_quotes.old.dayslow_el}")
	private String DAYSLOW_EL;

	@Value("${parser.yahoo.index_quotes.old.volume_el}")
	private String VOLUME_EL;

	public List<IndexQuote> parse(Document document) throws Exception {
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(document.getContent());
		StreamSource source = new StreamSource(is);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		
		List<IndexQuote> list = new ArrayList<IndexQuote>();
		String date = null;
		String time = null;
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);

		IndexQuote indexQuote = null;
		boolean isValid = true;
		while (reader.hasNext()) {
			try {
				XMLEvent ev = reader.nextEvent();
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(QUOTE_EL)) {
					indexQuote = new IndexQuote();
					isValid = true;
					Attribute a = ((StartElement) ev).getAttributeByName(new QName(SYMBOL_ATT));
					if (a != null && !a.getValue().equals(""))
						{
						String[] sp = a.getValue().split("\\.");
						indexQuote.setSymbol(sp[0]);
						if(sp.length == 2)
							indexQuote.setExchSymb(sp[1]);
						else indexQuote.setExchSymb("US");
						}
					else
						isValid = false;
				} else if (indexQuote != null && isValid) {

					if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(LASTTRADEPRICEONLY_EL)) {
						XMLEvent evt = reader.nextEvent();
						if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
							indexQuote.setLastTradePriceOnly(new BigDecimal(evt.asCharacters().getData()));

					} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(LASTTRADEDATE_EL)) {
						XMLEvent evt = reader.nextEvent();
						if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
							date = evt.asCharacters().getData();
						else
							isValid = false;
					} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(LASTTRADETIME_EL)) {
						XMLEvent evt = reader.nextEvent();
						if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
							time = evt.asCharacters().getData();
						else
							isValid = false;
					} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(CHANGE_EL)) {
						XMLEvent evt = reader.nextEvent();
						if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
							indexQuote.setChange(new BigDecimal(evt.asCharacters().getData()));

					} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(OPEN_EL)) {
						XMLEvent evt = reader.nextEvent();
						if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
							indexQuote.setOpen(new BigDecimal(evt.asCharacters().getData()));

					} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DAYSHIGH_EL)) {
						XMLEvent evt = reader.nextEvent();
						if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
							indexQuote.setDaysHigh(new BigDecimal(evt.asCharacters().getData()));

					} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DAYSLOW_EL)) {
						XMLEvent evt = reader.nextEvent();
						if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
							indexQuote.setDaysLow(new BigDecimal(evt.asCharacters().getData()));

					} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(VOLUME_EL)) {
						XMLEvent evt = reader.nextEvent();
						if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
							indexQuote.setVolume(new BigInteger(evt.asCharacters().getData()));

					} else if (isValid && ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(QUOTE_EL)) {

						if (date != null && time != null && document.getSource() != Character.UNASSIGNED && document.getDataType() != null) {
							DateTime ts = formatter.parseDateTime(date + " " + time);
							indexQuote.setQuoteDateTime(ts);
							indexQuote.setSource(document.getSource());
							indexQuote.setDataType(document.getDataType());
							indexQuote.setInputDate(new DateTime());
							list.add(indexQuote);
							indexQuote = null;
						}
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				isValid = false;
				break;
			} catch (XMLStreamException e) {
				e.printStackTrace();
				isValid = false;
				break;

			} catch (Exception e) {
				e.printStackTrace();
				isValid = false;
			}
		}
		return list;
	}

}
