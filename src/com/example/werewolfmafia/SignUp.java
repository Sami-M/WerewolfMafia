package com.example.werewolfmafia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Credentials;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends Activity {
	String name, email, password, confirm;
	boolean resultStatus = false;
	String semail, sname, spassword_digest, sremember_token;
	int suser_id, sgame_id, splayer_id;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		Button SignUpButton = (Button) findViewById(R.id.button1);
		Button SignInButton = (Button) findViewById(R.id.button2);
		

		SignUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				
				EditText Ename = (EditText) findViewById(R.id.editText1);
				EditText Eemail = (EditText) findViewById(R.id.editText2);
				EditText Epassword = (EditText) findViewById(R.id.editText3);
				EditText Econfirm = (EditText) findViewById(R.id.editText4);
				name = Ename.getText().toString();
				email = Eemail.getText().toString();
				password = Epassword.getText().toString();
				confirm = Econfirm.getText().toString();
				
				new MyAsyncTask().execute(name, email, password, confirm);
				
		

			}
		});
		

		SignInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent goToNextActivity = new Intent(getApplicationContext(),
						SignIn.class);
				startActivity(goToNextActivity);
			}
		});

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sign_up, menu);
		return true;
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, Double> {

		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(params[0], params[1], params[2], params[3]);
			return null;
		}

		protected void onPostExecute(Double result) {
			if (resultStatus == true){
				Toast.makeText(getApplicationContext(), "Registered! Go Log In!",
						Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "Try again",
						Toast.LENGTH_LONG).show();
			}
			
		}

		public Boolean postData(String name, String email, String password,
				String confirm) {
			// Create a new HttpClient and Post Header

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("https://werewolf-mafia.herokuapp.com/users.json");
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");
			
		    
			JSONObject object = new JSONObject();
			try {
				object.put("name", name);
				object.put("email", email);
				object.put("password", password);
				//object.put("user[password_confirmation", confirm);
				//object.put("commit", "Create my account");
				
				String jsonstring = object.toString();
				Log.d("Status Line","Json=" + jsonstring);
				StringEntity data = new StringEntity(jsonstring);
				httppost.setEntity(data);
				HttpResponse resp = httpclient.execute(httppost);
				if (resp != null) {
					if (resp.getStatusLine().getStatusCode() == 204) {
						resultStatus = true;
					}
					if (resp.getStatusLine().getStatusCode() == 201) {
						resultStatus = true;
					}
				}
				Log.d("Status Line", "" + resp.getStatusLine().getStatusCode());
				BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
				StringWriter sw = new StringWriter();
				char[] buffer = new char[1024 * 4];
				int n = 0;
				while (-1 != (n = rd.read(buffer))) {
					sw.write(buffer, 0, n);
				}
				String resultString = sw.toString();
				Log.d("Status Line", resultString);
				JSONObject jsonResponse=new JSONObject(resultString);
				semail=jsonResponse.getString("email");
				suser_id=Integer.parseInt(jsonResponse.getString("id"));
				sname=jsonResponse.getString("name");
				spassword_digest=jsonResponse.getString("password_digest");
				splayer_id=-1;
				sremember_token=jsonResponse.getString("remember_token");
				
				savePrefs(semail, semail, spassword_digest, sremember_token, sgame_id, splayer_id, suser_id);

				
				

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultStatus;

		}

	}

}
