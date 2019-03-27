package com.gb.apm.model;

import java.util.ArrayList;
import java.util.List;

public class TDataSourceList extends APMModel {
	private List<TDataSource> dataSourceList = new ArrayList<>(); // required

	public List<TDataSource> getDataSourceList() {
		return dataSourceList;
	}

	public void setDataSourceList(List<TDataSource> dataSourceList) {
		this.dataSourceList = dataSourceList;
	}

	public void addToDataSourceList(TDataSource datasource) {
		dataSourceList.add(datasource);
	}
}
