package com.fyp.birdfun;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Rect;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.birdfun.helpers.JSONParser;
import com.fyp.birdfun.helpers.PlayerDetails;

public class FantasticFeathersActivity extends Activity {
//for score 
int score = 0;
int time = 0;
CountDownTimer counter;
int wrongInput = 0;
int score_B=0;
int qn_num=0;
int score_change = 0;
TextView scoreViewGlobal;
Toast toast;
private int mShortAnimationDuration;

// Parameters to update score once done to server side
private static String url_score ="http://birdfun.net/update_score.php";
private Animator mCurrentAnimator;
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
//private ImageView topL = (ImageView) findViewById(R.id.topleft);
//private ImageView topR = (ImageView) findViewById(R.id.topright);
//private ImageView lowL = (ImageView) findViewById(R.id.lowleft);
//private ImageView lowR = (ImageView) findViewById(R.id.lowright);
//private ImageView centre = (ImageView) findViewById(R.id.centre);
// NFC declarations
private static String TAG = FantasticFeathersActivity.class.getSimpleName();

protected NfcAdapter nfcAdapter;
protected PendingIntent nfcPendingIntent;

private int[] birdArray = { R.drawable.fantasticfeathers_round1,
		   R.drawable.fantasticfeathers_round2,
		   R.drawable.fantasticfeathers_round3,
		   R.drawable.fantasticfeathers_round4,
		   R.drawable.fantasticfeathers_round5,
		   R.drawable.fantasticfeathers_round6,
		   R.drawable.fantasticfeathers_round7,
		   R.drawable.fantasticfeathers_round8,
		   R.drawable.fantasticfeathers_round9,
		   R.drawable.fantasticfeathers_round10};
private int[] birdAnswer = { 0, 1, 2, 3, 4, 5, 6, 7, 8,9 };


private int numCorrAns = 0;

private int[] nestArray = {

//		   R.drawable.fantasticfeathers_card1,
		   R.drawable.fantasticfeathers_card2,
//		   R.drawable.fantasticfeathers_card3,
//		   R.drawable.fantasticfeathers_card4,
//		   
//		   R.drawable.fantasticfeathers_card5,
//		   R.drawable.fantasticfeathers_card6,
//		   R.drawable.fantasticfeathers_card7,
		   R.drawable.fantasticfeathers_card8,
		   
		   R.drawable.fantasticfeathers_card9,
//		   R.drawable.fantasticfeathers_card10,
//		   R.drawable.fantasticfeathers_card11,
//		   R.drawable.fantasticfeathers_card12,
//		   
//		   R.drawable.fantasticfeathers_card13,
//		   R.drawable.fantasticfeathers_card14,
		   R.drawable.fantasticfeathers_card15,
//		   R.drawable.fantasticfeathers_card16,
//		   
//		   R.drawable.fantasticfeathers_card17,
		   R.drawable.fantasticfeathers_card18,
//		   R.drawable.fantasticfeathers_card19,
//		   R.drawable.fantasticfeathers_card20,
//		   
//		   R.drawable.fantasticfeathers_card21,
//		   R.drawable.fantasticfeathers_card22,
//		   R.drawable.fantasticfeathers_card23,
		   R.drawable.fantasticfeathers_card24,
//		   
//		   R.drawable.fantasticfeathers_card25,
//		   R.drawable.fantasticfeathers_card26,
		   R.drawable.fantasticfeathers_card27,
//		   R.drawable.fantasticfeathers_card28,
//		   
		   R.drawable.fantasticfeathers_card29,
//		   R.drawable.fantasticfeathers_card30,
//		   R.drawable.fantasticfeathers_card31,
//		   R.drawable.fantasticfeathers_card32,
//		   
//		   R.drawable.fantasticfeathers_card33,
//		   R.drawable.fantasticfeathers_card34,
//		   R.drawable.fantasticfeathers_card35,
		   R.drawable.fantasticfeathers_card36,
//		   
//		   R.drawable.fantasticfeathers_card37,
//		   R.drawable.fantasticfeathers_card38,
//		   R.drawable.fantasticfeathers_card39,
		   R.drawable.fantasticfeathers_card40, };

private int answerIndex = -1;
private int[] indexObjects = { 0, 0, 0, 0 };


int index = 0;
int prevIndex = 0;
int userAnswer = -1;
PlayerDetails Currentplayer = new PlayerDetails();
@Override
protected void onCreate(Bundle savedInstanceState) {

super.onCreate(savedInstanceState);

//topL = (ImageView) findViewById(R.id.topleft);
//topR = (ImageView) findViewById(R.id.topright);
//lowL = (ImageView) findViewById(R.id.lowleft);
//lowR = (ImageView) findViewById(R.id.lowright);
//centre = (ImageView) findViewById(R.id.centre);


setContentView(R.layout.fantasticfeathers_layout);
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
currentPlayer = ((GlobalLoginApplication) getApplication()).getPlayerDetails();
scoreViewGlobal=scoreView;

if (((GlobalLoginApplication) getApplication()).loginStatus()) {
maxScore.setText(String.valueOf(currentPlayer.FantasticFeathers));
} else {
maxScoreText.setText("Register\nYour score");
maxScoreText.setTextSize(20);
maxScoreText.setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View v) {
// intent listener to open the specific activity
// Intent myIntent = new Intent(FantasticFeathersActivity.this,
// PlayScreenActivity.class);
Intent playscreen = new Intent(FantasticFeathersActivity.this,
RegisterActivity.class);

startActivity(playscreen);
//finish();
}
});
}


