package com.ivanov.tech.multipletypesadapter.demo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ivanov.tech.connection.Connection;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.CursorMultipleTypesAdapter;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderBitcoin;
import com.ivanov.tech.multipletypesadapter.R;

import android.content.Context;

import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class FragmentDemo extends DialogFragment implements OnClickListener{
	
	private static final String TAG = FragmentDemo.class.getSimpleName();

	String url="https://api.coinmarketcap.com/v2/listings/";
	
	protected static final int TYPE_ITEM_BITCOIN = 0;

	RecyclerView recyclerview;
    
    protected CursorMultipleTypesAdapter adapter=null;

	MenuItem menu_itme_reload;
    
    public static FragmentDemo newInstance() {
    	FragmentDemo f = new FragmentDemo();
       
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View view = null;
        view = inflater.inflate(R.layout.fragment_demo, container, false);
        
        recyclerview=(RecyclerView)view.findViewById(R.id.fragment_demo_rv_recyclerview);

        //??????? ????????? RecyclerView, ?????? ??????????? ?????????
		LinearLayoutManager layoutmanager=new LinearLayoutManager(recyclerview.getContext());
		layoutmanager.setInitialPrefetchItemCount(100);
		layoutmanager.setItemPrefetchEnabled(true);
        recyclerview.setLayoutManager(layoutmanager);
		recyclerview.setHasFixedSize(true);

        adapter=new CursorMultipleTypesAdapter(getActivity(),null);
      
        //????? ???????????? ?????? ?????? https://github.com/igorpi25/multipletypesadapter
        adapter.addItemHolder(TYPE_ITEM_BITCOIN, new ItemHolderBitcoin(getActivity(),this));

		//??????????? ?????? ????????
        adapter.setHasStableIds(true);

        recyclerview.setAdapter(adapter);

		setHasOptionsMenu(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().show();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			//?????? ??????? ??? ? ??? ????
			getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.color_primary_dark));
		}

        return view;
    }

    @Override
	public void onStart(){
    	super.onStart();

    	//?????? ????????? ? ??????????? "????????"
		downloadListing();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_demo, menu);

		//?????? "Reload" ? ????, ????? ??? ????????? ????????? ? ?????????? ??????
		menu_itme_reload = menu.findItem(R.id.menu_reload);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
			case R.id.menu_reload:

				downloadListing();//????????? ????????? ????????, ? ?????????? ??????

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {

    	//????? ?? ????? ???? ?? ???????? ??????
		try {

			//??? ??? data-source ????????, ? ????? ??????, ???????? Cursor ?? ????????? ???????
			int clicked_item_position=(Integer)v.getTag(v.getId());
			adapter.getCursor().moveToPosition(clicked_item_position);

			//getValue - ??????????? ??????? ??? ???????????? ???????? ??????? value ?? Cursor
			JSONObject json=new JSONObject(CursorMultipleTypesAdapter.getValue(adapter.getCursor()));

			showDialogDetails(json);//?????????? ?????? ? ???????????, ?? ???????? ?????? ????????

		} catch (JSONException e) {
			e.printStackTrace();
		}


	}

//------------Preparing cursor----------------------------


    protected MatrixCursor getCursorForAdapter(JSONArray data) throws JSONException{
		//????? ?? ???????? ???????? JSONArray ? Cursor, ??? ?????????? ???????? ? ???????

    	MatrixCursor matrixcursor=new MatrixCursor(new String[]{adapter.COLUMN_ID, adapter.COLUMN_TYPE, adapter.COLUMN_KEY, adapter.COLUMN_VALUE});    	
    	
    	JSONObject json;    	

		for(int i=0;i<data.length();i++) {
			json=data.getJSONObject(i);
			matrixcursor.addRow(new Object[]{i, TYPE_ITEM_BITCOIN, i, json.toString()});
		}

		return matrixcursor;
    	
    }

//------------Network Methods--------------------------------

	public void downloadListing(){

		//????? ???????????? ?????? ?????? https://github.com/igorpi25/connection
		Connection.protocolConnection(getActivity(), getFragmentManager(),R.id.main_container, new Connection.ProtocolListener(){
			@Override
			public void isCompleted() {
				//??? ??????? ????????? ?????????. ???????? ????????
				doGetListingRequest(getActivity(),url,getFragmentManager(),R.id.main_container);
			}

			@Override
			public void onCanceled() {
				//???????????? ????? "Cancel". ????????? ???? ? ??? ?????? ??????(?.?. ??????? ?? ???? ??? ?? ??????????)
				if(adapter.getCursor()==null)
					getActivity().finish();
			}
		});
	}

	public void doGetListingRequest(final Context context, final String url, final FragmentManager fragmentManager, final int container) {

		//??????? ?????????? ? ????? "Loading"
		if(menu_itme_reload !=null)
			menu_itme_reload.setVisible(false);
		showProgressBar();

		StringRequest stringrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

				Log.d(TAG, "doRequestToServer onResponse response=");

				try {
					JSONObject json=new JSONObject(response);
					JSONArray data=json.getJSONArray("data");

					adapter.changeCursor(getCursorForAdapter(data));


				} catch (JSONException e) {
					e.printStackTrace();
				}

				//????????????? ????????? ??????????
				if(menu_itme_reload !=null)
					menu_itme_reload.setVisible(true);
				hideProgressBar();

			}

		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG, "doRequestToServer onErrorResponse error=" + error.toString());

				//????????????? ????????? ??????????
				if(menu_itme_reload !=null)
					menu_itme_reload.setVisible(true);
				hideProgressBar();
			}
		});


		String tag_stringrequest ="doRequestToServer_"+url;

		stringrequest.setTag(tag_stringrequest);
		Volley.newRequestQueue(context.getApplicationContext()).add(stringrequest);

	}

//--------------UI-----------------

	public void showProgressBar(){
		getActivity().findViewById(R.id.progressbar_layout).setVisibility(View.VISIBLE);
	}

	public void hideProgressBar(){
		getActivity().findViewById(R.id.progressbar_layout).setVisibility(View.GONE);
	}

	public void showDialogDetails(JSONObject json){
		//?????? ? ??????? ???????? ???????? ??????

		FragmentDetails f=FragmentDetails.newInstance(json);

		f.setStyle(STYLE_NO_TITLE,0);
		f.show(getFragmentManager(),"FragmentDetails");

	}
}