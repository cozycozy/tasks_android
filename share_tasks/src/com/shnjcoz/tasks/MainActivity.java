package com.shnjcoz.tasks;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

	private MainFragment mainFragment;
	private String TAG = "MainActivity";	
				
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		if(savedInstanceState == null){
			Log.i(TAG,"onCreate ...desu");
			mainFragment = new MainFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(android.R.id.content, mainFragment)
			.commit();
			
		} else {
			Log.i(TAG,"onCreate else...desu");
			mainFragment = (MainFragment) getSupportFragmentManager()
							.findFragmentById(android.R.id.content);
		}
	}
}
