package com.fyp.birdfun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.fyp.birdfun.helpers.JSONParser;
import com.fyp.birdfun.helpers.PlayerDetails;

public class LeaderBoardActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> usersList;
    ArrayList<PlayerDetails> playerdata = new ArrayList<PlayerDetails>();
    // url to get all products list
   
   //private static String leader_board ="http://172.23.194.143/leader_board.php";
   private static String leader_board="http://birdfun.net/leader_board.php";
   
   // JSON Node names
   private static final String TAG_SUCCESS = "success";
   private static final String TAG_FAIL="fail";
   private static final String TAG_USER = "users";
   private static final String TAG_PID = "pid";
   private static final String TAG_RANK = "rank";
   private static final String TAG_NAME = "name";
   private static final String TAG_AGE = "age";
   private static final String  TAG_TOTAL="total";
   private static final String  TAG_SCHOOL="school";
   private static final String TAG_SAVE = "savetheeggs";
   private static final String  TAG_FANTASTIC="fantasticfeathers";
   private static final String  TAG_WEAPON="theweapon";
  
   JSONArray users = null;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.leader_board);
       // Hashmap for ListView
       usersList = new ArrayList<HashMap<String, String>>();
       // Loading products in Background Thread
       
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
				 Intent myIntent = new Intent(LeaderBoardActivity.this, PlayScreenActivity.class);

		            startActivity(myIntent);      
				    finish();
			}
		});
     	
     Register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(LeaderBoardActivity.this, RegisterActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
     
     Login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(LeaderBoardActivity.this, LogInActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
     
     LeaderBoard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(LeaderBoardActivity.this, LeaderBoardActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
     
     Quit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(LeaderBoardActivity.this, PlayScreenActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
    
     new LoadLeaderBoard().execute();
     
   }
   /**
    * Background Async Task to Create new product
    * */
   class LoadLeaderBoard extends AsyncTask<String, String, String> {

       /**
        * Before starting background thread Show Progress Dialog
        * */
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(LeaderBoardActivity.this);
           pDialog.setMessage("Loading leader board.");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }

       /**
        *Checking for login
        * */
       protected String doInBackground(String... args) {
         
    	   // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
          
           // getting JSON Object
           // Note that Login url accepts POST method
           JSONObject json = jsonParser.makeHttpRequest(leader_board,
                   "GET",params);
          
           Log.d(TAG_FAIL,"After Parser");
           // check log cat fro response
           Log.d("Top Scorers", json.toString());

           // check for success tag
           try {
               int success = json.getInt(TAG_SUCCESS);       
               if (success == 1) {
               // products found
               // Getting Array of Products
               users = json.getJSONArray(TAG_USER);
           	
               // looping through All Products
               for (int i = 0; i < users.length(); i++) 
               {
               	JSONObject c = users.getJSONObject(i);
                  // Storing each json item in player object
               	
               	PlayerDetails player=new PlayerDetails();
               	
               	   player.Pid=c.getInt(TAG_PID);
               	   player.Rank=c.getInt(TAG_RANK);
                   player.Name = c.getString(TAG_NAME);        
                   player.Age=c.getInt(TAG_AGE);
                   player.School=c.getString(TAG_SCHOOL);
                   player.Total=c.getInt(TAG_TOTAL);
                   
                   // creating new HashMap
                   HashMap<String, String> map = new HashMap<String, String>();

                   // adding each child node to HashMap key => value
                   map.put(TAG_PID,Integer.toString( player.Pid) );
                   map.put(TAG_NAME, player.Name );
                   map.put(TAG_RANK, Integer.toString(player.Rank));
                   map.put(TAG_AGE, Integer.toString(player.Age));
                   map.put(TAG_SCHOOL, player.School);
                   map.put(TAG_TOTAL, Integer.toString(player.Total));
                   // adding HashList to ArrayList
                   usersList.add(map);
                  
               }

               } 
              
               else {
                   // no products found
                   // Launch Add New product Activity
                   Intent i = new Intent(getApplicationContext(),
                           RegisterActivity.class);
                   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(i);
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return null;
       }

       protected void onPostExecute(String file_url) {
           // dismiss the dialog after getting all products
           pDialog.dismiss();
           // updating UI from Background Thread
           runOnUiThread(new Runnable() {
               public void run() {
            
            ListAdapter adapter = new SimpleAdapter(
            LeaderBoardActivity.this, usersList,           
  R.layout.leader_board_list, new String[] {TAG_RANK,TAG_NAME,TAG_AGE,TAG_SCHOOL,TAG_TOTAL},
  new int[] { R.id.rank_content, R.id.name_content,R.id.age_content,R.id.school_content,R.id.score_content });
            // updating listview
            setListAdapter(adapter);
               }
           });

       }

   }
   
}
