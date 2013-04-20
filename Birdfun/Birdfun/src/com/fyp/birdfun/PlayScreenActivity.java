package com.fyp.birdfun;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyp.birdfun.helpers.PlayerDetails;

public class PlayScreenActivity extends Activity {
	 	Button btnSaveTheEggs;
	    Button btnFantasticFeathers;
	    Button btntheweapon;
	    
	    MediaPlayer mp;
	    
	    ArrayList<PlayerDetails> playerdata = new ArrayList<PlayerDetails>();
	   	
	    PlayerDetails Currentplayer=new PlayerDetails();
	    
	    TextView newtext;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.play_screen);
	        
	        // for side option buttons 
   
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
	 				 Intent myIntent = new Intent(PlayScreenActivity.this, PlayScreenActivity.class);
	 				 mp.stop();
	 		            startActivity(myIntent);      
	 				    finish();
	 			}
	 		});
	      	
	      Register.setOnClickListener(new View.OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {

	 				//	 intent listener to open the specific activity
	 					 Intent myIntent = new Intent(PlayScreenActivity.this, RegisterActivity.class);
	 					mp.stop();
	 			            startActivity(myIntent);      
	 					    finish();
	 				

	 			}
	 		});
	      
	      Login.setOnClickListener(new View.OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub

	 				//	 intent listener to open the specific activity
	 					 Intent myIntent = new Intent(PlayScreenActivity.this, LogInActivity.class);
	 					 mp.stop();
	 			            startActivity(myIntent);      
	 					    finish();
	 				

	 			}
	 		});
	      
	      LeaderBoard.setOnClickListener(new View.OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub

	 				//	 intent listener to open the specific activity
	 					 Intent myIntent = new Intent(PlayScreenActivity.this, LeaderBoardActivity.class);
	 					 mp.stop();
	 			            startActivity(myIntent);      
	 					    finish();
	 				

	 			}
	 		});
	      
	      Quit.setOnClickListener(new View.OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub
	 				//onPause();
	 			
	 		    Intent intent = new Intent(Intent.ACTION_MAIN);
	 			intent.addCategory(Intent.CATEGORY_HOME);
	 			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 			startActivity(intent);

	 			}
	 		});
	        
	        // Buttons
	        btnSaveTheEggs = (Button) findViewById(R.id.btnSaveTheEggs);
	        btnFantasticFeathers = (Button) findViewById(R.id.btnfantasticfeathers);
	        btntheweapon = (Button) findViewById(R.id.btntheweapon);
	     
	        // Save The Eggs  click event
	        btnSaveTheEggs.setOnClickListener(new View.OnClickListener() {
	 
	            
	            public void onClick(View view) {
	                // Launching All The weapon Activity 
	            	//send the player details
	                Intent i = new Intent(getApplicationContext(), FindTheNestActivity.class);
	                //i.putExtra("player",playerdata);  
	                mp.stop();
	                startActivity(i);
	                finish();
	               
	 
	            }
	        });

	        // Fantastic feathers click event
	        btnFantasticFeathers.setOnClickListener(new View.OnClickListener() {
	 
	            
	            public void onClick(View view) {
	            	// Launching All The weapon Activity 
	            	//send the send the current score and total
	                Intent i = new Intent(getApplicationContext(), FantasticFeathersActivity.class);
	             
	                mp.stop();
	                startActivity(i);
	 
	                finish();
	            }
	        });
	       
	        // The weapon click even
	        btntheweapon.setOnClickListener(new View.OnClickListener() {
	       	 
	            
	            public void onClick(View view) {
	                // Launching create the weapon  activity
	                Intent theweapon = new Intent(getApplicationContext(), TheWeaponActivity.class);
	              
	                mp.stop();
	                startActivity(theweapon);
	                finish();
	 
	            }
	        });
	    }
	    @Override
	    public void onSaveInstanceState(Bundle savedInstanceState) {
	        super.onSaveInstanceState(savedInstanceState);
	        // your stuff or nothing
	    }

	    @Override
	    public void onRestoreInstanceState(Bundle savedInstanceState) {
	        super.onRestoreInstanceState(savedInstanceState);
	        // your stuff or nothing
	    }
	  
	    @Override
	    protected void onPause() {
	    	super.onPause();
	    	  mp.setLooping(false);
	            if (mp != null) {
	            	if (mp.isPlaying()==true )
	            	{     mp.stop();
	            		  mp.release();
	            	}}
	           
	    }
	    @Override
	    public void onResume(){
	        super.onResume();
	        mp = MediaPlayer.create(getApplicationContext(), R.raw.bgm);
	        mp.setLooping(true);
	        mp.setOnPreparedListener(new OnPreparedListener() {
	            @Override
	            public void onPrepared(MediaPlayer mp) {
	            //Now your media player is ready to play   
	            	 mp.start();
	            }
	        });
	       
	    }
	   
}
