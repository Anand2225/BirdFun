package com.fyp.birdfun;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.birdfun.FindTheNestActivity.UpdateUserScore;
import com.fyp.birdfun.RegisterActivity.CreateNewUser;
import com.fyp.birdfun.helpers.Card;
import com.fyp.birdfun.helpers.DisplayNextView;
import com.fyp.birdfun.helpers.Flip3dAnimation;
import com.fyp.birdfun.helpers.JSONParser;
import com.fyp.birdfun.helpers.PlayerDetails;

public class TheWeaponActivity extends Activity  {
	//Game set up declarations
	private static int NO_OF_CARDS=32;
	private static int NO_OF_PAIRS=4;
	private static int NO_OF_VIEWS=8;
	private int[] ImageContainersFront;
	private int[] ImageContainersBack;
	private Card cards[];
	private Card playCards[];
	//NFC declarations
	private static String TAG = TheWeaponActivity.class.getSimpleName();
	private static String TAG1 = "newturn";
	private static String TAG2 = "firstTap";
	private static String TAG3 = "secondTap";
	private List myList = new ArrayList();
	private boolean disableintent;
	 Intent changeScreen;
	 
	protected NfcAdapter nfcAdapter;
    protected PendingIntent nfcPendingIntent;
	// Variables for playing the game
 
    private int noofCardsSolved;
    private boolean newTurn;

    //Variable to store the read content
    
	private int cardValue;
    private int prevReadPlayId;
	private int prevReadCardID;
	private int currentValue;
	
	//Variables for player details
	 ArrayList<PlayerDetails> playerdata = new ArrayList<PlayerDetails>();   	
	 PlayerDetails Currentplayer=new PlayerDetails();
	 
	//current players data is here
	 PlayerDetails currentPlayer;
	//Variable for manging score
	 private static String url_score ="http://birdfun.net/update_score.php";
	 
	 private int  roundCounter;
	 
	  JSONParser jsonParser = new JSONParser();
	 
	 // Progress Dialog
	 private ProgressDialog pDialog;
	 //Tags to update the score
	 private static final String TAG_SUCCESS = "success";
	 private static final String TAG_PID = "pid";
	 private static final String TAG_TOTAL = "total";
	 private static final String TAG_SAVETHEEGGS = "savetheeggs";
	 private static final String TAG_THEWEAPON= "theweapon";
	 private static final String TAG_FANTASTICFEATHERS= "fantasticfeathers";
	 
	 // for score display
	
