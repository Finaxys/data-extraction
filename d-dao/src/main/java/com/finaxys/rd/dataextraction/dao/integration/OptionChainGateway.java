package com.finaxys.rd.dataextraction.dao.integration;

import com.finaxys.rd.dataextraction.domain.msg.Document;

public interface OptionChainGateway {
	public Document getOptionChains(String symbols) throws Exception;
}
