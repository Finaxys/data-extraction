package com.finaxys.rd.dataextraction.gateway;

import java.io.File;

import com.finaxys.rd.dataextraction.msg.Document.ContentType;

public interface RateDataGateway {
	public char getProviderSymb();
	public File getRatesData(ContentType format);

}
