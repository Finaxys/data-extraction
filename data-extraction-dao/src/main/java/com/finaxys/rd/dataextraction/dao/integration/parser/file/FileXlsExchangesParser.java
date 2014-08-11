/*
 * 
 */
package com.finaxys.rd.dataextraction.dao.integration.parser.file;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Exchange;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsExchangesConverter.
 */
public class FileXlsExchangesParser implements Parser<Exchange> {

	public List<Exchange> parse(Document document) throws Exception {

		InputStream is = new ByteArrayInputStream(document.getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<Exchange> list = new ArrayList<Exchange>();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");


		Row row = rowIterator.next();
		Exchange exchange;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			Iterator<Cell> cellIterator = row.cellIterator();

			try {
				exchange = new Exchange();
				exchange.setSymbol(cellIterator.next().toString());
				exchange.setSourceSymbol(cellIterator.next().toString());
				exchange.setProvider(cellIterator.next().toString().charAt(0));
				exchange.setSource(document.getSource());
				exchange.setName(cellIterator.next().toString());
				exchange.setType(cellIterator.next().toString());
				exchange.setContinent(cellIterator.next().toString());
				exchange.setCountry(cellIterator.next().toString());
				exchange.setCurrency(cellIterator.next().toString());
				exchange.setOpenTime(formatter.parseDateTime(cellIterator.next().toString()).toLocalTime());
				exchange.setCloseTime(formatter.parseDateTime(cellIterator.next().toString()).toLocalTime());
				Cell delayCell = cellIterator.next();
				delayCell.setCellType(Cell.CELL_TYPE_STRING);
				exchange.setDelay(new Integer(delayCell.toString()));
				exchange.setInputDate(new DateTime());
				exchange.setDataType(document.getDataType());
				list.add(exchange);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

}
