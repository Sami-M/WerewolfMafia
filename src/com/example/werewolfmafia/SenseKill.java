package com.example.werewolfmafia;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SenseKill extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sense_kill);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sense_kill, menu);
		return true;
	}

}
