package com.finaxys.rd.dataextraction.dao.integration.parser.file;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.finaxys.rd.dataextraction.domain.Enum.Bucket;
import com.finaxys.rd.dataextraction.domain.InterbankRateData;

public class FileXlsHistInterbankRateDataParser implements Parser<InterbankRateData> {


	public List<InterbankRateData> parse(Document document) throws Exception {

		InputStream is = new ByteArrayInputStream(document.getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<InterbankRateData> list = new ArrayList<InterbankRateData>();
		

		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		Row row = rowIterator.next();
		
		InterbankRateData rate;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			
			Iterator<Cell> cellIterator = row.cellIterator();
			try{
			rate = new InterbankRateData();
			rate.setSymbol(cellIterator.next().toString());
			rate.setCurrency(cellIterator.next().toString());
			rate.setDataType(document.getDataType());
			rate.setSource(document.getSource());
			rate.setBucket(Bucket.valueOf("_" + cellIterator.next().toString()).getName());
			rate.setRateDateTime(formatter.parseDateTime(cellIterator.next().toString()));
			rate.setValue(new BigDecimal(cellIterator.next().toString()));
			rate.setInputDate(new DateTime());
			list.add(rate);

			}catch(Exception e){
				e.printStackTrace();
			}

		}

		return list;
	}
}
