package com.example.werewolfmafia;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			synchronized (this) {
				wait(4000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.d("MainActivity", "Waiting didnt work!!");
			e.printStackTrace();
		}
		
		Intent goToNextActivity = new Intent(getApplicationContext(), SignUp.class);
		startActivity(goToNextActivity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
