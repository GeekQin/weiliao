package com.Dinggrn.weiliao.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter{
	
	Context context;
	List<T> datasource;
	LayoutInflater inflater;
	
	
	
	public MyBaseAdapter(Context context, List<T> datasource) {
		super();
		this.context = context;
		this.datasource = datasource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return datasource.size();
	}

	@Override
	public T getItem(int position) {
		return datasource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return getItemView(position, convertView, parent);
	}
	
	public abstract View getItemView(int position, View convertView, ViewGroup parent);
	
	
	public void addAll(List<T> list,boolean isClear){
		if(isClear){
			datasource.clear();
		}
		datasource.addAll(list);
		notifyDataSetChanged();
	}
	
	public void add(T t){
		datasource.add(t);
		notifyDataSetChanged();
	}
	
	public void remove(T t){
		datasource.remove(t);
		notifyDataSetChanged();
	}
	
	
}
