package com.example.askme;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void ask(View view) {
    	Log.d("ask", "Entr�e dans la m�thode ask");
		EditText champQuestion  = (EditText) findViewById(R.id.question);
		String strQuestion = champQuestion.getText().toString();
		Log.d("ask", "Question re�ue via la m�thode ask : " + strQuestion);
		if(strQuestion.length() < 1) {
			
		}
    }
}
