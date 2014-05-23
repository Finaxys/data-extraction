package converter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.jdom.JDOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HomeExchangeConverter implements Converter {

	public byte[] convert(File f) throws Exception {
		FileInputStream file;
		file = new FileInputStream(f);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element exchanges = doc.createElement("exchanges");
		doc.appendChild(exchanges);
		Element exchangeList = doc.createElement("exchangeList");
		exchanges.appendChild(exchangeList);

		HSSFWorkbook workbook = new HSSFWorkbook(file);

		// Get first sheet from the workbook

		HSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();

		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			Element exchange = doc.createElement("exchange");
			exchangeList.appendChild(exchange);

			// For each row, iterate through each columns
			Iterator<Cell> cellIterator = row.cellIterator();

			Element mic = doc.createElement("mic");
			mic.appendChild(doc.createTextNode(cellIterator.next().toString()));
			exchange.appendChild(mic);

			Element symbol = doc.createElement("symbol");
			mic.appendChild(doc.createTextNode(cellIterator.next().toString()));
			exchange.appendChild(symbol);

			Element provider = doc.createElement("provider");
			mic.appendChild(doc.createTextNode(cellIterator.next().toString()));
			exchange.appendChild(provider);

			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(cellIterator.next().toString()));
			exchange.appendChild(name);

			Element type = doc.createElement("type");
			type.appendChild(doc.createTextNode(cellIterator.next().toString()));
			exchange.appendChild(type);

			Element continent = doc.createElement("continent");
			continent.appendChild(doc.createTextNode(cellIterator.next().toString()));
			exchange.appendChild(continent);

			Element country = doc.createElement("country");
			country.appendChild(doc.createTextNode(cellIterator.next().toString()));
			exchange.appendChild(country);

			Element currency = doc.createElement("currency");
			currency.appendChild(doc.createTextNode(cellIterator.next().toString()));
			exchange.appendChild(currency);

			Element openTime = doc.createElement("openTime");
			openTime.appendChild(doc.createTextNode(Long.toString(cellIterator.next().getDateCellValue().getTime())));
			exchange.appendChild(openTime);

			Element closeTime = doc.createElement("closeTime");
			closeTime.appendChild(doc.createTextNode(Long.toString(cellIterator.next().getDateCellValue().getTime())));
			exchange.appendChild(closeTime);

			Element status = doc.createElement("status");
			status.appendChild(doc.createTextNode(cellIterator.next().toString()));
			exchange.appendChild(status);

		}
		file.close();

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(os);

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);
		return os.toByteArray();

	}

	public byte[] stockSummariesToXml(File f){
		// TODO Auto-generated method stub
		return null;
	}
}
