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
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.domain.Stock;
import com.finaxys.rd.dataextraction.domain.Stocks;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsStocksConverter.
 */
public class FileXlsStocksConverter implements Converter {

	/** The default exchange el. */
	@Value("${converter.file.stocks.default_exchange_el}")
	public String DEFAULT_EXCHANGE_EL;

	
	public void convert(Message message) throws Exception {

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<Stock> list = new ArrayList<Stock>();
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		Row row = rowIterator.next();
		Stock stock;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();

			try {
				stock = new Stock();

				String symbol = cellIterator.next().toString();
				stock.setSymbol(symbol);

				String exchSymb = DEFAULT_EXCHANGE_EL;
				if (symbol.indexOf('.') != -1)
					exchSymb = symbol.substring(symbol.indexOf('.') + 1);
				stock.setExchSymb(exchSymb);

				stock.setProvider(message.getBody().getProvider());
				stock.setCompanyName(cellIterator.next().toString());
				list.add(stock);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Stocks stocks = new Stocks();
		stocks.setStocksList(list);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Stocks.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
			// true); Sinon erreur
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			jaxbMarshaller.marshal(stocks, os);
			message.setBody(new Document(message.getBody().getContentType(), message.getBody().getDataType(), message
					.getBody().getDataClass(), message.getBody().getProvider(), os.toByteArray()));
			message.getBody().setContentType(ContentType.XML);
		} catch (JAXBException e) {
			e.printStackTrace();
			message.setSend(false);
		}
	}

}