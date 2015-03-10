package com.example.askme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

public class ResultActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		Log.d("ask reponse", "Entr�e dans OnCreate ResultActivity");
		Intent intent = getIntent();
		String reponse = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		Log.d("ask reponse", "Reponse re�ue : " + reponse);
		TextView texteReponse = (TextView) findViewById(R.id.reponse);

		texteReponse.setText("R�ponse : " + reponse);
	}
}
