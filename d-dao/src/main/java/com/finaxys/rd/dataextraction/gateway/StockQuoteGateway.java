package com.finaxys.rd.dataextraction.gateway;

import java.io.File;

import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataType;

public interface StockQuoteGateway {
	public char getProviderSymb();
	public File getCurrentStocksQuotes(ContentType format, DataType type, String symbs);
}
