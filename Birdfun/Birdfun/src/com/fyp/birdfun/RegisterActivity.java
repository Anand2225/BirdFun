package com.fyp.birdfun;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.fyp.birdfun.helpers.JSONParser;
import com.fyp.birdfun.helpers.PlayerDetails;
import com.fyp.birdfun.helpers.ToastMaker;
 
public class RegisterActivity extends Activity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();

	// To hold Editabe Text in this activity 
    EditText inputName;
    EditText inputAge;
    EditText inputSchool;
    EditText inputLogin;
    EditText inputPassword;
    // url to create new product
  //  private static String url_register_user = "http://172.23.194.178/register_user.php";
    private static String url_register_user="http://birdfun.net/register_user.php?";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FAIL = "success";
    
    ArrayList<PlayerDetails> playerdata = new ArrayList<PlayerDetails>();
    
    boolean registercheck1=false;
    boolean registercheck2=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

       Button Play= (Button) findViewById(R.id.btnplays);
       Button Register= (Button) findViewById(R.id.btnregiste);
       Button Login= (Button) findViewById(R.id.btnlogins);
       Button LeaderBoard= (Button) findViewById(R.id.btnleaderboard);
       Button Quit= (Button) findViewById(R.id.btnquit);
       Button Help = (Button) findViewById(R.id.btnhelp);
       
       Help.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        Intent changeScreen = new Intent(RegisterActivity.this,
	        HelpActivity.class);
	        startActivity(changeScreen);      
			    finish();
	        
	        }
	        });
      // To the hidden menu option
     Play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			//	 intent listener to open the specific activity
				 Intent myIntent = new Intent(RegisterActivity.this, PlayScreenActivity.class);
				 myIntent.putExtra("player",playerdata);  

		            startActivity(myIntent);      
				    finish();
			}
		});
     	
     Register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	 		    Intent intent = new Intent(Intent.ACTION_MAIN);
	 			intent.addCategory(Intent.CATEGORY_HOME);
	 			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 			startActivity(intent);
			}
		});
     
     Login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(RegisterActivity.this, LogInActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
     
     LeaderBoard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(RegisterActivity.this, LeaderBoardActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
     
     Quit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_MAIN);
	 			intent.addCategory(Intent.CATEGORY_HOME);
	 			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 			startActivity(intent);


			}
		});
     
        
        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputAge = (EditText) findViewById(R.id.inputAge);
        inputSchool = (EditText) findViewById(R.id.inputSchool); 
        inputLogin = (EditText) findViewById(R.id.inputLogin);
        inputPassword=(EditText) findViewById(R.id.inputPassword);
        		
        // Create button
        Button btnRegister = (Button) findViewById(R.id.btnregister);
        Button btnCancel = (Button) findViewById(R.id.btncancel);
        // button click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // creating new product in background thread
            	
            	if(haveNetworkConnection(RegisterActivity.this))
            	{	
            	
                new CreateNewUser().execute();
            	}
            	else
            	{
            		ToastMaker.toastNow("Please check your Internet connnection ",getApplicationContext());
            	}
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(RegisterActivity.this, PlayScreenActivity.class);

	            startActivity(myIntent);      
			    finish();
		
			}
		});
        if(registercheck1 == true)
        {
        	ToastMaker.toastNow("User name already taken",getApplicationContext());
        	//Toast.makeText(getApplicationContext(), "Invalid User.", Toast.LENGTH_LONG).show();
        	registercheck1=false;
        }
        
        if(registercheck2 == true)
        {
        	ToastMaker.toastNow("Please complete the form. ",getApplicationContext());
        	registercheck2=false;
        }
    }
    
    public static boolean haveNetworkConnection(final Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        final ConnectivityManager cm = (ConnectivityManager) context
                     .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
               final NetworkInfo[] netInfo = cm.getAllNetworkInfo();
               for (final NetworkInfo netInfoCheck : netInfo) {
                     if (netInfoCheck.getTypeName().equalsIgnoreCase("WIFI")) {
                            if (netInfoCheck.isConnected()) {
                                   haveConnectedWifi = true;
                            }
                     }
                     if (netInfoCheck.getTypeName().equalsIgnoreCase("MOBILE")) {
                            if (netInfoCheck.isConnected()) {
                                   haveConnectedMobile = true;
                            }
                     }
               }
        }

        return haveConnectedWifi || haveConnectedMobile;
 }

 
    /**
     * Background Async Task to Create new product
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Registering user..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String age = inputAge.getText().toString();
            String school = inputSchool.getText().toString();
            String login = inputLogin.getText().toString();
            String password = inputPassword.getText().toString();
            
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("age", age));
            params.add(new BasicNameValuePair("school", school));
            params.add(new BasicNameValuePair("login", login));
            params.add(new BasicNameValuePair("password", password));
 
            Log.d( TAG_FAIL,"Before Json");
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_register_user,
                    "POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully created product
                	
                    Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                      
                    startActivity(i);      
                    // closing this screen
                    finish();
                } 
                else if (success==2){
                	registercheck1=true;
                }
                else if (success==3)
                {
                	registercheck2=true;
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