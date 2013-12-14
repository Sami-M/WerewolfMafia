package com.example.werewolfmafia;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}

	Button btnShowLocation;
	TextView tv1,tv2,tv3,tv4;

	// GPSTracker class
	GPSReadingService gps;

	private String s1,s2,s3,s4;
	private Integer s5,s6,s7;
	
	void getPrefs(){
		SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		String semail = myPrefs.getString("myEmail", "NO EMAIL");
		String sname = myPrefs.getString("myName", "No NAME");
		String spassword_digest = myPrefs.getString("myPasswordDigest", "NO PASSWORD DIGEST");
		String sremember_token = myPrefs.getString("myRemember_token", "NO REMEMBER TOKEN");
		int suser_id = myPrefs.getInt("myUserId",-1);
		int sgame_id = myPrefs.getInt("myGameId", -1);
		int splayer_id = myPrefs.getInt("myPlayerId", -1);
		Log.d("MainMenu Perfs", semail+"|"+sname+"|"+spassword_digest+"|"+sremember_token+"|"+String.valueOf(suser_id)+"|"+String.valueOf(sgame_id)+"|"+String.valueOf(splayer_id)+"|");
		this.s1=semail;
		this.s2=sname;
		this.s3=spassword_digest;
		this.s4=sremember_token;
		this.s5=suser_id;
		this.s6=sgame_id;
		this.s7=splayer_id;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		getPrefs();

		btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
		Log.d("B4 TV Perfs", this.s1+"|"+this.s2+"|"+this.s3+"|"+this.s4+"|"+String.valueOf(this.s5)+"|"+String.valueOf(this.s6)+"|"+String.valueOf(this.s7)+"|");

		tv1 = (TextView) findViewById(R.id.tv1);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv1.setText("Email:" + this.s1);
		tv3.setText("Score:" + this.s5);
		tv4.setText(String.valueOf(this.s7));

		// show location button click event
		btnShowLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// create class object

				gps = new GPSReadingService(MainMenu.this);

				// check if GPS enabled
				if (gps.canGetLocation()) {

					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();

					// \n is for new line
					Toast.makeText(
							getApplicationContext(),
							"Your Location is - \nLat: " + latitude
									+ "\nLong: " + longitude, Toast.LENGTH_LONG)
							.show();
					HttpClient httpclient = new DefaultHttpClient();
					String url = "https://werewolf-mafia.herokuapp.com/position/"+ String.valueOf(latitude) + "/"+String.valueOf(longitude * -1) + "/" + tv4.getText();
					HttpGet httpget = new HttpGet(url);
					Log.d("urlPosition", url);

					try {
						httpclient.execute(httpget);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}

			}
		});
		
	}

}
