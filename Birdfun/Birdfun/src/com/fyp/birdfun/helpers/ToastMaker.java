package com.fyp.birdfun.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class ToastMaker {
	  public static  void toastNow(String message,Context context)
	    {
	        Toast toast = Toast.makeText(context,message, Toast.LENGTH_LONG);
	        toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2, toast.getYOffset() / 2);

	        TextView textView = new TextView(context);
	        				
	        textView.setBackgroundColor(Color.argb(255, 255, 28, 28));
	        textView.setTextColor(Color.argb(255,0,208,208));
	        textView.setTextSize(25);
	        Typeface typeface = Typeface.create("serif", Typeface.BOLD);
	        textView.setTypeface(typeface);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setText(message);
	        toast.setView(textView);
	        toast.show();
	    }

	

}
