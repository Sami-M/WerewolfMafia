package com.example.werewolfmafia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignIn extends Activity {
	private EditText value1, value2;
	private Button signin;
	String email, password;
	String semail, sname, spassword_digest, sremember_token;
	int suser_id, sgame_id, splayer_id;
	private String s1,s2,s3,s4;
	private Integer s5,s6,s7;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		signin = (Button) findViewById(R.id.button1);

		signin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				value1 = (EditText) findViewById(R.id.editText1);
				value2 = (EditText) findViewById(R.id.editText1);
				email = value1.toString();
				password = value2.toString();

				if ((email.length() < 1) || (password.length() < 1)) {
					// out of range
					Toast.makeText(getApplicationContext(),
							"please enter something", Toast.LENGTH_LONG).show();
				}
				else{
					getPrefs();

					new MyAsyncTask().execute(s1, s2, s3, s4, String.valueOf(s5), String.valueOf(s6), String.valueOf(s7));
				}
			}
		});
	}
	
	void getPrefs(){
		SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		semail = myPrefs.getString("myEmail", "NO EMAIL");
		sname = myPrefs.getString("myName", "No NAME");
		spassword_digest = myPrefs.getString("myPasswordDigest", "NO PASSWORD DIGEST");
		sremember_token = myPrefs.getString("myRemember_token", "NO REMEMBER TOKEN");
		suser_id = myPrefs.getInt("myUserId",-1);
		sgame_id = myPrefs.getInt("myGameId", -1);
		splayer_id = myPrefs.getInt("myPlayerId", -1);
		Log.d("Get Perfs", semail+"|"+sname+"|"+spassword_digest+"|"+sremember_token+"|"+String.valueOf(suser_id)+"|"+String.valueOf(sgame_id)+"|"+String.valueOf(splayer_id)+"|");
		this.s1=semail;
		this.s2=sname;
		this.s3=spassword_digest;
		this.s4=sremember_token;
		this.s5=suser_id;
		this.s6=sgame_id;
		this.s7=splayer_id;
	}
	
	private void savePrefs(String email, String name, String password_digest, String remember_token, Integer user_id, Integer game_id, Integer player_id) {
		SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putString("myEmail", email);
		prefsEditor.putString("myName", name);
		prefsEditor.putString("myPasswordDigest", password_digest);
		prefsEditor.putString("myRemember_token", remember_token);
		prefsEditor.putInt("myUserId", user_id);
		prefsEditor.putInt("myGameId", game_id);
		prefsEditor.putInt("myPlayerId", player_id);
		Log.d("Save Perfs", email+"|"+name+"|"+password_digest+"|"+remember_token+"|"+String.valueOf(user_id)+"|"+String.valueOf(game_id)+"|"+String.valueOf(player_id)+"|");
		prefsEditor.commit();
		
		//String semail, sname, spassword_digest, sremember_token;
		//int suser_id, sgame_id, splayer_id;
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, Double> {

		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(params[0], params[1], params[2], params[3], params[4], params[5], params[6]);
			return null;
		}

		protected void onPostExecute(Double result) {
			Toast.makeText(getApplicationContext(), "command sent",
					Toast.LENGTH_LONG).show();
			
					Intent goToNextActivity = new Intent(getApplicationContext(),MainMenu.class);
					startActivity(goToNextActivity);
		}

		public void postData(String t1, String t2, String t3, String t4, String t5, String t6, String t7) {
			HttpClient httpclient = new DefaultHttpClient();
			String url = "https://werewolf-mafia.herokuapp.com/users/";
			String url2 = url + t7 + ".json";
			HttpGet httpget = new HttpGet(url2);
			Log.d("url2", url2);
			

			Log.d("PostData Perfs", t1+"|"+t2+"|"+t3+"|"+t4+"|"+t5+"|"+t6+"|"+t7+"|");
			
			



				// Execute HTTP Get Request
				HttpResponse resp;

				try {
					resp = httpclient.execute(httpget);
					BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
					StringWriter sw = new StringWriter();
					char[] buffer = new char[1024 * 4];
					int n = 0;
					while (-1 != (n = rd.read(buffer))) {
						sw.write(buffer, 0, n);
					}
					String resultString = sw.toString();
					Log.d("Sign In", resultString);
					JSONObject jsonResponse=new JSONObject(resultString);
					
					String email=jsonResponse.getString("email");
					Integer id=Integer.parseInt(jsonResponse.getString("id"));
					String name =jsonResponse.getString("name");
					String password_digest =jsonResponse.getString("password_digest");
					splayer_id=-1;
					sremember_token=jsonResponse.getString("remember_token");
					
					//savePrefs(semail, semail, spassword_digest, sremember_token, sgame_id, splayer_id, suser_id);
					
					//semail=jsonResponse.getString("email");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				;
			
		}


	}
}