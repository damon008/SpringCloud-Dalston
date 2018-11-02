package com.leinao.file.model;

import java.util.ArrayList;
import java.util.List;

public class EchartBarData {
	public List<String> legend = new ArrayList<String>();//图例
	public List<String> category = new ArrayList<String>();//横（纵）坐标
	public List<Series> series = new ArrayList<Series>();//纵（横）坐标
	
	public EchartBarData() {
		super();
	}

	public EchartBarData(List<String> legendList, List<String> categoryList, List<Series> seriesList) {
		super();
		this.legend = legendList;
		this.category = categoryList;
		this.series = seriesList;
	}


	

	public List<String> getLegend() {
		return legend;
	}


	public void setLegend(List<String> legend) {
		this.legend = legend;
	}


	public List<String> getCategory() {
		return category;
	}


	public void setCategory(List<String> category) {
		this.category = category;
	}


	public List<Series> getSeries() {
		return series;
	}


	public void setSeries(List<Series> series) {
		this.series = series;
	}
	
	
}
