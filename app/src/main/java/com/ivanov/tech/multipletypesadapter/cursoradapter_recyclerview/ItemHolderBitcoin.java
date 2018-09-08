package com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivanov.tech.multipletypesadapter.BinderTextView;
import com.ivanov.tech.multipletypesadapter.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemHolderBitcoin extends CursorItemHolder{

	private static final String TAG = ItemHolderBitcoin.class.getSimpleName();

	TextView textview_name, textview_symbol, textview_website_slug;

	OnClickListener onclicklistener;

	protected ItemHolderBitcoin(View itemView, Context context, OnClickListener onclicklistener) {
		super(itemView);

		this.context=context;
		this.onclicklistener=onclicklistener;

	}

	public ItemHolderBitcoin(Context context, OnClickListener onclicklistener) {
		super(new View(context));
		
		this.context=context;
		this.onclicklistener=onclicklistener;		
	}
	
	@Override
	public CursorItemHolder createClone(ViewGroup parent) {	
		
		Log.d(TAG, "createClone");
		
		View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bitcoin, parent, false);
		
		ItemHolderBitcoin itemholder=new ItemHolderBitcoin(view,context,onclicklistener);
		
		itemholder.textview_name = (TextView) view.findViewById(R.id.item_bitcoin_name);
		itemholder.textview_symbol = (TextView) view.findViewById(R.id.item_bitcoin_symbol);
		itemholder.textview_website_slug = (TextView) view.findViewById(R.id.item_bitcoin_website_slug);
		
		if(itemholder.onclicklistener!=null){
			view.setOnClickListener(itemholder.onclicklistener);
		}
		
		return itemholder;
	}

	@Override
	public void bindView(Cursor cursor) {
		JSONObject json;
		try {
			json = new JSONObject(CursorMultipleTypesAdapter.getValue(cursor));
			
			Log.d(TAG, "bindView json="+json);
						
			textview_name.setText(json.getString("name"));
			textview_symbol.setText(json.getString("symbol"));
			textview_website_slug.setText(json.getString("website_slug"));

			itemView.setTag(itemView.getId(), CursorMultipleTypesAdapter.getKey(cursor));
									
		} catch (JSONException e) {
			Log.e(TAG, "bindView JSONException e="+e);
		}
	}


}
