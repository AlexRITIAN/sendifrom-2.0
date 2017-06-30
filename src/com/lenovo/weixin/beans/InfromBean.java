package com.lenovo.weixin.beans;

import java.sql.Timestamp;

/**
 * 表Infrom实体类
 * @author yuhao5
 *
 */
public class InfromBean {
	int id;
	String  name;
	String  infrom;
	Timestamp  creat_time ;
	Timestamp  edit_time;
	
	public InfromBean(){
		super();
	}

	public InfromBean(int id, String name, String infrom, Timestamp creat_time, Timestamp edit_time) {
		super();
		this.id = id;
		this.name = name;
		this.infrom = infrom;
		this.creat_time = creat_time;
		this.edit_time = edit_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfrom() {
		return infrom;
	}

	public void setInfrom(String infrom) {
		this.infrom = infrom;
	}

	public Timestamp getCreat_time() {
		return creat_time;
	}

	public void setCreat_time(Timestamp creat_time) {
		this.creat_time = creat_time;
	}

	public Timestamp getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(Timestamp edit_time) {
		this.edit_time = edit_time;
	}

	@Override
	public String toString() {
		return "InfromBean [id=" + id + ", name=" + name + ", infrom=" + infrom + ", creat_time=" + creat_time
				+ ", edit_time=" + edit_time + "]";
	}
	
	
	
	
	
}
