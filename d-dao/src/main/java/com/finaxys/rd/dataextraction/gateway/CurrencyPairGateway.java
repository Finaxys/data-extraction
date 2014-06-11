package com.finaxys.rd.dataextraction.gateway;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.msg.Document.ContentType;

public interface CurrencyPairGateway {
	public char getProviderSymb();
	public File getCurrencyPairs(ContentType format);
}
