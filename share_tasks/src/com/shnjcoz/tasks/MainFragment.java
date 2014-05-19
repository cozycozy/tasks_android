package com.shnjcoz.tasks;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {
	
	private String TAG = "MainFragment";	
	private UiLifecycleHelper uiHelper;
	private TextView userInfoTextView;
	final AsyncHttpClient client = new AsyncHttpClient();
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		Log.i(TAG,"onCreate...desu");
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState){
		
		Log.i(TAG,"onCreateView...desu");
		View view = inflater.inflate(R.layout.activity_main, container,false);
		LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
		userInfoTextView = (TextView) view.findViewById(R.id.userInfoTextView);
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes"));
		return view;			
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception ){
		if(state.isOpened()){
			userInfoTextView.setVisibility(View.VISIBLE);
			Log.i(TAG,"Logged in...desu");
			Request.newMeRequest(session, new Request.GraphUserCallback() {				
				@Override
				public void onCompleted(GraphUser user, Response response) {
					Log.i(TAG,"onCompleted...desu");
					if(user != null){
						userInfoTextView.setText(buildUserInfoDisplay(user));
					}
				}
			}).executeAsync();
			client.get("http://shnjcoz.com/sample.php", new AsyncHttpResponseHandler(){
				@Override
				public void onSuccess(String response){
					Log.i(TAG,"onSuccess");
					Log.v(TAG,response);
				}
				@Override
				public void onFinish(){
					Log.i(TAG,"onFinish");					
				}
				
				@Override
				public void onStart(){
					Log.i(TAG,"onStart");
				}
			});
			
		}else if(state.isClosed()){
			userInfoTextView.setVisibility(View.INVISIBLE);
			Log.i(TAG,"Logged out...");
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
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
	
	private String buildUserInfoDisplay(GraphUser user){
		Log.i(TAG,"buildUserInfoDisplay...desu");
		StringBuffer userInfo = new StringBuffer();
		
		userInfo.append(String.format("Name: %S\n\n", user.getName()));
		userInfo.append(String.format("Birthday: %S\n\n", user.getBirthday()));
		userInfo.append(String.format("Location: %s\n\n", 
		        user.getLocation().getProperty("name")));
		userInfo.append(String.format("Locale: %s\n\n", 
		        user.getProperty("locale")));
		JSONArray languages = (JSONArray)user.getProperty("languages");

		Log.i(TAG,"userInfo1: " + userInfo.toString());

		if(languages.length() > 0){
			ArrayList<String> languageNames = new ArrayList<String>();
			for(int i = 0; i < languages.length(); i++ ){
				JSONObject language = languages.optJSONObject(i);
				languageNames.add(language.optString("name"));
			}
			userInfo.append(String.format("Languages: %s\n\n", languageNames.toString()));
		}
		Log.i(TAG,"userInfo2: " + userInfo.toString());
		return userInfo.toString();		
	}
}