Context context = getApplicationContext();
toast = Toast.makeText(context, "Game Beginning. Good Luck :)", 0);
toast.show();

// setImage();
imageUpdate();




counter = new CountDownTimer(300000, 1000) {

@Override
public void onFinish() {
//stop the timer from running
	
cancel();

score_change=(int)Math.exp(7.00+2.00*((time*1.00)/300.00));
//scoreViewGlobal.setText(Integer.toString(score_change));
	
     CountDownTimer miniCounter = new CountDownTimer(((score_change)*100),5){
	 int timeBlock=time/score_change;
	 int scoreBlocks=score_change/(time);
	 @Override
	public void onFinish() {
	 //	score_change=0;

	 cancel();
	 //nothing to do on finish
	 }


	 @Override
	 public void onTick(long millisUntilFinished) {
	 if( time>=1 ){
	 score_change-=scoreBlocks;
	 score+=scoreBlocks;

	scoreViewGlobal.setText(Integer.toString(score));
	 time--;
	 myCounter.setText("" + Integer.toString(time));
	 }
	 }
	     };	     
	 miniCounter.start();

}


@Override
public void onTick(long millisUntilFinished) {
// TODO Auto-generated method stub
time = (int) ( millisUntilFinished / 1000);
// myCounter.setType
myCounter.setText("" + Integer.toString(time));
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
Button Help = (Button) findViewById(R.id.btnhelp);
// To the hidden menu option

Help.setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View v) {
changeScreen = new Intent(FantasticFeathersActivity.this,
HelpActivity.class);
new UpdateUserScore().execute();
}
});

Play.setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View v) {
changeScreen = new Intent(FantasticFeathersActivity.this,
PlayScreenActivity.class);
new UpdateUserScore().execute();
}
});

Register.setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View v) {

// intent listener to open the specific activity
changeScreen = new Intent(FantasticFeathersActivity.this,
RegisterActivity.class);
new UpdateUserScore().execute();

}
});

Login.setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View v) {
// TODO Auto-generated method stub

// intent listener to open the specific activity
changeScreen = new Intent(FantasticFeathersActivity.this,
LogInActivity.class);
new UpdateUserScore().execute();


}
});

LeaderBoard.setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View v) {
// TODO Auto-generated method stub

// intent listener to open the specific activity
changeScreen = new Intent(FantasticFeathersActivity.this,
LeaderBoardActivity.class);
new UpdateUserScore().execute();
}
});

Quit.setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View v) {
// TODO Auto-generated method stub

// intent listener to open the specific activity
changeScreen = new Intent(FantasticFeathersActivity.this,
PlayScreenActivity.class);
new UpdateUserScore().execute();

}
});

}

