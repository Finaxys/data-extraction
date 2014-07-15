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
import com.finaxys.rd.dataextraction.domain.CurrencyPair;
import com.finaxys.rd.dataextraction.domain.CurrencyPairs;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsCurrencyPairsConverter.
 */
public class FileXlsCurrencyPairsConverter implements Converter {


	public void convert(Message message) throws Exception {

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<CurrencyPair> list = new ArrayList<CurrencyPair>();
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		Row row = rowIterator.next();
		CurrencyPair currencyPair;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			try {
				currencyPair = new CurrencyPair();
				String sym = cellIterator.next().toString();
				String[] symbol = (sym.split(" ")[0]).split("/");
				currencyPair.setSymbol(symbol[0] + symbol[1]);
				currencyPair.setBaseCurrency(symbol[0]);
				currencyPair.setQuoteCurrency(symbol[1]);
				list.add(currencyPair);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		CurrencyPairs currencyPairs = new CurrencyPairs();
		currencyPairs.setCurrencyPairsList(list);
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(CurrencyPairs.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); Sinon erreur
				jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				jaxbMarshaller.marshal(currencyPairs, os);
				message.setBody(new Document(message.getBody().getContentType(), message.getBody().getDataType(), message.getBody().getDataClass(), message.getBody().getProvider(), os.toByteArray()));
				message.getBody().setContentType(ContentType.XML);
			} catch (JAXBException e) {
				e.printStackTrace();
				message.setSend(false);
			}
	}

}
