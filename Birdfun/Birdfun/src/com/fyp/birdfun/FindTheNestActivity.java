package com.fyp.birdfun;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.birdfun.helpers.JSONParser;
import com.fyp.birdfun.helpers.PlayerDetails;

public class FindTheNestActivity extends Activity {
	int score = 0;
	int time = 0;
	CountDownTimer counter;
	int wrongInput = 0;

	Toast toast;
		
	
	// Parameters to update score once done to server side
	private static String url_score ="http://birdfun.net/update_score.php";
	 
	  JSONParser jsonParser = new JSONParser();
	  PlayerDetails currentPlayer;
	
	 // Progress Dialog
	 private ProgressDialog pDialog;
	 //Tags to update the score
	 private static final String TAG_SUCCESS = "success";
	 private static final String TAG_PID = "pid";
	 private static final String TAG_TOTAL = "total";
	 private static final String TAG_SAVETHEEGGS = "savetheeggs";
	 private static final String TAG_THEWEAPON= "theweapon";
	 private static final String TAG_FANTASTICFEATHERS= "fantasticfeathers";
	 Intent changeScreen;
	 
	// NFC declarations
	private static String TAG = FindTheNestActivity.class.getSimpleName();

	protected NfcAdapter nfcAdapter;
	protected PendingIntent nfcPendingIntent;

	private int[] birdArray = { R.drawable.ftn_burrow_1,
			R.drawable.ftn_burrow_2, R.drawable.ftn_burrow_3,
			R.drawable.ftn_cavity_1, R.drawable.ftn_cavity_2,
			R.drawable.ftn_cup_1, R.drawable.ftn_cup_2, R.drawable.ftn_mound_1,
			R.drawable.ftn_platform_1, };
	private int[] birdAnswer = { 0, 0, 0, 1, 1, 2, 2, 3, 6, };

	private int numCorrAns = 0;

	private int[] nestArray = {

	R.drawable.ftn_burrow, R.drawable.ftn_cavity, R.drawable.ftn_cup,
			R.drawable.ftn_mound, R.drawable.ftn_pendent, R.drawable.ftn_plate,
			R.drawable.ftn_platform, R.drawable.ftn_scrape,
			R.drawable.ftn_sphere, };

	private int answerIndex = -1;
	private int[] indexObjects = { 0, 0, 0, 0 };

	int index = 0;
	int prevIndex = 0;
	int userAnswer = -1;

