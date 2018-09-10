package com.ivanov.tech.multipletypesadapter.demo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivanov.tech.multipletypesadapter.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentDetails extends DialogFragment{

	private static final String TAG = FragmentDetails.class.getSimpleName();

	JSONObject json;

    //json: {id,name,symbol,website_slug}
    public static FragmentDetails newInstance(JSONObject json) {

    	FragmentDetails f = new FragmentDetails();
        f.json=json;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	View view = null;
        view = inflater.inflate(R.layout.fragment_details, container, false);

        try {
            ((TextView)view.findViewById(R.id.details_name)).setText(json.getString("name"));
            ((TextView)view.findViewById(R.id.details_symbol)).setText("Symbol: " + json.getString("symbol"));
            ((TextView)view.findViewById(R.id.details_id)).setText("ID: "+json.getString("id"));
            ((TextView)view.findViewById(R.id.details_website_slug)).setText("Website Slug: ["+json.getString("website_slug")+"]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(false);

        return view;
    }

}