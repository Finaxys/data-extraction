package com.finaxys.rd.dataextraction.gateway;

import java.io.File;

import com.finaxys.rd.dataextraction.msg.Document.ContentType;

public interface RateGateway {
	public char getProviderSymb();
	public File getRates(ContentType format);
}
