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

import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.CurrencyPair;
import com.finaxys.rd.dataextraction.domain.Document;


public class FileXlsCurrencyPairsParser implements Parser<CurrencyPair> {

	public List<CurrencyPair> parse(Document document) throws Exception {

		InputStream is = new ByteArrayInputStream(document.getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<CurrencyPair> list = new ArrayList<CurrencyPair>();
		

		Row row = rowIterator.next();
		CurrencyPair currencyPair;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			try {
				currencyPair = new CurrencyPair();
				String sym = cellIterator.next().toString();
				String[] symbol = (sym.split(" ")[0]).split("/");
				if (symbol.length == 2) {
					currencyPair.setSymbol(symbol[0] + symbol[1]);
					currencyPair.setBaseCurrency(symbol[0]);
					currencyPair.setQuoteCurrency(symbol[1]);
					currencyPair.setProvider(cellIterator.next().toString().charAt(0));
					currencyPair.setSource(document.getSource());
					currencyPair.setInputDate(new DateTime());
					currencyPair.setDataType(document.getDataType());
					list.add(currencyPair);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

}
