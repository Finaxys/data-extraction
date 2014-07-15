package com.finaxys.rd.dataextraction.converter.ebf.xls;

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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.domain.InterbankRateData;
import com.finaxys.rd.dataextraction.domain.InterbankRateDatas;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

public class EbfXlsHistEuriborConverter implements Converter {

	@Value("${converter.ebf.hist_interbank_rate_data.euribor_symb}")
	public String EURIBOR_SYMB;

	@Value("${converter.ebf.hist_interbank_rate_data.euro_symb}")
	public String EURO_SYMB;

	public void convert(Message message) throws Exception {

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<DateTime> dates = new ArrayList<DateTime>();
		List<InterbankRateData> list = new ArrayList<InterbankRateData>();
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
		Row row = rowIterator.next();
		Iterator<Cell> cellIterator = row.cellIterator();
		cellIterator.next();
		String date = "";
		while (cellIterator.hasNext() && !(date = cellIterator.next().toString()).equals(""))
			dates.add(formatter.parseDateTime(date));

		String bucket = "";
		while (rowIterator.hasNext()
				&& !(bucket = (cellIterator = (row = rowIterator.next()).cellIterator()).next().toString()).equals("")) {

			Iterator<DateTime> datesIterator = dates.iterator();
			String value = "";
			InterbankRateData rate;
			while (cellIterator.hasNext() && !(value = cellIterator.next().toString()).equals("")) {
				try {
					rate = new InterbankRateData();
					rate.setSymbol(EURIBOR_SYMB);
					rate.setCurrency(EURO_SYMB);
					rate.setBucket(bucket);
					rate.setDataType(message.getBody().getDataType());
					rate.setProvider(message.getBody().getProvider());
					rate.setTs(datesIterator.next());
					rate.setValue(new BigDecimal(value));
					list.add(rate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		InterbankRateDatas rates = new InterbankRateDatas();
		rates.setRatesList(list);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(InterbankRateDatas.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
			// true); Sinon erreur
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			jaxbMarshaller.marshal(rates, os);
			message.setBody(new Document(message.getBody().getContentType(), message.getBody().getDataType(), message
					.getBody().getDataClass(), message.getBody().getProvider(), os.toByteArray()));
			message.getBody().setContentType(ContentType.XML);
		} catch (JAXBException e) {
			e.printStackTrace();
			message.setSend(false);
		}

	}
}
