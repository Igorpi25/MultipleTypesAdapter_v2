package com.ivanov.tech.multipletypesadapter.demo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.CursorMultipleTypesAdapter;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderBitcoin;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderCardOrderItem;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderCardOrderString;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderCardPreview;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderCardProduct;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderCardProductShoppingCart;
import com.ivanov.tech.multipletypesadapter.R;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentDemoMD extends DialogFragment implements OnClickListener{
	
	private static final String TAG = FragmentDemoMD.class.getSimpleName();    
    
	
	protected static final int TYPE_ITEM_BITCOIN = 0;

    protected RecyclerView recyclerview;
    
    protected CursorMultipleTypesAdapter adapter=null;
    
    public static FragmentDemoMD newInstance() {
    	FragmentDemoMD f = new FragmentDemoMD();        
       
        return f;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View view = null;
        view = inflater.inflate(R.layout.fragment_demo_rv, container, false);
        
        recyclerview=(RecyclerView)view.findViewById(R.id.fragment_demo_rv_recyclerview);
        
        recyclerview.setLayoutManager(new LinearLayoutManager(recyclerview.getContext()));
        
        adapter=new CursorMultipleTypesAdapter(getActivity(),null);
      
        //Prepare map of types and set listeners for them. There are different ways in which you can define ItemHolder      
        adapter.addItemHolder(TYPE_ITEM_BITCOIN, new ItemHolderBitcoin(getActivity(),this));

        recyclerview.setAdapter(adapter);
        
        adapter.changeCursor(createMergeCursor());
        
    
        return view;
    }
    
//------------Preparing cursor----------------------------
	
    //Merge cursor for testing the huge number of items
	protected Cursor createMergeCursor(){
    	
    	List<Cursor> cursors_list=new ArrayList<Cursor>();	
    	
    	int _id=1;
    	try{
    	//You can test with huge number of items. Just set i=100
    	for(int i=0;i<1;i++){
    			Log.d(TAG,"i="+i);
	    		cursors_list.add(getCursorForAdapter(_id));
    	}
    	
    	}catch(JSONException e){
    		Log.e(TAG, "createMergeCursor JSONException e="+e);
    	}
    	    	
    	Cursor[] cursors_array=new Cursor[cursors_list.size()];
    	MergeCursor mergecursor=new MergeCursor(cursors_list.toArray(cursors_array));
    	
    	return mergecursor;    	
    }
    
	
    protected MatrixCursor getCursorForAdapter(int _id) throws JSONException{

    	MatrixCursor matrixcursor=new MatrixCursor(new String[]{adapter.COLUMN_ID, adapter.COLUMN_TYPE, adapter.COLUMN_KEY, adapter.COLUMN_VALUE});    	
    	
    	JSONObject json;    	

    	//---------Card Preview ------------


		json=new JSONObject("{ id:1, name:'Bitcoin', symbol:'BTC', website_slug:'bitcoin' }");
		matrixcursor.addRow(new Object[]{++_id,TYPE_ITEM_BITCOIN,_id,json.toString()});

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

}