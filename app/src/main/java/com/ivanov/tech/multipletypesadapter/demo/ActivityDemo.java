package com.ivanov.tech.multipletypesadapter.demo;

import com.ivanov.tech.multipletypesadapter.R;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Igor on 15.01.15.
 */
public class ActivityDemo extends AppCompatActivity {

	private String TAG=ActivityDemo.class.getSimpleName();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo);

        showSplash();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                showDemo();
            }
        }, 4000);

    }

    public void showDemo(){
      	 FragmentManager fragmentManager = getSupportFragmentManager();
           fragmentManager.beginTransaction()
                   .replace(R.id.main_container, new FragmentDemo())
                   .commit();           
    }

    public void showSplash(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_container, new FragmentSplash())
                .commit();
    }



}
