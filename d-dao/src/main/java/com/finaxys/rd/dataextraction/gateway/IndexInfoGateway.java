package com.finaxys.rd.dataextraction.gateway;

import java.io.File;

import com.finaxys.rd.dataextraction.msg.Document.ContentType;

public interface IndexInfoGateway {
	public char getProviderSymb();
	public File getIndexInfos(ContentType format);
}