public void resetAll() {
if(qn_num>=15){
qn_num++;
counter.onFinish();
//endgamescreen
//set end input 
}
else{
qn_num++;
answerIndex = -1;
wrongInput = 0;
imageUpdate();
}
}
 

public void checkAnswer() {
if(qn_num<16){
if (userAnswer == answerIndex) {
//	 numCorrAns++;
//	 switch (wrongInput) {
//	 case 0:
//	 score += 5;
//	 break;
//	 case 1:
//	 score += 3;
//	 break;
//	 case 2:
//	 score += 1;
//	 break;
//	 default:
//	 score += 0;
//	 }
//
	  ((GlobalLoginApplication)getApplication()).sounds.playcorrectSound();
		
score_change+=100;
     	 CountDownTimer miniCounter = new CountDownTimer((score_change*20),4){
     
 	 @Override
	public void onFinish() {
 	 //score_change=0;
 	 //nothing to do on finish
 	 }
 
 
 	 @Override
 	 public void onTick(long millisUntilFinished) {
 	 if(score_change>=1){
 	 score_change-=5;
 	 score+=5;
 	 scoreViewGlobal.setText(Integer.toString(score));
 	 }
 	 }
     	 };
     
     
 	 miniCounter.start();
Handler handler = new Handler(); 
   handler.postDelayed(new Runnable() { 
        @Override
		public void run() { 
      
    ShowtickAndWrong( userAnswer, true, false);
   
       	 resetAll();
        } 
   }, 1000); 
   
ShowtickAndWrong( userAnswer, true, true);
     
//scoreView.setText(Integer.toString(time));

toast.cancel();
toast = Toast.makeText(getApplicationContext(), "Good Job!",
Toast.LENGTH_SHORT);
toast.show();

//	 if (numCorrAns % 10 == 0 && numCorrAns != 0) {
//	 toast.cancel();
//	 toast = Toast.makeText(getApplicationContext(),
//	 "congratualtions you've gotten" + numCorrAns
//	 + "correct answers", Toast.LENGTH_SHORT);
//	 toast.show();
//	 }

} else {
	  ((GlobalLoginApplication)getApplication()).sounds.playwrongSound();
		
wrongInput++;
toast.cancel();
toast = Toast.makeText(getApplicationContext(),
"Wrong answer.Try again!", Toast.LENGTH_SHORT);
toast.show();
Handler handler = new Handler(); 
   handler.postDelayed(new Runnable() { 
        @Override
		public void run() { 
       	 ShowtickAndWrong( userAnswer, false, false);
       
        } 
   }, 2000); 

       	 ShowtickAndWrong( userAnswer, false, true);
   
//ImageView image = (ImageView) findViewById(R.id.nestView1);
// ShowtickAndWrong(image.getL.position_id,false,true);
}
}
}

public void imageUpdate() { 


//	
// for displaying the previous answers
prevIndex = index;
int tempIndex = index;
long seed =System.currentTimeMillis();
Random randomGenerator=new Random(seed);

while (tempIndex == index) {
	index = randomGenerator.nextInt(10);
}

final ImageView topL = (ImageView) findViewById(R.id.topleft);
final ImageView topR = (ImageView) findViewById(R.id.topright);
final ImageView lowL = (ImageView) findViewById(R.id.lowleft);
final ImageView lowR = (ImageView) findViewById(R.id.lowright);
final ImageView centre = (ImageView) findViewById(R.id.centre);


Drawable birdImage = getResources().getDrawable(birdArray[index]);
ImageView image = (ImageView) findViewById(R.id.centre);

image.setImageDrawable(birdImage);
// image.getLayoutParams().width=4*width/7 ;
// image.getLayoutParams().height=4*height/7;

answerIndex = randomGenerator.nextInt(4);

boolean notFilled = true;

int random[] = { -1, -1, -1, -1 };

// generate random numbers
int temp = 0;

for (int i = 0; i <= 3; i++) {
notFilled = true;
while (notFilled) {
notFilled = false;
temp = randomGenerator.nextInt(10);
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
 
topL.setImageDrawable(nest_1);
// image.getLayoutParams().width= width/4 ;
// image.getLayoutParams().height=height/4;
//

nest_2 = getResources().getDrawable(nestArray[random[1]]);
 
topR.setImageDrawable(nest_2);
// image.getLayoutParams().width= width/4 ;
// image.getLayoutParams().height=height/4;
//
nest_3 = getResources().getDrawable(nestArray[random[2]]);
 
lowL.setImageDrawable(nest_3);
// image.getLayoutParams().width= width/4 ;
// image.getLayoutParams().height=height/4;
//
nest_4 = getResources().getDrawable(nestArray[random[3]]);
 
lowR.setImageDrawable(nest_4);
// image.getLayoutParams().width= width/4 ;
// image.getLayoutParams().height=height/4;
//
for (int p = 0; p < 4; p++) {
indexObjects[p] = random[p];
}
final int one=nestArray[random[0]];
final int two=nestArray[random[1]];
final int three=nestArray[random[2]];
final int four=nestArray[random[3]];

//Hook up clicks on the thumbnail views.

// Retrieve and cache the system's default "short" animation time.
mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

final View thumb1View = findViewById(R.id.topleft);
thumb1View.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
try {
			zoomImageFromThumb(thumb1View, one );
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
	}
}
});



