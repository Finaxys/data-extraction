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
import com.finaxys.rd.dataextraction.domain.StockQuote;
import com.finaxys.rd.dataextraction.domain.StocksQuotes;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-genequoted Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlStockQuotesConverter implements Converter {
	/** The date format. */
	@Value("${converter.yahoo.stock_quotes.date_format}")
	private String DATE_FORMAT;

	@Value("${converter.yahoo.stock_quotes.old.no_data}")
	private String NO_DATA;

	@Value("${converter.yahoo.stock_quotes.old.quote_el}")
	private String QUOTE_EL;

	@Value("${converter.yahoo.stock_quotes.old.averagedailyvolume_el}")
	private String AVERAGEDAILYVOLUME_EL;

	@Value("${converter.yahoo.stock_quotes.old.change_el}")
	private String CHANGE_EL;

	@Value("${converter.yahoo.stock_quotes.old.dayslow_el}")
	private String DAYSLOW_EL;

	@Value("${converter.yahoo.stock_quotes.old.dayshigh_el}")
	private String DAYSHIGH_EL;

	@Value("${converter.yahoo.stock_quotes.old.yearlow_el}")
	private String YEARLOW_EL;

	@Value("${converter.yahoo.stock_quotes.old.yearhigh_el}")
	private String YEARHIGH_EL;

	@Value("${converter.yahoo.stock_quotes.old.marketcapitalization_el}")
	private String MARKETCAPITALIZATION_EL;

	@Value("${converter.yahoo.stock_quotes.old.lasttradepriceonly_el}")
	private String LASTTRADEPRICEONLY_EL;

	@Value("${converter.yahoo.stock_quotes.old.daysrange_el}")
	private String DAYSRANGE_EL;

	@Value("${converter.yahoo.stock_quotes.old.name_el}")
	private String NAME_EL;

	@Value("${converter.yahoo.stock_quotes.old.symbol_el}")
	private String SYMBOL_EL;

	@Value("${converter.yahoo.stock_quotes.old.volume_el}")
	private String VOLUME_EL;

	public void convert(Message message) throws Exception {
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		StreamSource source = new StreamSource(is);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		List<StockQuote> list = new ArrayList<StockQuote>();
		String date = "6/21/2014";
		String time = "7:23am";
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
		StockQuote stockQuote = null;
		while (reader.hasNext()) {
			try {
			XMLEvent ev = reader.nextEvent();

			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(QUOTE_EL))
				stockQuote = new StockQuote();

			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(AVERAGEDAILYVOLUME_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setAverageDailyVolume(new BigInteger(evt.asCharacters().getData()));

			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(CHANGE_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setChange(new BigDecimal(evt.asCharacters().getData()));
			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DAYSLOW_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setDaysLow(new BigDecimal(evt.asCharacters().getData()));
			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DAYSHIGH_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setDaysHigh(new BigDecimal(evt.asCharacters().getData()));
			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(YEARLOW_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setYearLow(new BigDecimal(evt.asCharacters().getData()));
			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(YEARHIGH_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setYearHigh(new BigDecimal(evt.asCharacters().getData()));
			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(MARKETCAPITALIZATION_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setMarketCapitalization(evt.asCharacters().getData());
			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(LASTTRADEPRICEONLY_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setLastTradePriceOnly(new BigDecimal(evt.asCharacters().getData()));
			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DAYSRANGE_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setDaysRange(evt.asCharacters().getData());
			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(NAME_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setName(evt.asCharacters().getData());
			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(SYMBOL_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setSymbol(evt.asCharacters().getData());
				else
					message.setSend(false);
			}
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(VOLUME_EL)) {
				XMLEvent evt = reader.nextEvent();
				if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
					stockQuote.setVolume(new BigInteger(evt.asCharacters().getData()));
			}
			if (ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(QUOTE_EL)) {
				if (message.isSend()) {
					if (date != null && time != null) {
						DateTime ts = formatter.parseDateTime(date + " " + time);
						stockQuote.setTs(ts);
						;
					} else
						message.setSend(false);
					if (message.getBody().getProvider() != Character.UNASSIGNED)
						stockQuote.setProvider(message.getBody().getProvider());
					else
						message.setSend(false);
					if (message.getBody().getDataType() != null)
						stockQuote.setDataType(message.getBody().getDataType());
					else
						message.setSend(false);

					if (message.isSend()) {
						String exchSymb = "NY";
						if (stockQuote.getSymbol().indexOf('.') != -1)
							exchSymb = stockQuote.getSymbol().substring(stockQuote.getSymbol().indexOf('.') + 1);
						stockQuote.setExchSymb(exchSymb);

						list.add(stockQuote);
						message.setSend(true);
						stockQuote = new StockQuote();

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.setSend(false);
		}
	}
			StocksQuotes stocksQuotes = new StocksQuotes();
			stocksQuotes.setQuotesList(list);
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(StocksQuotes.class);
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					// true); Sinon erreur
					jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
					jaxbMarshaller.marshal(stocksQuotes, os);
					message.setBody(new Document(message.getBody().getContentType(), message.getBody().getDataType(),
							message.getBody().getDataClass(), message.getBody().getProvider(), os.toByteArray()));

				} catch (JAXBException e) {
					e.printStackTrace();
					message.setSend(false);
				}
			
		}
	}


