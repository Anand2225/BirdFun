package com.fyp.birdfun;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.birdfun.helpers.PlayerDetails;

public class FindTheNestActivity extends Activity {
	int score=0;
	int time=0;
	int wrongInput=0;
	CountDownTimer counter;
	Toast toast;
	
	
	//NFC declarations
	private static String TAG = FindTheNestActivity.class.getSimpleName();
	private static String TAG1 = "playids";
	private static String TAG2 = "firstTap";
	private static String TAG3 = "secondTap";
	
	protected NfcAdapter nfcAdapter;
    protected PendingIntent nfcPendingIntent;
    private ViewGroup mContainerView;
    
	 private int[] birdArray = {
			 R.drawable.ftn_burrow_1,
			 R.drawable.ftn_burrow_2,
			 R.drawable.ftn_burrow_3,
			 R.drawable.ftn_cavity_1,
			 R.drawable.ftn_cavity_2,
			 R.drawable.ftn_cup_1,
			 R.drawable.ftn_cup_2,
			 R.drawable.ftn_mound_1,
			 R.drawable.ftn_platform_1,
			 };
	 private int[] birdAnswer = {
			 0,
			 0,
			 0,
			 1,
			 1,
			 2,
			 2,
			 3,
			 6,
			 };
	 
	 private int numCorrAns=0;
	 
	 private int[] nestArray={
			 
			 R.drawable.ftn_burrow,
			 R.drawable.ftn_cavity,
			 R.drawable.ftn_cup,
			 R.drawable.ftn_mound,
			 R.drawable.ftn_pendant,
			 R.drawable.ftn_plate,
			 R.drawable.ftn_platform,
			 R.drawable.ftn_scrape,
			 R.drawable.ftn_sphere,
	 };
	 
	 private int answerIndex= -1; 
	 private int[] indexObjects={0,0,0,0};
	 
	 private String[] textAnswers={"burrow","cavity","cup","mound","pendant","plate","platform","scrape","sphere"};
	 int index=0;
	 int prevIndex=0;
	 int userAnswer=-1;