final View thumb2View = findViewById(R.id.topright);
thumb2View.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
try {
			zoomImageFromThumb(thumb2View, two );
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
}
});

final View thumb3View = findViewById(R.id.lowleft);
thumb3View.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
try {
			zoomImageFromThumb(thumb3View, three);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
}
});

final View thumb4View = findViewById(R.id.lowright);
thumb4View.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
try {
			zoomImageFromThumb(thumb4View, four );
		} catch (SecurityException e) {
			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		}
}
});
final View thumb5View = findViewById(R.id.centre);
thumb5View.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
//do nothing.
}
});



}
public static Integer getDrawableId(ImageView obj)
throws SecurityException, NoSuchFieldException,
IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
    java.lang.reflect.Field f = ImageView.class.getDeclaredField("mResource");
    f.setAccessible(true);
    Object out = f.get(obj);
    return (Integer)out;
}

private void ShowtickAndWrong(int position, boolean tick, boolean visibility) {
ImageView ticks = null;
ImageView non_ticks1=null;
ImageView non_ticks2=null;
ImageView non_ticks3=null;
ImageView non_ticks4=null;
ImageView non_ticks5=null;
ImageView non_ticks6=null;
ImageView non_ticks7=null;

switch (position) {
case 0:
if (tick) {
ticks = (ImageView) findViewById(R.id.tick11);

non_ticks1=(ImageView) findViewById(R.id.cross11);
non_ticks2=(ImageView) findViewById(R.id.tick12);
non_ticks3=(ImageView) findViewById(R.id.cross12);
non_ticks4=(ImageView) findViewById(R.id.tick21);
non_ticks5=(ImageView) findViewById(R.id.cross21);
non_ticks6=(ImageView) findViewById(R.id.tick22);
non_ticks7=(ImageView) findViewById(R.id.cross22);

} else {
ticks = (ImageView) findViewById(R.id.cross11);
non_ticks1=(ImageView) findViewById(R.id.tick11);
non_ticks2=(ImageView) findViewById(R.id.tick12);
non_ticks3=(ImageView) findViewById(R.id.cross12);
non_ticks4=(ImageView) findViewById(R.id.tick21);
non_ticks5=(ImageView) findViewById(R.id.cross21);
non_ticks6=(ImageView) findViewById(R.id.tick22);
non_ticks7=(ImageView) findViewById(R.id.cross22);
}
break;
case 1:
if (tick) {
	ticks = (ImageView) findViewById(R.id.tick12);
	non_ticks1=(ImageView) findViewById(R.id.cross12);
	non_ticks2=(ImageView) findViewById(R.id.tick11);
	non_ticks3=(ImageView) findViewById(R.id.cross11);
	non_ticks4=(ImageView) findViewById(R.id.tick21);
	non_ticks5=(ImageView) findViewById(R.id.cross21);
	non_ticks6=(ImageView) findViewById(R.id.tick22);
	non_ticks7=(ImageView) findViewById(R.id.cross22);
} 
else {
	ticks = (ImageView) findViewById(R.id.cross12);
	non_ticks1=(ImageView) findViewById(R.id.tick12);
	non_ticks2=(ImageView) findViewById(R.id.tick11);
	non_ticks3=(ImageView) findViewById(R.id.cross11);
	non_ticks4=(ImageView) findViewById(R.id.tick21);
	non_ticks5=(ImageView) findViewById(R.id.cross21);
	non_ticks6=(ImageView) findViewById(R.id.tick22);
	non_ticks7=(ImageView) findViewById(R.id.cross22);
	}
break;
case 2:
if (tick) {

	ticks = (ImageView) findViewById(R.id.tick21);
	non_ticks1=(ImageView) findViewById(R.id.cross21);
	non_ticks2=(ImageView) findViewById(R.id.tick11);
	non_ticks3=(ImageView) findViewById(R.id.cross11);
	non_ticks4=(ImageView) findViewById(R.id.tick12);
	non_ticks5=(ImageView) findViewById(R.id.cross12);
	non_ticks6=(ImageView) findViewById(R.id.tick22);
	non_ticks7=(ImageView) findViewById(R.id.cross22);
	
} else {

	ticks = (ImageView) findViewById(R.id.cross21);
	non_ticks1=(ImageView) findViewById(R.id.tick21);
	non_ticks2=(ImageView) findViewById(R.id.tick11);
	non_ticks3=(ImageView) findViewById(R.id.cross11);
	non_ticks4=(ImageView) findViewById(R.id.tick12);
	non_ticks5=(ImageView) findViewById(R.id.cross12);
	non_ticks6=(ImageView) findViewById(R.id.tick22);
	non_ticks7=(ImageView) findViewById(R.id.cross22);

}
break;
case 3:
if (tick) {
	ticks = (ImageView) findViewById(R.id.tick22);
	non_ticks1=(ImageView) findViewById(R.id.cross22);
	non_ticks2=(ImageView) findViewById(R.id.tick11 );
	non_ticks3=(ImageView) findViewById(R.id.cross11);
	non_ticks4=(ImageView) findViewById(R.id.tick21 );
	non_ticks5=(ImageView) findViewById(R.id.cross21);
	non_ticks6=(ImageView) findViewById(R.id.tick12 );
	non_ticks7=(ImageView) findViewById(R.id.cross12);
} else {
	ticks = (ImageView) findViewById(R.id.cross22);
	non_ticks1=(ImageView) findViewById(R.id.tick22);
	non_ticks2=(ImageView) findViewById(R.id.tick11 );
	non_ticks3=(ImageView) findViewById(R.id.cross11);
	non_ticks4=(ImageView) findViewById(R.id.tick21 );
	non_ticks5=(ImageView) findViewById(R.id.cross21);
	non_ticks6=(ImageView) findViewById(R.id.tick12 );
	non_ticks7=(ImageView) findViewById(R.id.cross12);
}
break;
}
if (visibility) {
ticks.setVisibility(View.VISIBLE);
non_ticks1.setVisibility(View.INVISIBLE);
non_ticks2.setVisibility(View.INVISIBLE);
non_ticks3.setVisibility(View.INVISIBLE);
non_ticks4.setVisibility(View.INVISIBLE);
non_ticks5.setVisibility(View.INVISIBLE);
non_ticks6.setVisibility(View.INVISIBLE);
non_ticks7.setVisibility(View.INVISIBLE);
} else {
ticks.setVisibility(View.INVISIBLE);
non_ticks1.setVisibility(View.INVISIBLE);
non_ticks2.setVisibility(View.INVISIBLE);
non_ticks3.setVisibility(View.INVISIBLE);
non_ticks4.setVisibility(View.INVISIBLE);
non_ticks5.setVisibility(View.INVISIBLE);
non_ticks6.setVisibility(View.INVISIBLE);
non_ticks7.setVisibility(View.INVISIBLE);
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

private void zoomImageFromThumb(final View thumbView, int imageResId) {
// If there's an animation in progress, cancel it immediately and proceed with this one.
if (mCurrentAnimator != null) {
    mCurrentAnimator.cancel();
}

// Load the high-resolution "zoomed-in" image.
final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
expandedImageView.setImageResource(imageResId);

// Calculate the starting and ending bounds for the zoomed-in image. This step
// involves lots of math. Yay, math.
final Rect startBounds = new Rect();
final Rect finalBounds = new Rect();
final Point globalOffset = new Point();

// The start bounds are the global visible rectangle of the thumbnail, and the
// final bounds are the global visible rectangle of the container view. Also
// set the container view's offset as the origin for the bounds, since that's
// the origin for the positioning animation properties (X, Y).
thumbView.getGlobalVisibleRect(startBounds);
findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
startBounds.offset(-globalOffset.x, -globalOffset.y);
finalBounds.offset(-globalOffset.x, -globalOffset.y);

// Adjust the start bounds to be the same aspect ratio as the final bounds using the
// "center crop" technique. This prevents undesirable stretching during the animation.
// Also calculate the start scaling factor (the end scaling factor is always 1.0).
float startScale;
if ((float) finalBounds.width() / finalBounds.height()
        > (float) startBounds.width() / startBounds.height()) {
    // Extend start bounds horizontally
    startScale = (float) startBounds.height() / finalBounds.height();
    float startWidth = startScale * finalBounds.width();
    float deltaWidth = (startWidth - startBounds.width()) / 2;
    startBounds.left -= deltaWidth;
    startBounds.right += deltaWidth;
} else {
    // Extend start bounds vertically
    startScale = (float) startBounds.width() / finalBounds.width();
    float startHeight = startScale * finalBounds.height();
    float deltaHeight = (startHeight - startBounds.height()) / 2;
    startBounds.top -= deltaHeight;
    startBounds.bottom += deltaHeight;
}

// Hide the thumbnail and show the zoomed-in view. When the animation begins,
// it will position the zoomed-in view in the place of the thumbnail.
thumbView.setAlpha(0f);
expandedImageView.setVisibility(View.VISIBLE);

// Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
// the zoomed-in view (the default is the center of the view).
expandedImageView.setPivotX(0f);
expandedImageView.setPivotY(0f);

// Construct and run the parallel animation of the four translation and scale properties
// (X, Y, SCALE_X, and SCALE_Y).
AnimatorSet set = new AnimatorSet();
set
        .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
                finalBounds.left))
        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top,
                finalBounds.top))
        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
