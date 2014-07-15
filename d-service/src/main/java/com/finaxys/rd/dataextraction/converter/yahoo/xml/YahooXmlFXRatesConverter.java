/*
 * 
 */
package com.finaxys.rd.dataextraction.converter.yahoo.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.finaxys.rd.dataextraction.domain.FXRate;
import com.finaxys.rd.dataextraction.domain.FXRates;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-genequoted Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlFXRatesConverter implements Converter {
	/** The date format. */
	@Value("${converter.yahoo.fx_rates.date_format}")
	private String DATE_FORMAT;

	@Value("${converter.yahoo.fx_rates.old.no_data}")
	private String NO_DATA;

	@Value("${converter.yahoo.fx_rates.old.main_rate_el}")
	private String MAIN_RATE_EL;

	/** The id att. */
	@Value("${converter.yahoo.fx_rates.old.id_att}")
	private String ID_ATT;

	@Value("${converter.yahoo.fx_rates.old.rate_el}")
	private String RATE_EL;

	@Value("${converter.yahoo.fx_rates.old.date_el}")
	private String DATE_EL;

	@Value("${converter.yahoo.fx_rates.old.time_el}")
	private String TIME_EL;

	@Value("${converter.yahoo.fx_rates.old.ask_el}")
	private String ASK_EL;

	@Value("${converter.yahoo.fx_rates.old.bid_el}")
	private String BID_EL;

	public void convert(Message message) throws Exception {
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		StreamSource source = new StreamSource(is);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		List<FXRate> list = new ArrayList<FXRate>();
		String date = null;
		String time = null;
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);

		FXRate fxRate = null;
		while (reader.hasNext()) {
			try {
				XMLEvent ev = reader.nextEvent();

				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(MAIN_RATE_EL)) {
					fxRate = new FXRate();
					Attribute a = ((StartElement) ev).getAttributeByName(new QName(ID_ATT));
					if (a != null && !a.getValue().equals(""))
						fxRate.setSymbol(a.getValue());
					else
						message.setSend(false);
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(RATE_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						fxRate.setRate(new BigDecimal(evt.asCharacters().getData()));
					else
						message.setSend(false);
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(ASK_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						fxRate.setAsk(new BigDecimal(evt.asCharacters().getData()));
					else
						message.setSend(false);
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(BID_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						fxRate.setBid(new BigDecimal(evt.asCharacters().getData()));
					else
						message.setSend(false);
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DATE_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						date = evt.asCharacters().getData();
					else
						message.setSend(false);
				}
				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(TIME_EL)) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
						time = evt.asCharacters().getData();
					else
						message.setSend(false);
				}
				if (ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(MAIN_RATE_EL)) {
					if (message.isSend()) {
						if (date != null && time != null) {
							DateTime ts = formatter.parseDateTime(date + " " + time);
							fxRate.setTs(ts);
						} else
							message.setSend(false);
						if (message.getBody().getProvider() != Character.UNASSIGNED)
							fxRate.setProvider(message.getBody().getProvider());
						else
							message.setSend(false);
						if (message.getBody().getDataType() != null)
							fxRate.setDataType(message.getBody().getDataType());
						else
							message.setSend(false);
						if (message.isSend()) {
							list.add(fxRate);
							message.setSend(true);
							fxRate = new FXRate();

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				message.setSend(false);
			}
		}
		FXRates fxRates = new FXRates();
		fxRates.setIndInfosList(list);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(FXRates.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
			// true); Sinon erreur
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			jaxbMarshaller.marshal(fxRates, os);
			message.setBody(new Document(message.getBody().getContentType(), message.getBody().getDataType(), message
					.getBody().getDataClass(), message.getBody().getProvider(), os.toByteArray()));

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

}
