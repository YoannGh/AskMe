package com.example.askme;

import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "cleTransferReponse";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("ask", "Entrée onCreate");

	}

	public void ask(View v) {
		Log.d("ask", "Entrée dans la méthode ask");
		EditText champQuestion = (EditText) findViewById(R.id.question);
		String strQuestion = champQuestion.getText().toString();
		Log.d("ask", "Question reçue via la méthode ask : " + strQuestion);
		
		if (strQuestion.length() < 1) {
			Log.d("ask", "Question vide");
		}
		
		new WolframQuery().execute(strQuestion);
			
	}
	
	/* AsyncTask to get response from Wolfram API. */
	private class WolframQuery extends AsyncTask<String, Void, String> {
		
		private static final String FORMAT = "plaintext";
		private static final String appid = "W82RQ4-VW5975UY64";

		@Override
		protected String doInBackground(String... params) {
			/*
			 * Go online to > http://products.wolframalpha.com/developers/ and
			 * sign up for an app key. Once you have an app key, create a string
			 * resource with the id wolfram_key and the key for text.
			 */
			final WAEngine engine = new WAEngine();
			engine.setAppID(appid);
			engine.addFormat(FORMAT);
			final WAQuery query = engine.createQuery();
			query.setInput(params[0]);
			try {
				final WAQueryResult queryResult = engine.performQuery(query);
				if (queryResult.isError()) {
					return queryResult.getErrorMessage();
				} else if (!queryResult.isSuccess()) {
					return Arrays.asList(
							getResources().getStringArray(
									R.array.wolfram_bad_query)).get(
							new Random().nextInt(getResources().getStringArray(
									R.array.wolfram_bad_query).length));
				} else {
					/*
					 * Very hacky way to get Wolfram API response. Needs
					 * optimization.
					 */
					for (int i = 0; i < queryResult.getNumPods(); i++) {
						if (i == 2) {
							final Object element = queryResult.getPods()[1]
									.getSubpods()[0].getContents()[0];
							return (((WAPlainText) element).getText());
						}
					}
				}
			} catch (WAException exception) {
				Log.d("ask", exception.toString());
			}
			return Arrays.asList(
					getResources().getStringArray(R.array.wolfram_no_find))
					.get(new Random().nextInt(getResources().getStringArray(
							R.array.wolfram_no_find).length));
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("ask", "Réponse onPostExecute : " + result);
			showResult(result);
		}
	}
	
	private void showResult(String result) {
		Intent intent = new Intent(this, ResultActivity.class);
		Log.d("ask", "Réponse showResult : " + result);
		intent.putExtra(EXTRA_MESSAGE, result);
		startActivity(intent);
	}
	
}
