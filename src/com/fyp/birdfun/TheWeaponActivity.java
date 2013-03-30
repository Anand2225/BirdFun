package com.fyp.birdfun;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp.birdfun.helpers.Card;
import com.fyp.birdfun.helpers.DisplayNextView;
import com.fyp.birdfun.helpers.Flip3dAnimation;
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
	private static String TAG1 = "playids";
	private static String TAG2 = "firstTap";
	private static String TAG3 = "secondTap";
	
	protected NfcAdapter nfcAdapter;
    protected PendingIntent nfcPendingIntent;
	// Variables for playing the game
 
    private int noofCardsSolved;
    private boolean newTurn;

    //Variable to store the read content
    private String text;
	private int cardValue;
    private int prevReadPlayId;
	private int prevReadCardID;
	//animation part
	private int mShortAnimationDuration;
	private Animator mCurrentAnimator;
	
	//Variables for player details
	 ArrayList<PlayerDetails> playerdata = new ArrayList<PlayerDetails>();   	
	 PlayerDetails Currentplayer=new PlayerDetails();
	 
	//Variable for mangeing score
	 private int thisGamescore;
	 private int total;
	 private int CurrentScore;
	 
	 // for score display
	 TextView newtext;
	//Managing android Activity life cycle
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.theweapon_layout);
	
	//recieve intent from playscreen activities and update current player details
	 Intent intent =getIntent();
     Bundle bundle = intent.getExtras();
     playerdata =  bundle.getParcelableArrayList("player");
     for (int count = 0; count < playerdata.size(); count++) {
        PlayerDetails Currentplayer = (PlayerDetails) playerdata.get(count);
        this.total = Currentplayer.Total;
        this.thisGamescore=Currentplayer.TheWeapon;
       newtext =(TextView)findViewById(R.id.totaltag);
	    newtext.setText( Integer.toString(this.total));
	   // newtext =(TextView)findViewById(R.id.currenthightag);
	    //newtext.setText( this.thisGamescore);
	       
        
        }
     
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
	SetUpGame();
	noofCardsSolved=0;
	prevReadPlayId=99;
	prevReadCardID=99;
	mShortAnimationDuration=300;
	newTurn=true;
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
		LoadCards();
		shuffleCards();
		selectCardsforView();
		shufflePlayCards();
		LoadPlayCardsToView();
		showAndHideCards();
	}
	private void playGame() {
		
        // check whether the tapped card is solved
		 
		if(playCards[cardValue].solved==false&&noofCardsSolved!=4){
			if(newTurn){
				//open the tapped card
				Log.d(TAG2, "newTurn");
				applyRotation(0,-90,false,playCards[cardValue].front_view_id,playCards[cardValue].back_view_id);
				zoomImageFromThumb((ImageView)findViewById(playCards[cardValue].front_view_id),playCards[cardValue].front_drawable_id);
				// remember the position
				prevReadPlayId=playCards[cardValue].position_id;
				// remember the card id
				prevReadCardID=playCards[cardValue].card_id;
				newTurn=false;
				}
			
			else 
			{
				Log.d(TAG2, "secondTurn");
				// check whether same card read if yes close it
				if(playCards[cardValue].position_id ==prevReadPlayId)	
			    	applyRotation(0,90,true,playCards[cardValue].front_view_id,playCards[cardValue].back_view_id);
				//check for correct  card if yes open it
				else if(playCards[cardValue].card_id%16==prevReadCardID%16)
		    	{	applyRotation(0,-90,false,playCards[cardValue].front_view_id,playCards[cardValue].back_view_id);
		    		playCards[cardValue].solved=true;
		    		playCards[prevReadPlayId].solved=true;
		    		noofCardsSolved++;
		    	}
				//check for wrong  card if then close first tapped card
				else
				{
					//open the current card
					applyRotation(0,-90,false,playCards[cardValue].front_view_id,playCards[cardValue].back_view_id);
					//zoom the current card
					zoomImageFromThumb((ImageView)findViewById(playCards[cardValue].front_view_id),playCards[cardValue].front_drawable_id);
		    		//close the previous card
					applyRotation(0,90,true,playCards[prevReadPlayId].front_view_id,playCards[prevReadPlayId].back_view_id);
		    		//close the current card
					applyRotation(0,90,true,playCards[cardValue].front_view_id,playCards[cardValue].back_view_id);
				}
				newTurn=true;
			}
		}
		if(noofCardsSolved==4)
		{
			// reset all the playcard solved to false
			for(int i=0;i<NO_OF_VIEWS;i++)
			{
				playCards[prevReadPlayId].solved=false;
			}
			//repeat the process
			shuffleCards();
			selectCardsforView();
			shufflePlayCards();
			LoadPlayCardsToView();
			showAndHideCards();
		}
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
				if(cardIdFromDeckMod==TempCardIdFromDeckMod)
				{
				if (cardIdFromDeck!=TempCardIdFromDeck)
				{
				Log.d("TempCardIdFromDeck",""+TempCardIdFromDeck );
				Log.d("CardIdFromDeck",""+cardIdFromDeck );
			    playCards[i+4]=cards[j];
			    playCards[i+4].play_id=i+4;
				break;
				}
				}
			}
		
		  
	  }
	}
	private void shufflePlayCards() {
		
		Card temp =new Card(0,0,0);
		long seed =System.currentTimeMillis();
		Random randomGenerator=new Random(seed);
		// Now shuffle the cards  and store it 
		for(int i=0;i<NO_OF_VIEWS;i++)
		{
	
		//Log.d(TAG1, "play id before shuffle is "+playCards[i].play_id);
		}
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
		  new CountDownTimer(10000, 1000) {

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
		 ImageView image1=(ImageView)findViewById(imageview1id);
		 ImageView image2=(ImageView)findViewById(imageview2id);
		 // Find the center of image
		 final float centerX = image1.getWidth() / 2.0f;
		 final float centerY = image1.getHeight() / 2.0f;
		 // Create a new 3D rotation with the supplied parameter
		 // The animation listener is used to trigger the next animation
		 final Flip3dAnimation rotation =
		 new Flip3dAnimation(start, end, centerX, centerY);
		 rotation.setDuration(500);
		 rotation.setFillAfter(true);
		 rotation.setInterpolator(new AccelerateInterpolator());
		 rotation.setAnimationListener(new DisplayNextView(rotate, image1, image2));
				
		 if(rotate)
			image1.startAnimation(rotation);
		 else
			 image2.startAnimation(rotation);
		}
	private void zoomImageFromThumb(final View thumbView, int imageResId) {
	        // If there's an animation in progress, cancel it immediately and proceed with this one.
		   // hide it after 3 seconds
		   final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
	        expandedImageView.setImageResource(imageResId);
	        // Calculate the starting and ending bounds for the zoomed-in image. This step
	       
			  new CountDownTimer(2000, 1000) {
				  // involves lots of math. Yay, math.
			        final Rect startBounds = new Rect();
			        final Rect finalBounds = new Rect();
			        final Point globalOffset = new Point();
			        float startScale;
				     public void onTick(long millisUntilFinished) {
				    	  // Load the high-resolution "zoomed-in" image					     
					        // The start bounds are the global visible rectangle of the thumbnail, and the
					        // final bounds are the global visible rectangle of the container view. Also
					        // set the container view's offset as the origin for the bounds, since that's
					        // the origin for the positioning animation properties (X, Y).
					        thumbView.getGlobalVisibleRect(startBounds);
					        findViewById(R.id.game).getGlobalVisibleRect(finalBounds, globalOffset);
					        startBounds.offset(-globalOffset.x, -globalOffset.y);
					        finalBounds.offset(-globalOffset.x, -globalOffset.y);

					        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
					        // "center crop" technique. This prevents undesirable stretching during the animation.
					        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
					        
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
					      
					        expandedImageView.setVisibility(View.VISIBLE);

					        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
					        // the zoomed-in view (the default is the center of the view).
					        expandedImageView.setPivotX(0f);
					        expandedImageView.setPivotY(0f);

					        // Construct and run the parallel animation of the four translation and scale properties
					        // (X, Y, SCALE_X, and SCALE_Y).
					        AnimatorSet set = new AnimatorSet();
					        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
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
				     }
				     public void onFinish() {
				    	 
				    	 //zoom in 
					        final float startScaleFinal = startScale;
					        
					        if (mCurrentAnimator != null) {
				                mCurrentAnimator.cancel();
				            }

					        AnimatorSet set = new AnimatorSet();
				            set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
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
					        
				            if (mCurrentAnimator != null) {
					            mCurrentAnimator.cancel();
					        }
				    	 
				     }
				  }.start();         
	        
	    }
	//NFC part 
	@Override
	public void onNewIntent(Intent intent) { //
	Log.d(TAG, "onNewIntent");
	NdefMessage[] msgs = getNdefMessages(intent);
	if(checkCardContent(msgs[0]))
	{
		playGame();
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
    

	
	
	
	
}

	