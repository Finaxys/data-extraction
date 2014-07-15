package com.finaxys.rd.dataextraction.service;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

public interface HistInterbankRateDataService {
	public List<Message> getHistRatesData(Integer year) throws Exception;
}
