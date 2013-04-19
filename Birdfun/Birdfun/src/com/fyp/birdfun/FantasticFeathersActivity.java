package com.fyp.birdfun;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.fyp.birdfun.FindTheNestActivity.UpdateUserScore;
import com.fyp.birdfun.helpers.JSONParser;
import com.fyp.birdfun.helpers.PlayerDetails;







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
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FantasticFeathersActivity extends Activity implements CreateNdefMessageCallback, OnNdefPushCompleteCallback{

	private static String TAG = FantasticFeathersActivity.class.getSimpleName();
	protected NfcAdapter nfcAdapter;
	protected PendingIntent nfcPendingIntent;
	private Map<Integer, Integer> drawableMap = new HashMap<Integer, Integer>();
	
	int time = 0;
	CountDownTimer counter;
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
	//parameters to update score ends


		
	String text;
	ImageView topL, topR, lowL, lowR, centre, tick1, cross1, tick2, cross2, tick3, cross3, tick4, cross4;
	//Cards are group by set of 4 with 1 centre picture.
	public static Integer[] picOptions = {
		   R.drawable.fantasticfeathers_card1,
		   R.drawable.fantasticfeathers_card2,
		   R.drawable.fantasticfeathers_card3,
		   R.drawable.fantasticfeathers_card4,
		   
		   R.drawable.fantasticfeathers_card5,
		   R.drawable.fantasticfeathers_card6,
		   R.drawable.fantasticfeathers_card7,
		   R.drawable.fantasticfeathers_card8,
		   
		   R.drawable.fantasticfeathers_card9,
		   R.drawable.fantasticfeathers_card10,
		   R.drawable.fantasticfeathers_card11,
		   R.drawable.fantasticfeathers_card12,
		   
		   R.drawable.fantasticfeathers_card13,
		   R.drawable.fantasticfeathers_card14,
		   R.drawable.fantasticfeathers_card15,
		   R.drawable.fantasticfeathers_card16,
		   
		   R.drawable.fantasticfeathers_card17,
		   R.drawable.fantasticfeathers_card18,
		   R.drawable.fantasticfeathers_card19,
		   R.drawable.fantasticfeathers_card20,
		   
		   R.drawable.fantasticfeathers_card21,
		   R.drawable.fantasticfeathers_card22,
		   R.drawable.fantasticfeathers_card23,
		   R.drawable.fantasticfeathers_card24,
		   
		   R.drawable.fantasticfeathers_card25,
		   R.drawable.fantasticfeathers_card26,
		   R.drawable.fantasticfeathers_card27,
		   R.drawable.fantasticfeathers_card28,
		   
		   R.drawable.fantasticfeathers_card29,
		   R.drawable.fantasticfeathers_card30,
		   R.drawable.fantasticfeathers_card31,
		   R.drawable.fantasticfeathers_card32,
		   
		   R.drawable.fantasticfeathers_card33,
		   R.drawable.fantasticfeathers_card34,
		   R.drawable.fantasticfeathers_card35,
		   R.drawable.fantasticfeathers_card36,
		   
		   R.drawable.fantasticfeathers_card37,
		   R.drawable.fantasticfeathers_card38,
		   R.drawable.fantasticfeathers_card39,
		   R.drawable.fantasticfeathers_card40,
		
		};
	public static Integer[] centreOptions = {
		   R.drawable.fantasticfeathers_round1,
		   R.drawable.fantasticfeathers_round2,
		   R.drawable.fantasticfeathers_round3,
		   R.drawable.fantasticfeathers_round4,
		   R.drawable.fantasticfeathers_round5,
		   R.drawable.fantasticfeathers_round6,
		   R.drawable.fantasticfeathers_round7,
		   R.drawable.fantasticfeathers_round8,
		   R.drawable.fantasticfeathers_round9,
		   R.drawable.fantasticfeathers_round10,

		};
	
	
	private Animator mCurrentAnimator;
	private int mShortAnimationDuration;
	private int setCounter =0 ;
	public int Total_score = 0; // score.
	public int Current_score = 10; // score.
	public int prev_score = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		;
		setContentView(R.layout.fantasticfeathers_layout);
		
		// for score updating by anand
		

		

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
			if (((GlobalLoginApplication) getApplication()).loginStatus()) {
				currentPlayer = ((GlobalLoginApplication) getApplication())
						.getPlayerDetails();
			
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
					Intent playscreen = new Intent(FantasticFeathersActivity.this,
							RegisterActivity.class);

					startActivity(playscreen);
				}
			});
		}
		



		// setImage();
		counter = new CountDownTimer(100000, 1000) {

			public void onFinish() {

				
				if (time == 100) {
					counter.cancel();

				}

			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

				time = (int) (100 - millisUntilFinished / 1000);
				// myCounter.setType
				myCounter.setText("" + Integer.toString(time));
				scoreView.setText(Integer.toString(Total_score));

			}

		};

		counter.start();
		//score updating portion done
		topL = (ImageView) findViewById(R.id.topleft);
		topR = (ImageView) findViewById(R.id.topright);
		lowL = (ImageView) findViewById(R.id.lowleft);
		lowR = (ImageView) findViewById(R.id.lowright);
		centre = (ImageView) findViewById(R.id.centre);
		
		
		
		tick1 = (ImageView) findViewById(R.id.tick11);
		tick2 = (ImageView) findViewById(R.id.tick12);
		tick3 = (ImageView) findViewById(R.id.tick21);
		tick4 = (ImageView) findViewById(R.id.tick22);
		
		
		
		cross1 = (ImageView) findViewById(R.id.cross11);
		cross2 = (ImageView) findViewById(R.id.cross12);
		cross3 = (ImageView) findViewById(R.id.cross21);
		cross4 = (ImageView) findViewById(R.id.cross22);
		
		
		//Tick and Cross view
		
		tick1.setImageResource(R.drawable.fantasticfeathers_correct);
		tick2.setImageResource(R.drawable.fantasticfeathers_correct);
		tick3.setImageResource(R.drawable.fantasticfeathers_correct);
		tick4.setImageResource(R.drawable.fantasticfeathers_correct);
		
		cross1.setImageResource(R.drawable.fantasticfeathers_wrong);
		cross2.setImageResource(R.drawable.fantasticfeathers_wrong);
		cross3.setImageResource(R.drawable.fantasticfeathers_wrong);
		cross4.setImageResource(R.drawable.fantasticfeathers_wrong);
		

		
		tick1.setVisibility(View.INVISIBLE);
		tick2.setVisibility(View.INVISIBLE);
		tick3.setVisibility(View.INVISIBLE);
		tick4.setVisibility(View.INVISIBLE);
		
		cross1.setVisibility(View.INVISIBLE);
		cross2.setVisibility(View.INVISIBLE);
		cross3.setVisibility(View.INVISIBLE);
		cross4.setVisibility(View.INVISIBLE);
		
		//Call set cards, eg: setCards(0) == set 1
		setCards(setCounter);
		
		
		//scoreUpdate();
		
		
       // Hook up clicks on the thumbnail views.

        final View thumb1View = findViewById(R.id.topleft);
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
					zoomImageFromThumb(thumb1View, getDrawableId(topL) );
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        
        
        final View thumb2View = findViewById(R.id.topright);
        thumb2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
					zoomImageFromThumb(thumb2View, getDrawableId(topR) );
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        final View thumb3View = findViewById(R.id.lowleft);
        thumb3View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
					zoomImageFromThumb(thumb3View, getDrawableId(lowL) );
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        final View thumb4View = findViewById(R.id.lowright);
        thumb4View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
					zoomImageFromThumb(thumb4View, getDrawableId(lowR) );
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
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

      
        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
	
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
   			//	 intent listener to open the specific activity
   				changeScreen = new Intent(FantasticFeathersActivity.this, PlayScreenActivity.class);
 				
					new UpdateUserScore().execute();

   			}
   		});
      	
      Register.setOnClickListener(new View.OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {

   				//	 intent listener to open the specific activity
   				changeScreen= new Intent(FantasticFeathersActivity.this, RegisterActivity.class);
   				
    					new UpdateUserScore().execute();

   				

   			}
   		});
      
      Login.setOnClickListener(new View.OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				// TODO Auto-generated method stub

   				//	 intent listener to open the specific activity
   				changeScreen = new Intent(FantasticFeathersActivity.this, LogInActivity.class);
    					new UpdateUserScore().execute();

   				

   			}
   		});
      
      LeaderBoard.setOnClickListener(new View.OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				// TODO Auto-generated method stub

   				//	 intent listener to open the specific activity
   				changeScreen= new Intent(FantasticFeathersActivity.this, LeaderBoardActivity.class);
    					new UpdateUserScore().execute();

   			}
   		});
      
      Quit.setOnClickListener(new View.OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				// TODO Auto-generated method stub

   				//	 intent listener to open the specific activity
   				changeScreen= new Intent(FantasticFeathersActivity.this, PlayScreenActivity.class);
   					
   					new UpdateUserScore().execute();


   			}
   		}); 

		
	   
	}
	
	//get drawable ID from ImageView
	public static Integer getDrawableId(ImageView obj)
		    throws SecurityException, NoSuchFieldException,
		    IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
		        java.lang.reflect.Field f = ImageView.class.getDeclaredField("mResource");
		        f.setAccessible(true);
		        Object out = f.get(obj);
		        return (Integer)out;
		}
	
   
   	
	@Override
	public void onNewIntent(Intent intent) { //
		
		
		
		
		Log.d(TAG, "onNewIntent");
		//final PopupWindow popupWindow = new PopupWindow(this);
		//popupWindow.show(findViewById(R.id.centre), 0, -250);
		
		NdefMessage[] msgs = getNdefMessages(intent);
		// First card
		// call checkCardContent method
		// the checkCardContent will return a integer value
		// if current cards cleared then shuffle the cards
		if(checkCardContent(msgs[0])==11)
		{
			//select option A
			if (Check(topL,setCounter) == true)
			{
				//Correct Picture
				new CountDownTimer(4000, 1000) {

				     public void onTick(long millisUntilFinished) {
				        //While counting
				    	 tick1.setVisibility(View.VISIBLE);
				     }

				     public void onFinish() {
				         //when finish
				    	 tick1.setVisibility(View.INVISIBLE);
				    	 setCounter++;
				    	 prev_score = Total_score; //Get Previous Total Score
				    	 Total_score = prev_score + Current_score; //Combine with current score to get total score
				    	 Current_score = 10; //Reset current score to 10
				    	 //scoreUpdate();
						 checkLevel(setCounter);
				     }
				  }.start();

				
			}else // Incorrect Picture
				{
				new CountDownTimer(2000, 1000) {

				     public void onTick(long millisUntilFinished) {
				        //While counting
				    	 cross1.setVisibility(View.VISIBLE);
				     }
				     public void onFinish() {
				         //when finish
				    	 cross1.setVisibility(View.INVISIBLE);
				    	 Current_score--;
				    	 //scoreUpdate();
				     }
				  }.start();
				}
		}
		if(checkCardContent(msgs[0])==12)
		{
			//select option B
			if (Check(topR,setCounter) == true){
				new CountDownTimer(2000, 1000) {

				     public void onTick(long millisUntilFinished) {
				        //While counting
				    	 tick2.setVisibility(View.VISIBLE);
				     }

				     public void onFinish() {
				         //when finish
				    	 tick2.setVisibility(View.INVISIBLE);
				    	 setCounter++;
				    	 prev_score = Total_score;
				    	 Total_score = prev_score + Current_score;
				    	 Current_score = 10;
				    	 //scoreUpdate();
						 checkLevel(setCounter);
				     }
				  }.start();

				
			}else // Incorrect Picture
				{
				new CountDownTimer(2000, 1000) {

				     public void onTick(long millisUntilFinished) {
				        //While counting
				    	 cross2.setVisibility(View.VISIBLE);
				     }
				     public void onFinish() {
				         //when finish
				    	 cross2.setVisibility(View.INVISIBLE);
				    	 Current_score--;
				    	 //scoreUpdate();
				     }
				  }.start();
				}
			
		}
		if(checkCardContent(msgs[0])==21)
		{
			//select option C
			if (Check(lowL,setCounter) == true){
				//Correct Picture
				new CountDownTimer(4000, 1000) {

				     public void onTick(long millisUntilFinished) {
				        //While counting
				    	 tick3.setVisibility(View.VISIBLE);
				     }

				     public void onFinish() {
				         //when finish
				    	 tick3.setVisibility(View.INVISIBLE);
				    	 setCounter++;
				    	 prev_score = Total_score;
				    	 Total_score = prev_score + Current_score;
				    	 Current_score = 10;
				    	 //scoreUpdate();
						 checkLevel(setCounter);
				     }
				  }.start();

				
			}else // Incorrect Picture
				{
				new CountDownTimer(2000, 1000) {

				     public void onTick(long millisUntilFinished) {
				        //While counting
				    	 cross3.setVisibility(View.VISIBLE);
				     }
				     public void onFinish() {
				         //when finish
				    	 cross3.setVisibility(View.INVISIBLE);
				    	 Current_score--;
				    	 //scoreUpdate();
				     }
				  }.start();
				}
		}
		if(checkCardContent(msgs[0])==22)
		{
			//select option D
			if (Check(lowR,setCounter) == true)
			{
			//Correct Picture
				new CountDownTimer(4000, 1000) {

				     public void onTick(long millisUntilFinished) {
				        //While counting
				    	 tick4.setVisibility(View.VISIBLE);
				     }

				     public void onFinish() {
				         //when finish
				    	 tick4.setVisibility(View.INVISIBLE);
				    	 setCounter++;
				    	 prev_score = Total_score;
				    	 Total_score = prev_score + Current_score;
				    	 Current_score = 10;
				    	// scoreUpdate();
						 checkLevel(setCounter);
				     }
				  }.start();
			
		}else // Incorrect Picture
			{
			new CountDownTimer(2000, 1000) {

			     public void onTick(long millisUntilFinished) {
			        //While counting
			    	 cross4.setVisibility(View.VISIBLE);
			     }
			     public void onFinish() {
			         //when finish
			    	 cross4.setVisibility(View.INVISIBLE);
			    	 Current_score--;
			    	 //scoreUpdate();
			     }
			  }.start();
			}
		
		}
	}
	
	
