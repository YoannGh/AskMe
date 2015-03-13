package fr.y0koz.askme;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {


	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	public static final String EXTRA_MESSAGE = "cleTransferReponse";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("ask", "Entrée onCreate");

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);


		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public void ask(View v) {
		Log.d("ask", "Entrée dans la méthode ask");
		EditText champQuestion = (EditText) findViewById(R.id.question);
		String strQuestion = champQuestion.getText().toString();
		Log.d("ask", "Question reçue via la méthode ask : " + strQuestion);

		if (strQuestion.length() < 1) {
			Log.d("ask", "Question vide");
		}

		new WolframQuery(this).execute(strQuestion);
	}

	/* Partie du code relative au tabs */

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			switch (position) {
			case 1:
				return VocalFragment.newInstance(position + 1);
			case 2:
				return TexteFragment.newInstance(position + 1);
			default:
				// TODO : Historique
				return PlaceholderFragment.newInstance(position + 1);
			}
		}

		@Override
		public int getCount() {
			return 3; // nb de tab
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/* Tab vide (TODO : Historique) */

	public static class PlaceholderFragment extends Fragment {

		private static final String ARG_SECTION_NUMBER = "section_number";

		private String text;
		
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
			text = new WolframQuery(this.getActivity()).getHistoritque();
			TextView textView = (TextView) getActivity().findViewById(R.id.historique);
			textView.setText(text);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}


	/* Tab : Texte */

	public static class TexteFragment extends Fragment {

		private static final String ARG_SECTION_NUMBER = "section_number";

		public static TexteFragment newInstance(int sectionNumber) {
			TexteFragment fragment = new TexteFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public TexteFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_texte,
					container, false);

			return rootView;
		}

	}

	/* Tab : Vocal */

	public static class VocalFragment extends Fragment implements
			RecognitionListener {

		private static final String ARG_SECTION_NUMBER = "section_number";

		private ImageButton iconeVocale;
		private boolean screenState = false;
		private TextView reconnaissanceText;
		private SpeechRecognizer speechReco = null;
		private Intent recognizerIntent;

		public static VocalFragment newInstance(int sectionNumber) {
			VocalFragment fragment = new VocalFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public VocalFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_vocal,
					container, false);

			iconeVocale = (ImageButton) rootView.findViewById(R.id.iconeVocale);
			reconnaissanceText = (TextView) rootView
					.findViewById(R.id.reconnaissance);

			speechReco = SpeechRecognizer.createSpeechRecognizer(getActivity());
			speechReco.setRecognitionListener(this);
			recognizerIntent = new Intent(
					RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			recognizerIntent.putExtra(
					RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
			recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			
			/*
			 * recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
			 * RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
			 */

			iconeVocale.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					changeState(v);
				}
			});

			return rootView;
		}

		public void changeState(View v) {
			Log.d("vocal", "Entrée dans changeState");
			if (!screenState) {
				Log.d("vocal", "start listening");
				speechReco.startListening(recognizerIntent);
				screenState = true;
				iconeVocale.setBackgroundResource(R.drawable.red);
			} else {
				Log.d("vocal", "stop listening");
				speechReco.stopListening();
				screenState = false;
				iconeVocale.setBackgroundResource(R.drawable.grey);
			}
		}

		@Override
		public void onResume() {
			super.onResume();
		}

		@Override
		public void onBeginningOfSpeech() {
			Log.i("vocal", "onBeginningOfSpeech");
		}

		@Override
		public void onBufferReceived(byte[] buffer) {
			Log.i("vocal", "onBufferReceived: " + buffer);
		}

		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
		@Override
		public void onEndOfSpeech() {
			Log.i("vocal", "onEndOfSpeech");
			iconeVocale.callOnClick();
		}

		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
		@Override
		public void onError(int errorCode) {
			String errorMessage = getErrorText(errorCode);
			Log.d("vocal", "FAILED " + errorMessage);
			reconnaissanceText.setText(errorMessage);
			iconeVocale.callOnClick();
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
			Log.i("vocal", "onEvent");
		}

		@Override
		public void onPartialResults(Bundle arg0) {
			Log.i("vocal", "onPartialResults");
		}

		@Override
		public void onReadyForSpeech(Bundle arg0) {
			Log.i("vocal", "onReadyForSpeech");
		}

		@Override
		public void onResults(Bundle results) {
			Log.i("vocal", "onResults");
			ArrayList<String> matches = results
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			String text = "";
			for (String result : matches)
				text += result + "\n";

			Log.i("vocal", " text entendu : " + text);
			reconnaissanceText.setText(text);

			new WolframQuery(getActivity()).execute(text);

		}

		@Override
		public void onRmsChanged(float rmsdB) {
			Log.i("vocal", "onRmsChanged: " + rmsdB);
		}

		public static String getErrorText(int errorCode) {
			String message;
			switch (errorCode) {
			case SpeechRecognizer.ERROR_AUDIO:
				message = "Problème d'enregistrement audio";
				break;
			case SpeechRecognizer.ERROR_CLIENT:
				message = "Problème client";
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
				message = "Permissions insufisantes";
				break;
			case SpeechRecognizer.ERROR_NETWORK:
				message = "Erreur réseau";
				break;
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
				message = "Délai d'attente dépassé";
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:
				message = "Aucune correspondance";
				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
				message = "Service de reconnaissance vocal surchargé";
				break;
			case SpeechRecognizer.ERROR_SERVER:
				message = "Erreur du serveur";
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
				message = "Aucun son capté";
				break;
			default:
				message = "Veuillez réessayer!";
				break;
			}
			return message;
		}

	}

}
