/*
 * 
 */
package com.finaxys.rd.dataextraction.converter.file.xls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.domain.Exchange;
import com.finaxys.rd.dataextraction.domain.Exchanges;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;


// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsExchangesConverter.
 */
public class FileXlsExchangesConverter implements Converter {
	

	public void convert(Message message) throws Exception {
		

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<Exchange> list = new ArrayList<Exchange>();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
		
		Row row = rowIterator.next();
		Exchange exchange;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			Iterator<Cell> cellIterator = row.cellIterator();


			try {
				exchange = new Exchange();
			exchange.setMic(cellIterator.next().toString());
			exchange.setSymbol(cellIterator.next().toString());
			exchange.setSuffix(cellIterator.next().toString());
			exchange.setProvider(message.getBody().getProvider());
			exchange.setName(cellIterator.next().toString());
			exchange.setType(cellIterator.next().toString());
			exchange.setContinent(cellIterator.next().toString());
			exchange.setCountry(cellIterator.next().toString());
			exchange.setCurrency(cellIterator.next().toString());
			exchange.setOpenTime(formatter.parseDateTime(cellIterator.next().toString()));
			exchange.setCloseTime(formatter.parseDateTime(cellIterator.next().toString()));
			exchange.setStatus(new Boolean(cellIterator.next().toString()));
			list.add(exchange);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Exchanges exchanges = new Exchanges();
		exchanges.setExchangesList(list);
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Exchanges.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); Sinon erreur
				jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				jaxbMarshaller.marshal(exchanges, os);
				message.setBody(new Document(message.getBody().getContentType(), message.getBody().getDataType(), message.getBody().getDataClass(), message.getBody().getProvider(), os.toByteArray()));
				message.getBody().setContentType(ContentType.XML);
			} catch (JAXBException e) {
				e.printStackTrace();
				message.setSend(false);
			}
	}

}
