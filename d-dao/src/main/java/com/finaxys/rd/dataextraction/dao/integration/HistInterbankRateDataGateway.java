package com.finaxys.rd.dataextraction.dao.integration;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

public interface HistInterbankRateDataGateway {
	public Document getHistRatesData(Integer year) throws Exception;
}
