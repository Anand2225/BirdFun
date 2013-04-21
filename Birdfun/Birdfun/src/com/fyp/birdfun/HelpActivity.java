package com.fyp.birdfun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class HelpActivity extends Activity {

	 WebView wv;
	public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
	 setContentView(R.layout.help_layout);
	
     wv = (WebView)findViewById(R.id.webview);

     wv.getSettings().setJavaScriptEnabled(true);
     
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
				 Intent myIntent = new Intent(HelpActivity.this, PlayScreenActivity.class);

		            startActivity(myIntent);      
				    finish();
			}
		});
   	
   Register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(HelpActivity.this, RegisterActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
   
   Login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(HelpActivity.this, LogInActivity.class);

			            startActivity(myIntent);      
					    finish();
				

			}
		});
   
   LeaderBoard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	 intent listener to open the specific activity
					 Intent myIntent = new Intent(HelpActivity.this, LeaderBoardActivity.class);

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
  
   wv.loadUrl("file:///android_asset/www/gallery/index.html");
   wv.setWebViewClient(new HelpViewClient());
	}
	private class HelpViewClient extends WebViewClient {
		public boolean overRideURlloading(WebView webview,String url )
		{
			 if (Uri.parse(url).getHost().equals("file:///android_asset/www/circle.html")) {
			     // do not override; let my WebView load the page
			     return false;
			 }
			 return true;
		}
		
	}
	
}
