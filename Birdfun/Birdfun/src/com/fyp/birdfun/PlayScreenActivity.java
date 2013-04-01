package com.fyp.birdfun;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyp.birdfun.helpers.PlayerDetails;

public class PlayScreenActivity extends Activity {
	 	Button btnSaveTheEggs;
	    Button btnFantasticFeathers;
	    Button btntheweapon;
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

	 		            startActivity(myIntent);      
	 				    finish();
	 			}
	 		});
	      	
	      Register.setOnClickListener(new View.OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {

	 				//	 intent listener to open the specific activity
	 					 Intent myIntent = new Intent(PlayScreenActivity.this, RegisterActivity.class);

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

	 			            startActivity(myIntent);      
	 					    finish();
	 				

	 			}
	 		});
	      
	      Quit.setOnClickListener(new View.OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub

	 				//	 intent listener to open the specific activity
	 					 Intent myIntent = new Intent(PlayScreenActivity.this, PlayScreenActivity.class);

	 			            startActivity(myIntent);      
	 					    finish();
	 				

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
	             

	                startActivity(i);
	 
	                finish();
	            }
	        });
	       
	        // The weapon click even
	        btntheweapon.setOnClickListener(new View.OnClickListener() {
	       	 
	            
	            public void onClick(View view) {
	                // Launching create the weapon  activity
	                Intent theweapon = new Intent(getApplicationContext(), TheWeaponActivity.class);
	              

	                startActivity(theweapon);
	                finish();
	 
	            }
	        });
	    }
	    
	    //To-do Add exit button
	    //To-do Sign up acitivty to update the score to the server
		//updating the score task runs Following  occasions !
		//User Exit to main menu
	    //User on pause
	    //User on resume
		//User Comes back from playing Save the Eggs
		//User Comes back from playing The weapon game
	    //User Comes back from playing Fantastic feathers
	      
	    
}
