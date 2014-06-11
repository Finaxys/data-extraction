package com.finaxys.rd.dataextraction.gateway;

import java.io.File;

import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataType;

public interface FXRateGateway {
	public char getProviderSymb();
	public File getFXRates(ContentType format, String symbs);
}
