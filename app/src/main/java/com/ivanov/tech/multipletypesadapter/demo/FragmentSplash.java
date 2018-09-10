package com.ivanov.tech.multipletypesadapter.demo;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ivanov.tech.connection.Connection;
import com.ivanov.tech.multipletypesadapter.R;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.CursorMultipleTypesAdapter;
import com.ivanov.tech.multipletypesadapter.cursoradapter_recyclerview.ItemHolderBitcoin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentSplash extends DialogFragment{

	private static final String TAG = FragmentSplash.class.getSimpleName();

    public static FragmentSplash newInstance() {
    	FragmentSplash f = new FragmentSplash();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	View view = null;
        view = inflater.inflate(R.layout.fragment_splash, container, false);

        //???????? ?????-??? ? ????? ??????
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //???????? ??????-??? ? ????? ?????? (?????? ?????? ??? ?????, ? ??? ?? ?????)
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.color_white));
        }

        return view;
    }

}