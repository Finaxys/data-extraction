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
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.OptionChains;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class YahooXmlOptionChainsConverter implements Converter {
	/** The date format. */
	@Value("${converter.yahoo.option_chains.date_format}")
	private String DATE_FORMAT;

	@Value("${converter.yahoo.option_chains.old.no_data}")
	private String NO_DATA;

	@Value("${converter.yahoo.option_chains.old.main_option_el}")
	private String MAIN_OPTION_EL;

	@Value("${converter.yahoo.option_chains.old.symbol_att}")
	private String SYMBOL_ATT;

	@Value("${converter.yahoo.option_chains.old.contract_el}")
	private String CONTRACT_EL;


	public void convert(Message message) throws Exception {
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(message.getBody()
				.getContent());
		StreamSource source = new StreamSource(is);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		List<OptionChain> list = new ArrayList<OptionChain>();
		String date = null;
		String time = null;
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);

		OptionChain optionChain = null;
		String symbol = "";
		while (reader.hasNext()) {
			try {
				XMLEvent ev = reader.nextEvent();

				if (ev.isStartElement()
						&& ((StartElement) ev).getName().getLocalPart()
								.equals(MAIN_OPTION_EL)) {
					Attribute a = ((StartElement) ev)
							.getAttributeByName(new QName(SYMBOL_ATT));
					if (a != null && !a.getValue().equals(""))
						symbol = a.getValue();
					else
						message.setSend(false);
				}
				if (ev.isStartElement()
						&& ((StartElement) ev).getName().getLocalPart()
								.equals(CONTRACT_EL)
						&& message.getBody().getProvider() != Character.UNASSIGNED
						&& message.isSend()) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters()
							&& !evt.asCharacters().getData().equals(NO_DATA)) {
						optionChain = new OptionChain(symbol, formatter
								.parseDateTime(evt.asCharacters().getData())
								.toLocalDate(), new LocalDate(), message.getBody().getProvider());
						list.add(optionChain);
						message.setSend(true);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				message.setSend(false);
			}
		}
		OptionChains optionChains = new OptionChains();
		optionChains.setOptionChainsList(list);
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(OptionChains.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
			// true); Sinon erreur
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			jaxbMarshaller.marshal(optionChains, os);
			message.setBody(new Document(message.getBody().getContentType(),
					message.getBody().getDataType(), message.getBody()
							.getDataClass(), message.getBody().getProvider(),
					os.toByteArray()));

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

}
