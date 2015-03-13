package fr.y0koz.askme;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;

/* AsyncTask to get response from Wolfram API. */
public class WolframQuery extends AsyncTask<String, Void, String> {

	private static final String FORMAT = "plaintext";
	private static final String appid = "GetYourOwn"; // xHJmsffnTy1v1KZn1uvw2qd rc4 date de naissance

	private static String filename = "historique";
	
	private FileWriter filewriter;
	private String question;
	private Activity caller;

	public WolframQuery(Activity caller) {
		this.caller = caller;
	}

	@Override
	protected String doInBackground(String... params) {

		final WAEngine engine = new WAEngine();
		engine.setAppID(appid);
		engine.addFormat(FORMAT);
		final WAQuery query = engine.createQuery();
		query.setInput(params[0]);
		
		question = params[0];
		
		try {
			final WAQueryResult queryResult = engine.performQuery(query);
			if (queryResult.isError()) {
				return queryResult.getErrorMessage();
			} else if (!queryResult.isSuccess()) {
				return Arrays.asList(
						caller.getResources().getStringArray(
								R.array.wolfram_bad_query)).get(
						new Random()
								.nextInt(caller.getResources().getStringArray(
										R.array.wolfram_bad_query).length));
			} else {

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
				caller.getResources().getStringArray(R.array.wolfram_no_find))
				.get(new Random().nextInt(caller.getResources().getStringArray(
						R.array.wolfram_no_find).length));
	}

	@Override
	protected void onPostExecute(String result) {
		showResult(result);
	}

	private void showResult(String result) {
		super.onPostExecute(result);
		
		try {
			// Ouvre le fichier en ecrivant à la suite
			filewriter = new FileWriter(filename, true);
			filewriter.write(question + System.getProperty("line.separator") + result);
			// Ferme le fichier
			filewriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Intent intent = new Intent(caller, ResultActivity.class);
		intent.putExtra(MainActivity.EXTRA_MESSAGE, result);
		caller.startActivity(intent);
	}
	
	public String getHistoritque(){
		String ret = new String();
		BufferedReader in
		   = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			while(in.ready()){
				ret += in.readLine();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
