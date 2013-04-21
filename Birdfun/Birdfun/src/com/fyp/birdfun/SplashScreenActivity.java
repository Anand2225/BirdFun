package com.fyp.birdfun;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.fyp.birdfun.helpers.SoundPoolManager;

public class SplashScreenActivity extends Activity  {
	
	 SoundPoolManager soundManger;
	 SoundPool sounds;
	 private int sExplosion;
	 
	 private int soundID;
	 boolean loaded=false;
	 
		protected int _splashTime = 2000; 
	 private static int CORRECT_SOUNDS=5;
		
	private static int WRONG_SOUNDS=4;
	
	private Thread splashTread;
	 
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        // making it full screen
	       
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        GLSurfaceView surface=new GLSurfaceView(this);
	    
	        setContentView(R.layout.splash_screen);
	       
	        Log.d("loaded",""+loaded);
	        new DoInBackground().execute();
	        
	        // thread for displaying the SplashScreen
		    splashTread = new Thread() {
		        @Override
		        public void run() {
		            try {	            	
		            	synchronized(this){
		            		wait(_splashTime);
		            	}
		            	
		            } catch(InterruptedException e) {} 
		            finally {
		                finish();
		                
		                Intent i = new Intent();
		                i.setClass(SplashScreenActivity.this, PlayScreenActivity.class);
		        		startActivity(i);
		        		   finish();
		                
		                
		            }
		        }
		    };
		    
		    splashTread.start();
	       
	        
  }

	 private class DoInBackground extends AsyncTask<Void, Void, Void> implements DialogInterface.OnCancelListener
     {
     private ProgressDialog dialog;

    protected void onPreExecute() 
    {
    	dialog = ProgressDialog.show(SplashScreenActivity.this, "", "Loading. Please wait...", true);
    }

    protected Void doInBackground(Void... unused) 
    { 
    	 int correctid[]=new int[CORRECT_SOUNDS];
	    	int wrongid[]=new int [WRONG_SOUNDS];    	 
		    	
	    	    correctid[0]= R.raw.correctsound1;
		     	correctid[1]= R.raw.correctsound1;
		     	correctid[2]= R.raw.correctsound1;
		     	correctid[3]= R.raw.correctsound1;
		     	correctid[4]= R.raw.correctsound1;
		     	
		     	wrongid[0] =  R.raw.wrongsound1;
		     	wrongid[1] =  R.raw.wrongsound2;
		     	wrongid[2] =  R.raw.wrongsound3;
		     	wrongid[3] =  R.raw.wrongsound4;
		     	soundManger=new SoundPoolManager(getApplicationContext());	 
		       	 soundManger.load(correctid,wrongid);
		     
    	
    	 return null; 
    }

    protected void onPostExecute(Void unused) 
    { 
    	dialog.dismiss(); 	
          	 
	        	  ((GlobalLoginApplication)getApplication()).setSounds(soundManger);
	        	   
	        	   Intent i = new Intent();
	               
	      
    }

    public void onCancel(DialogInterface dialog) 
    { 
    	cancel(true); 
    	dialog.dismiss(); 	
    }
}
}
