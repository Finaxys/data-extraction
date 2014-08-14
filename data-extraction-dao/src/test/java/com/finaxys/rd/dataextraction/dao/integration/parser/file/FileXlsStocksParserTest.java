package com.finaxys.rd.dataextraction.dao.integration.parser.file;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.finaxys.rd.dataextraction.dao.integration.parser.yahoo.TestHelper;
import com.finaxys.rd.dataextraction.domain.DataWrapper;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.Stock;
import com.google.protobuf.Message;

public class FileXlsStocksParserTest {

	FileXlsStocksParser target;

	@Before
	public void setUp() {
		target = new FileXlsStocksParser();
	}

	@Test
	public void test_convert() throws IOException, JAXBException   {
		Stock stock = new Stock();
		stock.setSymbol("TIF");
		stock.setCompanyName("Tiffany & Co.");
		stock.setExchSymb("NY");
		stock.setProvider('1');
		stock.setSource('0');
		stock.setDataType(DataType.REF);
		stock.setInputDate(new DateTime());
		List<Stock> outList = new ArrayList<Stock>();
		outList.add(stock);
		DataWrapper<Stock> out = new DataWrapper<Stock>(outList);

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsStocksParser/test_convert.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.REF);

		// Execution
		List<Stock> inList = target.parse(inMessageFixture);
		DataWrapper<Stock> in = new DataWrapper<Stock>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}


}