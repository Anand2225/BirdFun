package com.fyp.birdfun;



import android.app.Activity;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends Activity  {
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        // making it full screen
	       
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        GLSurfaceView surface=new GLSurfaceView(this);
	        setContentView(R.layout.splash_screen);
	        
  }

		
}
