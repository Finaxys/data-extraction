package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

public interface InterbankRateDataService {
	public List<Message> getCurrentRatesData() throws Exception;
}