//	public void scoreUpdate()
//	{
//		TextView totalscore = (TextView)findViewById(R.id.totaltag);
//		TextView currentscore = (TextView)findViewById(R.id.scoretag);
//		totalscore.setText(String.valueOf(Total_score));
//		currentscore.setText(String.valueOf(Current_score));
//	}
//	
	private void checkLevel(int counter)
	{
		if(counter < 11)
		{
			setCards(counter);
		}
		//===========================================================================================================
		//end of the game
		//return to menu
		//===========================================================================================================
		
		
		else   
		{
					final FantasticFeathersActivity sPlashScreen = this; 
			    	Intent i = new Intent();
			        i.setClass(sPlashScreen, LogInActivity.class);
					startActivity(i);		
		}
	}
	
	private void setCards(int i)
	{
		//set0 =1234 set1=5678 set2=9101112
		
		
		
		topL.setImageResource(picOptions[i*4+0]);
		topR.setImageResource(picOptions[i*4+1]);
		lowL.setImageResource(picOptions[i*4+2]);
		lowR.setImageResource(picOptions[i*4+3]);
		centre.setImageResource(centreOptions[i+0]);
	}
	
	
	private boolean Check(ImageView IV, int i)
	{
		//set answer here
		drawableMap.put(0, R.drawable.fantasticfeathers_card2);
	    drawableMap.put(1, R.drawable.fantasticfeathers_card8);
		drawableMap.put(2, R.drawable.fantasticfeathers_card9);
	    drawableMap.put(3, R.drawable.fantasticfeathers_card15);
		drawableMap.put(4, R.drawable.fantasticfeathers_card18);
		
		drawableMap.put(5, R.drawable.fantasticfeathers_card24);
	    drawableMap.put(6, R.drawable.fantasticfeathers_card27);
		drawableMap.put(7, R.drawable.fantasticfeathers_card29);
	    drawableMap.put(8, R.drawable.fantasticfeathers_card36);
		drawableMap.put(9, R.drawable.fantasticfeathers_card40);
	   	
		try {
	
			//Toast.makeText(this, getDrawableId(IV), Toast.LENGTH_SHORT).show();
			//Toast.makeText(this, drawableMap.get(i), Toast.LENGTH_SHORT).show();
			if (getDrawableId(IV).equals(drawableMap.get(i))) //AnswerOptions[0] R.drawable.bird2
			return true;
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
	public NdefMessage createNdefMessage(NfcEvent event) { 	

	Log.d(TAG, "createNdefMessage");
	
	throw new IllegalArgumentException("Not implemented"); 
}

	@Override
	public void onNdefPushComplete(NfcEvent arg0) {
	Log.d(TAG, "onNdefPushComplete");
	throw new IllegalArgumentException("Not implemented");
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
	    //check the card contents
	    //edit here for the return integer values.
	    int checkCardContent(final NdefMessage msg) {
	        
	        
	        try {
	            byte[] payload = msg.getRecords()[0].getPayload();
	           
	            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
	            int languageCodeLength = payload[0] & 0077;      
	            
	            String text = new String(payload, languageCodeLength + 1,
	                    payload.length - languageCodeLength - 1, textEncoding);
	            
	            //check the text in the nfc card and compare
	           //example if the NFC card contains '0', return '11'
	            if(text.contains("11")== true){
	            	return 11;
	            }
	            else if (text.contains("12")== true)
	            {
	            	return 12;
	            }
	            else if (text.contains("21")== true)
	            {
	            	return 21;
	            }
	            else if (text.contains("22")== true)
	            {
	            	return 22;
	            }else
	            {
	            	//no match, return a value
	            	return 99;
	            }
	        
	        
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
	        protected String doInBackground(String... args) {
	            // getting updated data from EditTexts
	        	if (((GlobalLoginApplication) getApplication()).loginStatus()){
	           
	        	if(Total_score>currentPlayer.FantasticFeathers){
	          	   currentPlayer.FantasticFeathers=Total_score;
	             }
	             
	              // Building Parameters
	              List<NameValuePair> params = new ArrayList<NameValuePair>();
	              params.add(new BasicNameValuePair(TAG_PID, Integer.toString(currentPlayer.Pid)));
	              params.add(new BasicNameValuePair(TAG_TOTAL,Integer.toString(currentPlayer.Total)));
	              params.add(new BasicNameValuePair(TAG_SAVETHEEGGS, Integer.toString(currentPlayer.SaveTheeggs)));
	              params.add(new BasicNameValuePair(TAG_FANTASTICFEATHERS, Integer.toString(currentPlayer.FantasticFeathers)));
	              params.add(new BasicNameValuePair(TAG_THEWEAPON, Integer.toString(currentPlayer.TheWeapon)));
	              
	          
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
	            currentPlayer.FantasticFeathers=Total_score;
	            ((GlobalLoginApplication) getApplication()).setPlayerDetails(currentPlayer);
	            
	        	}
	        	   startActivity(changeScreen);
	   				finish();
	            
	            return null;
	        }
	 

	        protected void onPostExecute(String file_url) {
	            // dismiss the dialog once done
	        	pDialog.dismiss();
	      	   startActivity(changeScreen);
				finish();

	        	
	        }
	        
	    }
	 
	

}