	PlayerDetails Currentplayer = new PlayerDetails();



	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_the_nest);
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		// setting the fonts
		final TextView scoreView = (TextView) findViewById(R.id.scoreView);
		final TextView scoreViewText = (TextView) findViewById(R.id.scoreViewText);
		final TextView myCounter = (TextView) findViewById(R.id.myCounter);
		final TextView myCounterText = (TextView) findViewById(R.id.myCounterText);
		final TextView maxScore = (TextView) findViewById(R.id.maxScore);
		final TextView maxScoreText = (TextView) findViewById(R.id.maxScoreText);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"Fonts/Flipper_Font_New 2.ttf");
		myCounter.setTypeface(typeface);
		maxScore.setTypeface(typeface);
		scoreView.setTypeface(typeface);

		typeface = Typeface.createFromAsset(getAssets(), "Fonts/street.ttf");
		myCounterText.setTypeface(typeface);
		maxScoreText.setTypeface(typeface);
		scoreViewText.setTypeface(typeface);

		myCounterText.setText("Time ");
		maxScoreText.setText("Your Top");
		scoreViewText.setText("Score ");
		// setting the players score
		currentPlayer = ((GlobalLoginApplication) getApplication())
				.getPlayerDetails();
		if (((GlobalLoginApplication) getApplication()).loginStatus()) {
			maxScore.setText(String.valueOf(currentPlayer.SaveTheeggs));
		} else {
			maxScoreText.setText("Register\nYour score");
			maxScoreText.setTextSize(20);
			maxScoreText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// intent listener to open the specific activity
					// Intent myIntent = new Intent(FindTheNestActivity.this,
					// PlayScreenActivity.class);
					Intent playscreen = new Intent(FindTheNestActivity.this,
							RegisterActivity.class);

					startActivity(playscreen);
					 finish();
				}
			});
		}
		
		
		Context context = getApplicationContext();
		toast = Toast.makeText(context, "Game Beginning. Good Luck :)", 0);
		toast.show();

		// setImage();
		imageUpdate();
		counter = new CountDownTimer(100000, 1000) {

			public void onFinish() {

				CharSequence timeUp = "Time is UP!. Game Over";

				if (time == 100) {
					toast.cancel();
					toast = Toast.makeText(getApplicationContext(), timeUp,
							Toast.LENGTH_SHORT);
					toast.show();
					counter.cancel();

				}

			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

				time = (int) (100 - millisUntilFinished / 1000);
				// myCounter.setType
				myCounter.setText("" + Integer.toString(time));
				scoreView.setText(Integer.toString(score));

			}

		};

		counter.start();
		// buttons needed for the hidden menu options
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		Button Play = (Button) findViewById(R.id.btnplays);
		Button Register = (Button) findViewById(R.id.btnregiste);
		Button Login = (Button) findViewById(R.id.btnlogins);
		Button LeaderBoard = (Button) findViewById(R.id.btnleaderboard);
		Button Quit = (Button) findViewById(R.id.btnquit);
		// To the hidden menu option
		Play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
				changeScreen = new Intent(FindTheNestActivity.this,
						PlayScreenActivity.class);
				
				if (((GlobalLoginApplication) getApplication()).loginStatus()) {
					 new UpdateUserScore().execute();
				}

				startActivity(changeScreen);
				finish();
			}
		});

		Register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// intent listener to open the specific activity
				changeScreen = new Intent(FindTheNestActivity.this,
						RegisterActivity.class);
				
				if (((GlobalLoginApplication) getApplication()).loginStatus()) {
					 new UpdateUserScore().execute();
				}


				startActivity(changeScreen);
				finish();

			}
		});

		Login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// intent listener to open the specific activity
				changeScreen = new Intent(FindTheNestActivity.this,
						LogInActivity.class);
				if (((GlobalLoginApplication) getApplication()).loginStatus()) {
					 new UpdateUserScore().execute();
				}

				startActivity(changeScreen);
				finish();

			}
		});

		LeaderBoard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// intent listener to open the specific activity
				changeScreen = new Intent(FindTheNestActivity.this,
						LeaderBoardActivity.class);
				if (((GlobalLoginApplication) getApplication()).loginStatus()) {
					 new UpdateUserScore().execute();
				}

				startActivity(changeScreen);
				finish();

			}
		});

		Quit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// intent listener to open the specific activity
				changeScreen = new Intent(FindTheNestActivity.this,
						PlayScreenActivity.class);
				if (((GlobalLoginApplication) getApplication()).loginStatus()) {
					 new UpdateUserScore().execute();
				}

				startActivity(changeScreen);
				finish();

			}
		});

	}

	public void resetAll() {
		answerIndex = -1;
		wrongInput = 0;
		imageUpdate();
	}

	public void checkAnswer() {
		if (userAnswer == answerIndex) {
			numCorrAns++;
			switch (wrongInput) {
			case 0:
				score += 5;
				break;
			case 1:
				score += 3;
				break;
			case 2:
				score += 1;
				break;
			default:
				score += 0;
			}
			
			 
				
			 Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			        	 ShowtickAndWrong( userAnswer, true, false);		
			        		resetAll();
			         } 
			    }, 2000); 
			    
			    ShowtickAndWrong( userAnswer, true, true);
			// scoreView.setText(Integer.toString(time));

			toast.cancel();
			toast = Toast.makeText(getApplicationContext(), "Good Job!",
					Toast.LENGTH_SHORT);
			toast.show();

			if (numCorrAns % 10 == 0 && numCorrAns != 0) {
				toast.cancel();
				toast = Toast.makeText(getApplicationContext(),
						"congratualtions you've gotten" + numCorrAns
								+ "correct answers", Toast.LENGTH_SHORT);
				toast.show();
			}

		

		} else {

			wrongInput++;
			toast.cancel();
			toast = Toast.makeText(getApplicationContext(),
					"Wrong answer.Try again!", Toast.LENGTH_SHORT);
			toast.show();
			
			
			 Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			        	 ShowtickAndWrong( userAnswer, false, false);			            
			         } 
			    }, 2000); 

	        	 ShowtickAndWrong( userAnswer, false, true);
			    
			//ImageView image = (ImageView) findViewById(R.id.nestView1);
			// ShowtickAndWrong(image.getL.position_id,false,true);
		}
	}

	public void imageUpdate() {

		// for displaying the previous answers
		prevIndex = index;
		int tempIndex = index;

		while (tempIndex == index) {
			index = (int) (Math.random() * 9);

		}

		Drawable birdImage = getResources().getDrawable(birdArray[index]);
		ImageView image = (ImageView) findViewById(R.id.birdView);

		image.setImageDrawable(birdImage);
		// image.getLayoutParams().width= 4*width/7 ;
		// image.getLayoutParams().height=4*height/7;

		answerIndex = (int) (Math.random() * 4);

		boolean notFilled = true;

		int random[] = { -1, -1, -1, -1 };

		// generate random numbers
		int temp = 0;

		for (int i = 0; i <= 3; i++) {
			notFilled = true;
			while (notFilled) {
				notFilled = false;
				temp = (int) (Math.random() * 9);
				for (int J = 0; J < 4; J++) {
					if (temp == random[J]) {
						notFilled = true;
						break;
					}
				}

				if (temp == birdAnswer[index])
					notFilled = true;
			}
			random[i] = temp;
		}

		Drawable nest_1;
		Drawable nest_2;
		Drawable nest_3;
		Drawable nest_4;

		random[answerIndex] = birdAnswer[index];
		nest_1 = getResources().getDrawable(nestArray[random[0]]);
		image = (ImageView) findViewById(R.id.nestView1);
		image.setImageDrawable(nest_1);
		// image.getLayoutParams().width= width/4 ;
		// image.getLayoutParams().height=height/4;
		//

		nest_2 = getResources().getDrawable(nestArray[random[1]]);
		image = (ImageView) findViewById(R.id.nestView2);
		image.setImageDrawable(nest_2);
		// image.getLayoutParams().width= width/4 ;
		// image.getLayoutParams().height=height/4;
		//
		nest_3 = getResources().getDrawable(nestArray[random[2]]);
		image = (ImageView) findViewById(R.id.nestView3);
		image.setImageDrawable(nest_3);
		// image.getLayoutParams().width= width/4 ;
		// image.getLayoutParams().height=height/4;
		//
		nest_4 = getResources().getDrawable(nestArray[random[3]]);
		image = (ImageView) findViewById(R.id.nestView4);
		image.setImageDrawable(nest_4);
		// image.getLayoutParams().width= width/4 ;
		// image.getLayoutParams().height=height/4;
		//
		for (int p = 0; p < 4; p++) {
			indexObjects[p] = random[p];
		}

	}

	private void ShowtickAndWrong(int position, boolean tick, boolean visibility) {
		ImageView ticks = null;
		switch (position) {
		case 0:
			if (tick) {
				ticks = (ImageView) findViewById(R.id.correctView01);
			} else {
				ticks = (ImageView) findViewById(R.id.wrongView01);
			}
			break;
		case 1:
			if (tick) {
				ticks = (ImageView) findViewById(R.id.correctView02);
			} else {
				ticks = (ImageView) findViewById(R.id.wrongView02);
			}
			break;
		case 2:
			if (tick) {
				ticks = (ImageView) findViewById(R.id.correctView03);
			} else {
				ticks = (ImageView) findViewById(R.id.wrongView03);
			}
			break;
		case 3:
			if (tick) {
				ticks = (ImageView) findViewById(R.id.correctView04);
			} else {
				ticks = (ImageView) findViewById(R.id.wrongView04);
			}
			break;
		}
		if (visibility) {
			ticks.setVisibility(View.VISIBLE);
		} else {
			ticks.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.d(TAG, "onResume");

		enableForegroundMode();
	}

	@Override
	protected void onPause() {
		super.onResume();

		Log.d(TAG, "onPause");

		disableForegroundMode();
	}

	@Override
	public void onNewIntent(Intent intent) { //
		Log.d(TAG, "onNewIntent");
		NdefMessage[] msgs = getNdefMessages(intent);
		if (checkCardContent(msgs[0])) {
			checkAnswer();
		}
	}

	// get Ndef Messages from NFC Card
	NdefMessage[] getNdefMessages(Intent intent) {
		// Parse the intent
		NdefMessage[] msgs = null;
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
						empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				msgs = new NdefMessage[] { msg };
			}
		} else {
			Log.d(TAG, "Unknown intent.");
			finish();
		}
		return msgs;
	}

	// check the card contents
	// edit here for the return integer values.
	boolean checkCardContent(final NdefMessage msg) {
		try {
			byte[] payload = msg.getRecords()[0].getPayload();

			String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8"
					: "UTF-16";
			int languageCodeLength = payload[0] & 0077;

			String text = new String(payload, languageCodeLength + 1,
					payload.length - languageCodeLength - 1, textEncoding);

			// convert the string to int to check the contents
			userAnswer = Integer.parseInt(text);
			// check the text in the nfc card and compare
			// example if the NFC card contains '0', return '11'
			if (userAnswer > -1 && userAnswer < 8)
				return true;

			else
				// no match, return a value
				return false;

		} catch (UnsupportedEncodingException e) {
			// should never happen unless we get a malformed tag.
			throw new IllegalArgumentException(e);

		}

	}

	public void enableForegroundMode() {
		Log.d(TAG, "enableForegroundMode");
		// foreground mode gives the current active application priority for
		// reading scanned tags
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED); // filter for tags
		IntentFilter[] writeTagFilters = new IntentFilter[] { tagDetected };
		nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent,
				writeTagFilters, null);
	}

	public void disableForegroundMode() {
		Log.d(TAG, "disableForegroundMode");

		nfcAdapter.disableForegroundDispatch(this);
	}
	 /**
     * Background Async Task to  Save product Details
     * */

    class UpdateUserScore extends AsyncTask<String, String, String> {
    	 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FindTheNestActivity.this);
            pDialog.setMessage("updating score ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
            // getting updated data from EditTexts
           
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, Integer.toString(currentPlayer.Pid)));
            params.add(new BasicNameValuePair(TAG_TOTAL,Integer.toString(currentPlayer.Total)));
            params.add(new BasicNameValuePair(TAG_SAVETHEEGGS, Integer.toString(score)));
            params.add(new BasicNameValuePair(TAG_FANTASTICFEATHERS, Integer.toString(currentPlayer.FantasticFeathers)));
            params.add(new BasicNameValuePair(TAG_THEWEAPON, Integer.toString(currentPlayer.TheWeapon)));
   
            // sending modified data through http request
            // Notice that update product url accepts POST method
            
            
          
            JSONObject json = jsonParser.makeHttpRequest(url_score,
                    "POST", params);
 
            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            //Pushing to local application class to maintain local data
            currentPlayer.SaveTheeggs=score;
            ((GlobalLoginApplication) getApplication()).setPlayerDetails(currentPlayer);
            
        	startActivity(changeScreen);
			finish();
            return null;
        }
 

       
        
    }
 

	
}
