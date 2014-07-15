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

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.domain.InterbankRate;
import com.finaxys.rd.dataextraction.domain.InterbankRates;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsStocksConverter.
 */
public class FileXlsInterbankRatesConverter implements Converter {


	public void convert(Message message) throws Exception {

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<InterbankRate> list = new ArrayList<InterbankRate>();
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		Row row = rowIterator.next();
		InterbankRate interbankRate;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			Iterator<Cell> cellIterator = row.cellIterator();

			try {
				interbankRate = new InterbankRate();
				if (cellIterator.hasNext())
					interbankRate.setSymbol(cellIterator.next().toString());
				if (cellIterator.hasNext())
					interbankRate.setCurrency(cellIterator.next().toString());
				interbankRate.setProvider(message.getBody().getProvider());
				list.add(interbankRate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		InterbankRates interbankRates = new InterbankRates();
		interbankRates.setRatesList(list);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(InterbankRates.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
			// true); Sinon erreur
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			jaxbMarshaller.marshal(interbankRates, os);
			message.setBody(new Document(message.getBody().getContentType(), message.getBody().getDataType(), message
					.getBody().getDataClass(), message.getBody().getProvider(), os.toByteArray()));
			message.getBody().setContentType(ContentType.XML);
		} catch (JAXBException e) {
			e.printStackTrace();
			message.setSend(false);
		}
	}

}
