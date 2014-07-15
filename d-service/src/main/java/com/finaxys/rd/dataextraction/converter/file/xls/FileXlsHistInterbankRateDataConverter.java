package com.finaxys.rd.dataextraction.converter.file.xls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.finaxys.rd.dataextraction.domain.InterbankRateData;
import com.finaxys.rd.dataextraction.domain.InterbankRateDatas;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

public class FileXlsHistInterbankRateDataConverter implements Converter {


	public void convert(Message message) throws Exception {

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<InterbankRateData> list = new ArrayList<InterbankRateData>();
		ByteArrayOutputStream os = new ByteArrayOutputStream();

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
			rate.setDataType(message.getBody().getDataType());
			rate.setProvider(message.getBody().getProvider());
			rate.setBucket(cellIterator.next().toString());
			rate.setTs(formatter.parseDateTime(cellIterator.next().toString()));
			rate.setValue(new BigDecimal(cellIterator.next().toString()));
			list.add(rate);

			}catch(Exception e){
				e.printStackTrace();
			}

		}

		InterbankRateDatas rates = new InterbankRateDatas();
		rates.setRatesList(list);
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(InterbankRateDatas.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); Sinon erreur
				jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				jaxbMarshaller.marshal(rates, os);
				message.setBody(new Document(message.getBody().getContentType(), message.getBody().getDataType(), message.getBody().getDataClass(), message.getBody().getProvider(), os.toByteArray()));
				message.getBody().setContentType(ContentType.XML);
			} catch (JAXBException e) {
				e.printStackTrace();
				message.setSend(false);
			}

	}
}
