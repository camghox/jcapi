package com.zhaohuo18.api.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductVO implements Serializable{
	
	private String title;
	private List<String> piclist;
	
	public ProductVO() {
		super();
		// TODO Auto-generated constructor stub
		this.piclist = new ArrayList<String>();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getPiclist() {
		return piclist;
	}
	public void setPiclist(List<String> piclist) {
		this.piclist = piclist;
	}
	
	

}
