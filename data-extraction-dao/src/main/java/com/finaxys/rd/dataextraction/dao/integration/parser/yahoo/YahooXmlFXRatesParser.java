/*
 * 
 */
package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.finaxys.rd.dataextraction.domain.FXRate;

// TODO: Auto-genequoted Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlFXRatesParser implements Parser<FXRate> {
	/** The date format. */
	@Value("${parser.yahoo.fx_rates.date_format}")
	private String DATE_FORMAT;

	@Value("${parser.yahoo.fx_rates.old.no_data}")
	private String NO_DATA;

	@Value("${parser.yahoo.fx_rates.old.main_rate_el}")
	private String MAIN_RATE_EL;

	/** The id att. */
	@Value("${parser.yahoo.fx_rates.old.id_att}")
	private String ID_ATT;

	@Value("${parser.yahoo.fx_rates.old.rate_el}")
	private String RATE_EL;

	@Value("${parser.yahoo.fx_rates.old.date_el}")
	private String DATE_EL;

	@Value("${parser.yahoo.fx_rates.old.time_el}")
	private String TIME_EL;

	@Value("${parser.yahoo.fx_rates.old.ask_el}")
	private String ASK_EL;

	@Value("${parser.yahoo.fx_rates.old.bid_el}")
	private String BID_EL;

	public List<FXRate> parse(Document document) throws Exception {
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(document.getContent());
		StreamSource source = new StreamSource(is);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		
		List<FXRate> list = new ArrayList<FXRate>();
		String date = null;
		String time = null;
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);

		FXRate fxRate = null;
		boolean isValid = true;
		while (reader.hasNext()) {
			try {
				XMLEvent ev = reader.nextEvent();

				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(MAIN_RATE_EL)) {
					fxRate = new FXRate();
					isValid = true;
					Attribute a = ((StartElement) ev).getAttributeByName(new QName(ID_ATT));
					if (a != null && !a.getValue().equals(""))
						fxRate.setSymbol(a.getValue());
					else
						isValid = false;
				} else if(fxRate != null && isValid){
					if(ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(RATE_EL)) {
				
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						fxRate.setRate(new BigDecimal(evt.asCharacters().getData()));
					else
						isValid = false;
				} else if ( ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(ASK_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						fxRate.setAsk(new BigDecimal(evt.asCharacters().getData()));

				} else if ( ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(BID_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						fxRate.setBid(new BigDecimal(evt.asCharacters().getData()));

				} else if ( ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DATE_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						date = evt.asCharacters().getData();
					else
						isValid = false;
				} else if ( ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(TIME_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						time = evt.asCharacters().getData();
					else
						isValid = false;
				} else if (isValid  && ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(MAIN_RATE_EL)) {

					if (date != null && time != null && document.getSource() != Character.UNASSIGNED && document.getDataType() != null) {
						DateTime ts = formatter.parseDateTime(date + " " + time);
						fxRate.setRateDateTime(ts);
						fxRate.setSource(document.getSource());
						fxRate.setDataType(document.getDataType());
						fxRate.setInputDate(new DateTime());
						list.add(fxRate);
						fxRate = null;
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
