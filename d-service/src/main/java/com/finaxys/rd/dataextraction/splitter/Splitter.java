package com.finaxys.rd.dataextraction.splitter;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

public interface Splitter {
	public List<Message> split(Message message) throws Exception;
}
