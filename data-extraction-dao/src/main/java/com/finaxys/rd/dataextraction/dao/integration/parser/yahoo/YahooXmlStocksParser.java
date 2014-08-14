/*
 * 
 */
package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.exception.ParserException;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Stock;

// TODO: Auto-generated Javadoc
/**
 * The Class YahooXmlStocksConverter.
 */
public class YahooXmlStocksParser implements Parser<Stock> {

	private static Logger logger = Logger
			.getLogger(YahooXmlStocksParser.class);
	
	@Value("${parser.yahoo.stocks.new.stock_el}")
	private String ITEM_EL;
	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.parser.Parser#parse(com.finaxys.rd.dataextraction.msg.Message)
	 */
	public List<Stock> parse(Document document) throws ParserException {

		return null;
	}
}