	    PlayerDetails Currentplayer=new PlayerDetails();
	    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_the_nest);
    	nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
       
        //setting the fonts
        final TextView scoreView=(TextView)findViewById(R.id.scoreView);
        final TextView scoreViewText=(TextView)findViewById(R.id.scoreViewText);
        final TextView myCounter = (TextView)findViewById(R.id.myCounter);
        final TextView myCounterText = (TextView)findViewById(R.id.myCounterText);
        final TextView maxScore=(TextView)findViewById(R.id.maxScore);
        final TextView maxScoreText=(TextView)findViewById(R.id.maxScoreText);

      
        Typeface typeface= Typeface.createFromAsset(getAssets(),"Fonts/Flipper_Font_New 2.ttf");
        myCounter.setTypeface(typeface);
        maxScore.setTypeface(typeface);
        scoreView.setTypeface(typeface);
        
         typeface= Typeface.createFromAsset(getAssets(),"Fonts/street.ttf");
         myCounterText.setTypeface(typeface);
         maxScoreText.setTypeface(typeface);
        scoreViewText.setTypeface(typeface);
        
        myCounterText.setText("Time ");
        maxScoreText.setText("Your Top");
        scoreViewText.setText("Score ");
        //setting the players score
        PlayerDetails currentPlayer=((GlobalLoginApplication)getApplication()).getPlayerDetails();
        if(((GlobalLoginApplication)getApplication()).loginStatus()){
        	maxScore.setText(String.valueOf(currentPlayer.SaveTheeggs));
        }
        else{
        	maxScoreText.setText("Register\nYour score");
        	maxScoreText.setTextSize(20);

        }
        Context context = getApplicationContext();
        toast = Toast.makeText(context,"Game Beginning. Good Luck :)", 0);
        toast.show();
        
       // setImage();
        imageUpdate();
        counter=new CountDownTimer(100000, 1000) {
 
       
        public void onFinish() {
  
      	      
        CharSequence timeUp= "Time is UP!. Game Over";
     
        scoreView.setText(Integer.toString(score) );
 	    
 	    answerIndex=-1;
	       if(time==100){
	    	  toast.cancel();
	         toast = Toast.makeText(getApplicationContext(), timeUp,Toast.LENGTH_SHORT);
	          toast.show();
	          counter.cancel();
	      
	       }
	       else{
	          toast.cancel(); 
	       toast = Toast.makeText(getApplicationContext(), "Good Job!",Toast.LENGTH_SHORT);
	  
	       toast.show();
	       if(numCorrAns%10==0 && numCorrAns!=0){
	           toast.cancel(); 
	   	       toast = Toast.makeText(getApplicationContext(), "congratualtions you've gotten"+ numCorrAns +"correct answers",Toast.LENGTH_SHORT);
	   	       toast.show();
	       }
 	    
	 	      imageUpdate();
	       }
        
	       
        }
        @Override
        public void onTick(long millisUntilFinished) {
        // TODO Auto-generated method stub
     
        	time=(int)(100-millisUntilFinished/1000);
        	//myCounter.setType
        	myCounter.setText(""+Integer.toString(time));

        } 

        };


        	counter.start();
        	//buttons needed for the hidden menu options
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            Button Play= (Button) findViewById(R.id.btnplays);
            Button Register= (Button) findViewById(R.id.btnregiste);
            Button Login= (Button) findViewById(R.id.btnlogins);
            Button LeaderBoard= (Button) findViewById(R.id.btnleaderboard);
            Button Quit= (Button) findViewById(R.id.btnquit);
           // To the hidden menu option
          Play.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    			//	 intent listener to open the specific activity
    				 //Intent myIntent = new Intent(FindTheNestActivity.this, PlayScreenActivity.class);
    				 Intent playscreen = new Intent(FindTheNestActivity.this, PlayScreenActivity.class);
                	 
    		            startActivity(playscreen);      
    				    //finish();
    			}
    		});
          	
          Register.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {

    				//	 intent listener to open the specific activity
    					 Intent myIntent = new Intent(FindTheNestActivity.this, RegisterActivity.class);

    			            startActivity(myIntent);      
    					    finish();
    				

    			}
    		});
          
          Login.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub

    				//	 intent listener to open the specific activity
    					 Intent myIntent = new Intent(FindTheNestActivity.this, LogInActivity.class);

    			            startActivity(myIntent);      
    					    finish();
    				

    			}
    		});
          
          LeaderBoard.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub

    				//	 intent listener to open the specific activity
    					 Intent myIntent = new Intent(FindTheNestActivity.this, LeaderBoardActivity.class);

    			            startActivity(myIntent);      
    					    finish();
    				

    			}
    		});
          
          Quit.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub

    				//	 intent listener to open the specific activity
    					 Intent myIntent = new Intent(FindTheNestActivity.this, PlayScreenActivity.class);

    			            startActivity(myIntent);      
    					    finish();
    				

    			}
    		});
          
         
        
    }
    


    public void checkAnswer(){
    	if(userAnswer==answerIndex){
    		  numCorrAns++;
  	          counter.onFinish();
    	   
    	}	
    	else{
    		
     	   Context context = getApplicationContext();

     	  int tries = 3-wrongInput;
     	  CharSequence wrongAnswer=null;
      
     	wrongAnswer = "Wrong answer. Try Again";	
     	toast.cancel();
     	 
     	 toast = Toast.makeText(context,wrongAnswer,toast.LENGTH_SHORT);
     	 toast.show(); 
     	  
    	}
    }
    
    
    public void imageUpdate(){
    	
    	//setting the image relative to the screen size
 	Display display = getWindowManager().getDefaultDisplay();
 	Point size = new Point();
 	display.getSize(size);
 	int width = size.x;
 	int height = size.y;
 	
 	//for displaying the previous answers
    prevIndex=index;
    int tempIndex=index;
    
    while(tempIndex==index)
    {
    	index=(int)(Math.random()*9);
   	      
	      
    }
    

    
   	  Drawable birdImage = getResources().getDrawable(birdArray[index]);
     	ImageView image = (ImageView)findViewById(R.id.birdView);
    
     	image.setImageDrawable(birdImage);
//     	image.getLayoutParams().width= 4*width/7 ;
//     	image.getLayoutParams().height=4*height/7;
     	

     	answerIndex = (int)(Math.random()*4);
     	
     	Drawable nest_1;
     	Drawable nest_2;
     	Drawable nest_3;
     	Drawable nest_4;
     	
     	
    
     	
     	boolean notFilled=true;
     	
     	int  random[]={-1,-1,-1,-1};
     	
     	// generate random numbers 
     	int temp=0;

     	for(int i=0;i<=3;i++){
     		notFilled=true;
     		while(notFilled){
     			notFilled=false;
     			temp=(int) (Math.random() * 9 );
     			for(int J=0;J<4;J++){
     				if(temp==random[J]){
     					notFilled=true;
     					break;
     				}}
     			
     				if(temp==birdAnswer[index])
     					notFilled=true;
     			}
     		random[i]=temp;
     		}
     		
     	
     	
     	random[answerIndex]=birdAnswer[index];
     	


     	nest_1 = getResources().getDrawable(nestArray[random[0]]);
     	image = (ImageView)findViewById(R.id.nestView1);
       	image.setImageDrawable(nest_1);
//    	image.getLayoutParams().width= width/4 ;
//     	image.getLayoutParams().height=height/4;
//     
 
       	nest_2 = getResources().getDrawable(nestArray[random[1]]);
     	image = (ImageView)findViewById(R.id.nestView2);
       	image.setImageDrawable(nest_2);
//    	image.getLayoutParams().width= width/4 ;
//     	image.getLayoutParams().height=height/4;
//  
       	nest_3 = getResources().getDrawable(nestArray[random[2]]);
     	image = (ImageView)findViewById(R.id.nestView3);
       	image.setImageDrawable(nest_3);
//    	image.getLayoutParams().width= width/4 ;
//     	image.getLayoutParams().height=height/4;
//       	
       	nest_4 = getResources().getDrawable(nestArray[random[3]]);
     	image = (ImageView)findViewById(R.id.nestView4);
       	image.setImageDrawable(nest_4);
//    	image.getLayoutParams().width= width/4 ;
//     	image.getLayoutParams().height=height/4;
//     	
       	for(int p=0;p<4;p++){
       		indexObjects[p]=random[p];
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
	if(checkCardContent(msgs[0]))
	{
		checkAnswer();
	}
	}
	//get Ndef Messages from NFC Card
	NdefMessage[] getNdefMessages(Intent intent) {
    // Parse the intent
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
       
        String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0077;      
        
        String text = new String(payload, languageCodeLength + 1,
                payload.length - languageCodeLength - 1, textEncoding);
        
        // convert the string to int to check the contents
        userAnswer = Integer.parseInt(text);
        //check the text in the nfc card and compare
       //example if the NFC card contains '0', return '11'
        if( userAnswer>-1 && userAnswer<8)
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


    
}
