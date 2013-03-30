package com.fyp.birdfun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp.birdfun.helpers.JSONParser;
import com.fyp.birdfun.helpers.PlayerDetails;
 

public class LogInActivity extends Activity{

    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> productsList;
    ArrayList<PlayerDetails> playerdata = new ArrayList<PlayerDetails>();
    // url to get all products list
   //private static String url_login = "http://31.170.160.92/login_user.php";
   //private static String url_login ="http://172.23.194.143/login_user.php";
   // private static String url_login="http://birdfun.netii.net/login_user.php";
  
    private static String url_login ="http://birdfun.net/login_user.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FAIL="fail";
    private static final String TAG_USER = "users";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String  TAG_TOTAL="total";
    private static final String TAG_SAVE = "savetheeggs";
    private static final String  TAG_FANTASTIC="fantasticfeathers";
    private static final String  TAG_WEAPON="theweapon";
    private boolean checkSuccess0 = false;
    private boolean checkSuccess3 = false;
    
    
    // products JSONArray
    JSONArray users = null;
    JSONArray users1 = null;
    //to read the content of the user input
    EditText inputLogin;
    EditText inputPassword;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);
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
				 //Intent myIntent = new Intent(LogInActivity.this, PlayScreenActivity.class);
				 Intent playscreen = new Intent(getApplicationContext(), PlayScreenActivity.class);
                 playscreen.putExtra("player",playerdata);  
				 
		            startActivity(playscreen);      
				    finish();
			}
		});
      	
      Register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(LogInActivity.this, RegisterActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
      
      Login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(LogInActivity.this, LogInActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
      
      LeaderBoard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(LogInActivity.this, LeaderBoardActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
      
      Quit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(LogInActivity.this, PlayScreenActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
      
      // Edit Text
      inputLogin = (EditText) findViewById(R.id.inputLogin);
      inputPassword = (EditText) findViewById(R.id.inputPassword);
     // Create button
      Button btnLogin = (Button) findViewById(R.id.btnlogin);
      Button btnCancel = (Button) findViewById(R.id.btncancel);
        // button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // creating Login check in background thread
                new LoginUser().execute();
               
                if(checkSuccess0 == true)
                {
                	Toast.makeText(getApplicationContext(), "Invalid User.", Toast.LENGTH_LONG).show();
                }
                
                if(checkSuccess3 == true)
                {
                	Toast.makeText(getApplicationContext(), "Missing Username or Password.", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
        	 
            @Override
            public void onClick(View view) {
                // cancel and go back to main thread
               
            }
        });
        
        
    }
        /**
         * Background Async Task to Create new product
         * */
        class LoginUser extends AsyncTask<String, String, String> {
     
            /**
             * Before starting background thread Show Progress Dialog
             * */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(LogInActivity.this);
                pDialog.setMessage("Logging in user..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
     
            /**
             *Checking for login
             * */
            protected String doInBackground(String... args) {
              
                String login = inputLogin.getText().toString();
                String password = inputPassword.getText().toString();
                
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
               
                params.add(new BasicNameValuePair("login", login));
                params.add(new BasicNameValuePair("password", password));
         
                Log.d(TAG_FAIL,"before Parser");
                
                // getting JSON Object
                // Note that Login url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_login,
                        "POST", params);
     
                Log.d(TAG_FAIL,"After Parser");
                // check log cat fro response
                Log.d("Create Response", json.toString());
     
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
                    	
                        player.Pid = c.getInt(TAG_PID);
                        player.Name = c.getString(TAG_NAME);
                        player.Total=c.getInt(TAG_TOTAL);
                        player.SaveTheeggs=c.getInt(TAG_SAVE);
                        player.FantasticFeathers=c.getInt(TAG_FANTASTIC);
                        player.TheWeapon=c.getInt(TAG_WEAPON);
                      
                        playerdata.add(player);
                       
                    }
                   
                    Intent playscreen = new Intent(getApplicationContext(), PlayScreenActivity.class);
                    playscreen.putExtra("player",playerdata);  
                    startActivity(playscreen);      
                    // closing this screen
                    finish();
                    } 
                    
                    
                    
                    //if success == 0, invalid user
                    if (success == 0)
                    {
                    	checkSuccess0 = true;
                    	
                    }
                    //if success == 3, missing username or password
                    if (success == 3)
                    {
                    	checkSuccess3 = true;
                    }
                    
                    
                    
                   
                } catch (JSONException e) {
                    e.printStackTrace();
                }
     
                return null;
            }
     
            /**
             * After completing background task Dismiss the progress dialog
             * **/
            protected void onPostExecute(String file_url) {
                // dismiss the dialog once done
                pDialog.dismiss();
            }
     
        }
 
 
	
}
