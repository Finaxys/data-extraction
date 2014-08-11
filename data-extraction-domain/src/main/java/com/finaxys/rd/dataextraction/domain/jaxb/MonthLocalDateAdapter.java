package com.finaxys.rd.dataextraction.domain.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
 
public class MonthLocalDateAdapter
    extends XmlAdapter<String, LocalDate>{
	DateTimeFormatter dformatter = DateTimeFormat.forPattern("yyyy-MM");
	
    public LocalDate unmarshal(String v) throws Exception {
        return dformatter.parseDateTime(v).toLocalDate();
    }
 
    public String marshal(LocalDate v) throws Exception {
        return dformatter.print(v);
    }
 
}