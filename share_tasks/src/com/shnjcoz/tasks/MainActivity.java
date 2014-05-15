package com.shnjcoz.tasks;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import android.util.Log;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {
	private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

	private String TAG = "MainActivity";		
			
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
	}

	protected void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if(state.isOpened()){
			Log.i(TAG,"Logged in ...");
		} else if(state.isClosed()){
			Log.i(TAG,"Logged out ...");			
		}
	}
	@Override
	public void onResume(){
		super.onResume();
		uiHelper.onResume();
	}
	@Override
	public void onPause(){
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		uiHelper.onDestroy();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

}