set.setDuration(mShortAnimationDuration);
set.setInterpolator(new DecelerateInterpolator());
set.addListener(new AnimatorListenerAdapter() {
    @Override
    public void onAnimationEnd(Animator animation) {
        mCurrentAnimator = null;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        mCurrentAnimator = null;
    }
});
set.start();
mCurrentAnimator = set;

// Upon clicking the zoomed-in image, it should zoom back down to the original bounds
// and show the thumbnail instead of the expanded image.
final float startScaleFinal = startScale;
expandedImageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Animate the four positioning/sizing properties in parallel, back to their
        // original values.
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                thumbView.setAlpha(1f);
                expandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                thumbView.setAlpha(1f);
                expandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
    }
});
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
            pDialog = new ProgressDialog(FantasticFeathersActivity.this);
            pDialog.setMessage("updating score ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        @Override
		protected String doInBackground(String... args) {
            // getting updated data from EditTexts
        if (((GlobalLoginApplication)getApplication()).loginStatus()){
           if(score>currentPlayer.FantasticFeathers){
          currentPlayer.Total+=score-currentPlayer.FantasticFeathers;
          
        	  currentPlayer.FantasticFeathers=score;
          
           }
           
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, Integer.toString(currentPlayer.Pid)));
            params.add(new BasicNameValuePair(TAG_TOTAL,Integer.toString(currentPlayer.Total)));
            params.add(new BasicNameValuePair(TAG_SAVETHEEGGS, Integer.toString(currentPlayer.SaveTheeggs)));
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
            

            ((GlobalLoginApplication) getApplication()).setPlayerDetails(currentPlayer);
        }
       
        startActivity(changeScreen);
 	 finish();
       
            return null;
        }
 
        @Override
		protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
        
        pDialog.dismiss();
        startActivity(changeScreen);
 	 finish();


        }
       
        
    }
 

}
