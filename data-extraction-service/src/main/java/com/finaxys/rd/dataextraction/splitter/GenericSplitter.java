package com.finaxys.rd.dataextraction.splitter;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.Splittable;

public class GenericSplitter<T extends Splittable> {
	
	private  int size;


	public GenericSplitter(int size) {
		super();
		this.size = size;
	}

	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
	}

	public List<?> split(T t){
		
		return t.split(this.size);
	}
}
