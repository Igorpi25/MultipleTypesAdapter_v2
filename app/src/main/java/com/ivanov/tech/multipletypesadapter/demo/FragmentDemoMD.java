package com.ivanov.tech.multipletypesadapter.demo;

import java.util.ArrayList;
import java.util.List;

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
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderCardOrderItem;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderCardOrderString;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderCardPreview;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderCardProduct;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderCardProductShoppingCart;
import com.ivanov.tech.multipletypesadapter.R;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.Toast;

import static com.ivanov.tech.connection.Connection.createFragmentNoServerResponding;

public class FragmentDemoMD extends DialogFragment implements OnClickListener{
	
	private static final String TAG = FragmentDemoMD.class.getSimpleName();    

	String url="https://api.coinmarketcap.com/v2/listings/";
	
	protected static final int TYPE_ITEM_BITCOIN = 0;

	RecyclerView recyclerview;
    
    protected BitcoinAdapter adapter=null;
    
    public static FragmentDemoMD newInstance() {
    	FragmentDemoMD f = new FragmentDemoMD();        
       
        return f;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View view = null;
        view = inflater.inflate(R.layout.fragment_demo_rv, container, false);
        
        recyclerview=(RecyclerView)view.findViewById(R.id.fragment_demo_rv_recyclerview);

		LinearLayoutManager layoutmanager=new LinearLayoutManager(recyclerview.getContext());
		layoutmanager.setInitialPrefetchItemCount(100);
		layoutmanager.setItemPrefetchEnabled(true);

        recyclerview.setLayoutManager(layoutmanager);

		recyclerview.setHasFixedSize(true);

//		recyclerview.setItemViewCacheSize(100);
//		recyclerview.setDrawingCacheEnabled(true);
//		recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);



        
        adapter=new BitcoinAdapter(getActivity(),null);
      
        //Prepare map of types and set listeners for them. There are different ways in which you can define ItemHolder      
        adapter.addItemHolder(TYPE_ITEM_BITCOIN, new ItemHolderBitcoin(getActivity(),this));
		adapter.setHasStableIds(true);

        recyclerview.setAdapter(adapter);

		downloadListing();

        return view;
    }

//------------Preparing cursor----------------------------
	

    protected MatrixCursor getCursorForAdapter(JSONArray data) throws JSONException{

    	MatrixCursor matrixcursor=new MatrixCursor(new String[]{adapter.COLUMN_ID, adapter.COLUMN_TYPE, adapter.COLUMN_KEY, adapter.COLUMN_VALUE});    	
    	
    	JSONObject json;    	

    	//---------Card Preview ------------


		for(int i=0;i<data.length();i++) {
			json=data.getJSONObject(i);
			matrixcursor.addRow(new Object[]{i, TYPE_ITEM_BITCOIN, i, json.toString()});
		}

		return matrixcursor;
    	
    }
    
//--------------Adapter Callbacks----------------------
     
	@Override
	public void onClick(View v) {
				
			
		toast("clicked key="+v.getTag(v.getId()));
			
	}
	
//-----------------------Utilites--------------------------
	
	private void toast(String msg){
		Log.d(TAG, msg);
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	private void downloadListing(){
		Connection.protocolConnection(getActivity(), getFragmentManager(),R.id.main_container, new Connection.ProtocolListener(){
			@Override
			public void isCompleted() {
				//??? ??????? ????????? ?????????. ???????? ????????

				doGetListingRequest(getActivity(),url,getFragmentManager(),R.id.main_container);

			}

			@Override
			public void onCanceled() {
				//???????????? ????? "Cancel"

			}
		});
	}

	public void doGetListingRequest(final Context context, final String url, final FragmentManager fragmentManager, final int container) {

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

			}

		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG, "doRequestToServer onErrorResponse error=" + error.toString());


			}
		});


		String tag_stringrequest ="doRequestToServer_"+url;

		stringrequest.setTag(tag_stringrequest);
		Volley.newRequestQueue(context.getApplicationContext()).add(stringrequest);

	}

	class BitcoinAdapter extends CursorMultipleTypesAdapter implements SectionIndexer{

		private ArrayList<Integer> mSectionPositions;


		public BitcoinAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public Object[] getSections() {
			List<String> sections = new ArrayList<>(26);
			mSectionPositions = new ArrayList<>(26);
			for (int i = 0, size = 1000; i < size; i++) {

				String section = String.valueOf(i);

				if (!sections.contains(section)) {
					sections.add(section);
					mSectionPositions.add(i);
				}
			}
			return sections.toArray(new String[0]);
		}

		@Override
		public int getPositionForSection(int sectionIndex) {
			return mSectionPositions.get(sectionIndex);
		}

		@Override
		public int getSectionForPosition(int position) {
			return position;
		}
	}
}