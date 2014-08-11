package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;

import com.finaxys.rd.dataextraction.domain.CurrencyPair;
import com.finaxys.rd.dataextraction.domain.DataWrapper;
import com.finaxys.rd.dataextraction.domain.FXRate;
import com.finaxys.rd.dataextraction.domain.IndexQuote;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.OptionQuote;
import com.finaxys.rd.dataextraction.domain.Stock;
import com.finaxys.rd.dataextraction.domain.StockQuote;

public class TestHelper {

	
	public static byte[] getResourceAsBytes(String path) throws IOException{
		InputStream in = TestHelper.class.getResourceAsStream(path);
		return IOUtils.toByteArray(in);
	}
	
	public static String marshall(DataWrapper<?> currencyPairs) throws JAXBException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(DataWrapper.class,
				CurrencyPair.class, Stock.class, FXRate.class, IndexQuote.class, OptionChain.class
				,OptionQuote.class, StockQuote.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(currencyPairs, os);
		return new String(os.toByteArray());
	}
}
