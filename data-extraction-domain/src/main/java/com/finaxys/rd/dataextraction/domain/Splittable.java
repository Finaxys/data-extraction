package com.finaxys.rd.dataextraction.domain;


import java.util.List;

public interface Splittable {
	public List<?> split(int n);
	public List<?> split();
}
