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
	        Intent intent =getIntent();
	        
	        Bundle bundle = intent.getExtras();
             //recieve intent from other activities and update current player detials
	       if(bundle.getParcelableArrayList("player")!=null){
	        playerdata =  bundle.getParcelableArrayList("player");
	     //

	           PlayerDetails Currentplayer = (PlayerDetails) playerdata.get(0);
	           String name = Currentplayer.Name;
	          // newtext =(TextView)findViewById(R.id.nametag);
		      // newtext.setText(name);
	       }
	        
	        // Buttons
	        btnSaveTheEggs = (Button) findViewById(R.id.btnsavetheeggs);
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
	               
	 
	            }
	        });

	        // Fantastic feathers click event
	        btnFantasticFeathers.setOnClickListener(new View.OnClickListener() {
	 
	            
	            public void onClick(View view) {
	            	// Launching All The weapon Activity 
	            	//send the send the current score and total
	                Intent i = new Intent(getApplicationContext(), FantasticFeathersActivity.class);
	               // i.putExtra("player",playerdata);  

	                startActivity(i);
	 
	            }
	        });
	       
	        // The weapon click even
	        btntheweapon.setOnClickListener(new View.OnClickListener() {
	       	 
	            
	            public void onClick(View view) {
	                // Launching create the weapon  activity
	                Intent theweapon = new Intent(getApplicationContext(), TheWeaponActivity.class);
	                theweapon.putExtra("player",playerdata);  

	                startActivity(theweapon);
	 
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
