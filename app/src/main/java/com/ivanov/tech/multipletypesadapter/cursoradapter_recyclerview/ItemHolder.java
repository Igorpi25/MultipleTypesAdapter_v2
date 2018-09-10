package com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview;

import android.view.View;
import android.view.ViewGroup;

public interface ItemHolder<T> {
		
	public ItemHolder<T> createClone();
	
	public View getView(View convertView, ViewGroup parent, T data);
	
	boolean isEnabled();
}
