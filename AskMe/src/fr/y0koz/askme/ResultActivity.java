package fr.y0koz.askme;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

public class ResultActivity extends ActionBarActivity {

	private TextToSpeech TTS;
	private String reponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		Log.d("ask", "Entrée dans OnCreate ResultActivity");
		Intent intent = getIntent();
		reponse = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		Log.d("ask", "Reponse reçue ResultActivity : " + reponse);
		TextView texteReponse = (TextView) findViewById(R.id.reponse);
		texteReponse.setText("Réponse : " + reponse);
		TTS = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {
						if (status != TextToSpeech.ERROR) {
							TTS.setLanguage(Locale.UK);
						}
					}
				});

		if (reponse != "")
			TTS.speak(reponse, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onPause() {
		if (TTS != null) {
			TTS.stop();
			TTS.shutdown();
		}
		super.onPause();
	}

}
