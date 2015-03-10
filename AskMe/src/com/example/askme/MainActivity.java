package com.example.askme;

import tools.Wolframe;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "cleTransferReponse";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("commit", "test");

	}

	public void ask(View v) {
		Log.d("ask", "Entrée dans la méthode ask");
		EditText champQuestion = (EditText) findViewById(R.id.question);
		String strQuestion = champQuestion.getText().toString();
		Log.d("ask", "Question reçue via la méthode ask : " + strQuestion);
		
		if (strQuestion.length() < 1) {
			Log.d("ask", "Question vide");
		}
		
		Wolframe.request(strQuestion);
		String reponse = Wolframe.getQueryResult();
		
		Intent intent = new Intent(this, ResultActivity.class);
		Log.d("ask", "Réponse : " + reponse);
		intent.putExtra(EXTRA_MESSAGE, reponse);
		startActivity(intent);
		
	}
	
	

}