	 //Update this score variable to update score on screen
	int score = 0;
	int time = 0;
	CountDownTimer counter;
	
	
	//Managing android Activity life cycle
	public void onCreate(Bundle savedInstanceState) {
	
		
	super.onCreate(savedInstanceState);
	getWindow().setFormat(PixelFormat.RGBA_8888);
	setContentView(R.layout.theweapon_layout);
	
	//Score Updating portion starts
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
	PlayerDetails currentPlayer = ((GlobalLoginApplication) getApplication())
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
				Intent playscreen = new Intent(TheWeaponActivity.this,
						RegisterActivity.class);

				startActivity(playscreen);
				// finish();
			}
		});
	}
	
	


	counter = new CountDownTimer(1000000, 1000) {

		public void onFinish() {

			CharSequence timeUp = "Time is UP!. Game Over";

			//set your timer limit here
			
//			if (time == 100) {
//				counter.cancel();
//
//			}
			
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
	//Score updating portion ends 
	
	
	//Create adapter and pending intent for NFC
	nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
	
    // create a deck of 32 cards
	cards =new Card[NO_OF_CARDS];
	
	// Create 8 cards to load to imageview
	playCards =new Card[NO_OF_VIEWS];
	
	//Id of the containers to change the image
	ImageContainersFront=new int[NO_OF_VIEWS];
	ImageContainersBack=new int[NO_OF_VIEWS];
	
	// load the views to the array
	LoadCards();
	SetUpGame();
	disableintent=false;
	noofCardsSolved=0;
	
	roundCounter=0;
	
	prevReadPlayId=99;
	prevReadCardID=99;
	newTurn=true;
	Button back= (Button) findViewById(R.id.btnback);
	 back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 new UpdateUserScore().execute();
				
			}
		});
	//buttons to add the side option menu and also the listener methods for this side menu
     Button Play= (Button) findViewById(R.id.btnplays);
     Button Register= (Button) findViewById(R.id.btnregiste);
     Button Login= (Button) findViewById(R.id.btnlogins);
     Button LeaderBoard= (Button) findViewById(R.id.btnleaderboard);
     Button Quit= (Button) findViewById(R.id.btnquit);
    // To the hidden menu option
		Play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
				changeScreen = new Intent(TheWeaponActivity.this,
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
				changeScreen = new Intent(TheWeaponActivity.this,
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
				changeScreen = new Intent(TheWeaponActivity.this,
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
				changeScreen = new Intent(TheWeaponActivity.this,
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
				changeScreen = new Intent(TheWeaponActivity.this,
						PlayScreenActivity.class);
				if (((GlobalLoginApplication) getApplication()).loginStatus()) {
					 new UpdateUserScore().execute();
				}

				startActivity(changeScreen);
				finish();

			}
		});
	
	
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
	//Game Logic
	private void SetUpGame() {
		// TODO Auto-generated method stub
	
		shuffleCards();
		selectCardsforView();
		shufflePlayCards();
		LoadPlayCardsToView();
		showAndHideCards();
		resetTicks();
		if(roundCounter!=10)
		roundCounter++;
		
		
	}
	private void resetTicks() {
		// TODO Auto-generated method stub
		for(int i=0;i<NO_OF_VIEWS;i++)
		{
			ShowtickAndWrong(playCards[i].position_id,false,false);
			ShowtickAndWrong(playCards[i].position_id,true,false);
		}
	}
	private void playGame() {
		
        // check whether the tapped card is solved
		//nfcAdapter.enable();
		currentValue=cardValue;
		
		
		if(playCards[currentValue].solved==false&&noofCardsSolved!=4){
			if(newTurn){
				//open the tapped card
				Log.d(TAG2, "firtap");
				disableintent=true;
				applyRotation(0,-90,false,playCards[currentValue].front_view_id,playCards[currentValue].back_view_id);
				disableintent=false;
				Log.d(TAG2, ""+currentValue);
				ShowtickAndWrong(playCards[cardValue].position_id,true,true);
				// remember the position
				prevReadPlayId=playCards[currentValue].position_id;
				// remember the card id
				prevReadCardID=playCards[currentValue].card_id;
				newTurn=false;
				}
			
			else 
			{
				/*
				 * Position
				 * Tick(true) or wrong(false)
				 * visible(true) and invisible(false)
				 */
				
				// check whether same card read if yes close it
				if(playCards[currentValue].position_id ==prevReadPlayId)	
				{	
					//remove tick and add wrong
					ShowtickAndWrong(playCards[cardValue].position_id,true,false);
			    	applyRotation(0,90,true,playCards[currentValue].front_view_id,playCards[currentValue].back_view_id);
			    	//remove wrong
			    	
				}
				//check for correct  card if yes open it
				else if(playCards[currentValue].card_id%16==prevReadCardID%16)
					
		    	{	
					ShowtickAndWrong(playCards[cardValue].position_id,true,true);
					applyRotation(0,-90,false,playCards[currentValue].front_view_id,playCards[currentValue].back_view_id);
		    		playCards[currentValue].solved=true;
		    		playCards[prevReadPlayId].solved=true;
		    		noofCardsSolved++;
		    	}
				//check for wrong  card if then close first tapped card
				else
				{
					//remove correct
					//
					//add wrong
					ShowtickAndWrong(playCards[prevReadPlayId].position_id,true,false);
				    
					//open the current card
					applyRotation(0,-90,false,playCards[currentValue].front_view_id,playCards[currentValue].back_view_id);
					//wrong card 
				     
					 ShowtickAndWrong(playCards[currentValue].position_id,false,true);
					 ShowtickAndWrong(playCards[prevReadPlayId].position_id,false,true);
					new CountDownTimer(200, 100) {
			
					     public void onTick(long millisUntilFinished) {
					    	 
					    	
					     }
					     public void onFinish() {

						 applyRotation(0,90,true,playCards[prevReadPlayId].front_view_id,playCards[prevReadPlayId].back_view_id);
					     applyRotation(0,90,true,playCards[currentValue].front_view_id,playCards[currentValue].back_view_id);
					    
					     ShowtickAndWrong(playCards[currentValue].position_id,false,false);
					     ShowtickAndWrong(playCards[prevReadPlayId].position_id,false,false);


					     }
					  }.start();
					
		    		//close the previous card
					
		    		//close the current card
					
				}
				newTurn=true;
			}
		}
		if(noofCardsSolved==4)
		{
			noofCardsSolved=0;
			// reset all the playcard solved to false
			for(int i=0;i<NO_OF_VIEWS;i++)
			{
				playCards[i].solved=false;
			}
			//repeat the process
			SetUpGame();
		}
		
	}	

	private void ShowtickAndWrong(int position,boolean tick,boolean visibility){
		ImageView ticks = null;
	
	switch(position)
	{
	case 0: 
		if(tick)
		{ticks=(ImageView)findViewById(R.id.correctView01);}
		else
		{ticks=(ImageView)findViewById(R.id.wrongView01);}
		break;
	case 1: 
		if(tick)
		{ticks=(ImageView)findViewById(R.id.correctView02);}
		else
		{ticks=(ImageView)findViewById(R.id.wrongView02);}
		break;
	case 2: 
		if(tick)
		{ticks=(ImageView)findViewById(R.id.correctView03);}
		else
		{ticks=(ImageView)findViewById(R.id.wrongView03);}
		break;
	case 3: 
		if(tick)
		{ticks=(ImageView)findViewById(R.id.correctView04);}
		else
		{ticks=(ImageView)findViewById(R.id.wrongView04);}
		break;
	case 4: 
		if(tick)
		{ticks=(ImageView)findViewById(R.id.correctView05);}
		else
		{ticks=(ImageView)findViewById(R.id.wrongView05);}
		break;
	case 5: 
		if(tick)
		{ticks=(ImageView)findViewById(R.id.correctView06);}
		else
		{ticks=(ImageView)findViewById(R.id.wrongView06);}
		break;
	case 6: 
		if(tick)
		{ticks=(ImageView)findViewById(R.id.correctView07);}
		else
		{ticks=(ImageView)findViewById(R.id.wrongView07);}
		break;
	case 7: 
		if(tick)
		{ticks=(ImageView)findViewById(R.id.correctView08);}
		else
		{ticks=(ImageView)findViewById(R.id.wrongView08);}
		break;
	}
	if(visibility)
	{ticks.setVisibility(View.VISIBLE);}
	else
	{ticks.setVisibility(View.INVISIBLE);}
		
	}
	private void LoadPlayCardsToView() {
		for(int i=0;i<NO_OF_VIEWS;i++)
		{
		 ImageView image = (ImageView) findViewById(ImageContainersFront[i]);
	        image.setImageResource(playCards[i].front_drawable_id);
	        playCards[i].front_view_id=ImageContainersFront[i];
	        playCards[i].back_view_id=ImageContainersBack[i];
	        playCards[i].position_id=i;
		}
	}
	private void selectCardsforView() {
		// TODO Auto-generated method stub
		// This method will take the first 8 cards from the shuffled card and store it in 
		//play cards
		
		int numberofCards=1;
		//load the first card
		playCards[0]=cards[0]; 
		
		long seed =System.currentTimeMillis();
		Random randomGenerator=new Random(seed);
		boolean exsists=false;
		
		while(numberofCards!=4)
		{
			//generate a number between 0 and 32
			 int randomInt=randomGenerator.nextInt(NO_OF_CARDS);
			 //loop through the first four cars to make sure this card is unique
			for(int i=0;i<numberofCards;i++)
			{
			 
			  if(playCards[i].card_id%16==cards[randomInt].card_id%16)
			  { 
			   exsists=true;
			   break;
			  }
			}
			
			if(exsists==false){
				playCards[numberofCards]=cards[randomInt];
				 playCards[numberofCards].play_id=numberofCards;
				
				numberofCards++;
				
			}
			 exsists=false;
			}
		
		int cardIdFromDeck,TempCardIdFromDeck,cardIdFromDeckMod,TempCardIdFromDeckMod;
		  for(int i=0;i<NO_OF_PAIRS;i++)	
		  {
			  cardIdFromDeck=playCards[i].card_id;
			  cardIdFromDeckMod=cardIdFromDeck%16;
			  Log.d("cardid",""+playCards[i].card_id);
			for( int j=0;j<NO_OF_CARDS;j++)
			{
				TempCardIdFromDeck=cards[j].card_id;
				TempCardIdFromDeckMod=TempCardIdFromDeck%16;
				if((cardIdFromDeckMod==TempCardIdFromDeckMod)&&(TempCardIdFromDeck!=cardIdFromDeck))
				{	playCards[i+4]=cards[j];
			        playCards[i+4].play_id=i+4;
			        Log.d("cardid",""+playCards[i+4].card_id);
			        break;
				}
			
			}
		 }
	  /*
	   int j=0,cardIdFromDeck=0,TempCardIdFromDeck=0;
		int  cardIdFromDeckMod=0,TempCardIdFromDeckMod=0; 
	    for(int i=0;i<NO_OF_PAIRS;i++)	
	  {
		  
		  playCards[i]=cards[i];
		  playCards[i].play_id=i;
		  cardIdFromDeck=cards[i].card_id;
		  cardIdFromDeckMod=cardIdFromDeck%16;
		    for(j=0; j<NO_OF_CARDS;j++) 
			{
		    	TempCardIdFromDeck=cards[j].card_id;
				TempCardIdFromDeckMod=TempCardIdFromDeck%16;
				if(cardIdFromDeckMod==TempCardIdFromDeckMod&&TempCardIdFromDeck!=cardIdFromDeck)
				
				{
					Log.d("CardIdFromDeck",""+cardIdFromDeck );
				Log.d("TempCardIdFromDeck",""+TempCardIdFromDeck );
			
			    playCards[i+4]=cards[j];
			    playCards[i+4].play_id=i+4;
				//break;
				}
			
			}
		
		  
	  }*/
		
	}
	private void shufflePlayCards() {
		
		Card temp =new Card(0,0,0);
		long seed =System.currentTimeMillis();
		Random randomGenerator=new Random(seed);
		// Now shuffle the cards  and store it 
		
		for(int j=NO_OF_VIEWS;j>0;j--)
		{
		   int randomInt=randomGenerator.nextInt(j);
		   if(j!=0)
		   {
			// store the current card  to temp
		   temp=playCards[j-1];
		   //swamp the cards between random number location and current location
		   playCards[j-1]=playCards[randomInt];
		   //put back the card at random location
		   playCards[randomInt]=temp;
		   }
		 //  Log.d(TAG1, "card id after shuffle is "+playCards[j-1].card_id);
		   //Log.d(TAG1, "play card after shuffle is "+playCards[j-1].play_id);
		}
	}
	private void shuffleCards() {
		//This method knuth shuffle to shuffle the deck of cards
		Card temp =new Card(0,0,0);
		//generate random number
		long seed =System.currentTimeMillis();
		Random randomGenerator=new Random(seed);
		// Now shuffle the cards  and store it 
		for(int j=NO_OF_CARDS;j>0;j--)
		{
		   int randomInt=randomGenerator.nextInt(j);
		 
		   if(j!=0)
		   {
			  
			// store the current card  to temp
		   temp=cards[j-1];
		   //swamp the cards between random number location and current location
		   cards[j-1]=cards[randomInt];
		   //put back the card at random location
		   cards[randomInt]=temp;
		   }
		}
		
		
		
	}	
	private void LoadCards() {
		// TODO Auto-generated method stub
	   //get the background id is same for all the cards
		
		// load the id of the imageView containers 
		ImageContainersFront[0]= R.id.BirdView01;
		ImageContainersFront[1]= R.id.BirdView02;
		ImageContainersFront[2]= R.id.BirdView03;
		ImageContainersFront[3]= R.id.BirdView04;
		ImageContainersFront[4]= R.id.FlagView01;
		ImageContainersFront[5]= R.id.FlagView02;
		ImageContainersFront[6]= R.id.FlagView03;
		ImageContainersFront[7]= R.id.FlagView04;
		
		 //birds backview
		ImageContainersBack[0] = R.id.BackView01;
		ImageContainersBack[1] = R.id.BackView02;
		ImageContainersBack[2] = R.id.BackView03;
		ImageContainersBack[3] = R.id.BackView04;
		ImageContainersBack[4] = R.id.BackView05;
		ImageContainersBack[5] = R.id.BackView06;
		ImageContainersBack[6] = R.id.BackView07;
		ImageContainersBack[7] = R.id.BackView08;
		
		// load the drawable ids to the cards
		int back= getResources().getIdentifier("com.fyp.birdfun:drawable/birdfun_background", null, null);
		for(int i=0;i<NO_OF_CARDS;i++)
		{
			int front= getResources().getIdentifier("com.fyp.birdfun:drawable/theweapon_card"+i, null, null);
			
			if(front==0)
			{
				//error to log
			}
			
			else
			  cards[i]=new Card(i,front,back);
		
		}
		
	}
	//Animation part
	private void showAndHideCards() {
		// TODO Auto-generated method stub
		for(int i=0;i<NO_OF_VIEWS;i++)
		  {
		  applyRotation(0,-90,false,playCards[i].front_view_id,playCards[i].back_view_id);
		
		  }
		  // hide it after 3 seconds
		  new CountDownTimer(3000, 1000) {

			     public void onTick(long millisUntilFinished) {
			    	
			     }
			     public void onFinish() {
			    	  for(int i=0;i<NO_OF_VIEWS;i++)
					  {
					  applyRotation(0,90,true,playCards[i].front_view_id,playCards[i].back_view_id);
					 
					  }
			     }
			  }.start();
		
	}
	//must take the start,end,Id of the images to rotate,
	private void applyRotation(float start, float end,boolean rotate,int imageview1id,int imageview2id) {
		// Find the center of image
		disableintent=true;
		 ImageView image1=(ImageView)findViewById(imageview1id);
		 ImageView image2=(ImageView)findViewById(imageview2id);
		 // Find the center of image
		 final float centerX = image1.getWidth() / 2.0f;
		 final float centerY = image1.getHeight() / 2.0f;
		 // Create a new 3D rotation with the supplied parameter
		 // The animation listener is used to trigger the next animation
		 final Flip3dAnimation rotation =
		 new Flip3dAnimation(start, end, centerX, centerY);
		 rotation.setDuration(50);
		 rotation.setFillAfter(true);
		 rotation.setInterpolator(new AccelerateInterpolator());
		 rotation.setAnimationListener(new DisplayNextView(rotate, image1, image2));
				
		 if(rotate)
			image1.startAnimation(rotation);
		 else
			 image2.startAnimation(rotation);
		 disableintent=false;
		}

	@Override
	public void onNewIntent(Intent intent) { //
	Log.d(TAG2, "onNewIntent");
	if(disableintent==false){
	NdefMessage[] msgs = getNdefMessages(intent);
	if(checkCardContent(msgs[0]))
	{
		playGame();
	}
	}
	}
	//get Ndef Messages from NFC Card
	NdefMessage[] getNdefMessages(Intent intent) {
    // Parse the intent
	String text;
    NdefMessage[] msgs = null;
    String action = intent.getAction();
    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            msgs = new NdefMessage[rawMsgs.length];  
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
        } else {
            // Unknown tag type
            byte[] empty = new byte[] {};
            NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
            NdefMessage msg = new NdefMessage(new NdefRecord[] {
                record
            });
            msgs = new NdefMessage[] {
                msg
            };
        }
    } else {
        Log.d(TAG, "Unknown intent.");
        finish();
    }
    return msgs;
}
	//check the card contents
	//edit here for the return integer values.
	boolean checkCardContent(final NdefMessage msg) {
     try {
        byte[] payload = msg.getRecords()[0].getPayload();
        String text;
        String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0077;      
        
        text = new String(payload, languageCodeLength + 1,
                payload.length - languageCodeLength - 1, textEncoding);
        
        // convert the string to int to check the contents
        cardValue = Integer.parseInt(text);
        //check the text in the nfc card and compare
       //example if the NFC card contains '0', return '11'
        if(cardValue>-1&&cardValue<8)
        	return true;
        
        else
        	//no match, return a value
        	return false;
    
    
    } catch (UnsupportedEncodingException e) {
        // should never happen unless we get a malformed tag.
        throw new IllegalArgumentException(e);
    
    }
    
}
	public void enableForegroundMode() {
    Log.d(TAG, "enableForegroundMode");
    // foreground mode gives the current active application priority for reading scanned tags
    IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED); // filter for tags
    IntentFilter[] writeTagFilters = new IntentFilter[] {tagDetected};
    nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
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
            pDialog = new ProgressDialog(TheWeaponActivity.this);
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
            params.add(new BasicNameValuePair(TAG_SAVETHEEGGS, Integer.toString(currentPlayer.SaveTheeggs)));
            params.add(new BasicNameValuePair(TAG_FANTASTICFEATHERS, Integer.toString(currentPlayer.FantasticFeathers)));
            params.add(new BasicNameValuePair(TAG_THEWEAPON, Integer.toString(score)));
   
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
            currentPlayer.TheWeapon=score;
            ((GlobalLoginApplication) getApplication()).setPlayerDetails(currentPlayer);
            
        	startActivity(changeScreen);
			finish();
            return null;
        }
 

       
        
    }
 

	
	
}

	