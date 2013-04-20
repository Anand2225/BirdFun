package com.fyp.birdfun.helpers;

import java.util.Random;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundPoolManager {
	
	private Context pContext;		// Local copy of app context
	//intialize two sound Pools
	private SoundPool correctSoundPool;		// Our SoundPool instance
	private SoundPool wrongSoundPool;		// Our SoundPool instance
	
	//private static int NO_BACK_SOUNDS=3;
	private static int CORRECT_SOUNDS=5;
	
	private static int WRONG_SOUNDS=4;
	
	//Settings for the sounds
	private float rate = 1.0f;		// Playback rate
	private float masterVolume = 1.0f;	// Master vloume level
	private float leftVolume = 1.0f;	// Volume levels for left and right channels
	private float rightVolume = 1.0f;
	private float balance = 0.5f;		// A balance value used to calculate left/right volume levels
    private int[] correctsoundid;
    
    private int[] wrongsoundid;
    
    private int mysound;
	private boolean loaded=false;
	
	
	
    public SoundPoolManager( Context appContext){
    	//intialize sounds pool
    	correctSoundPool=new SoundPool(CORRECT_SOUNDS, AudioManager.STREAM_MUSIC, 0);
    	wrongSoundPool=new  SoundPool(WRONG_SOUNDS, AudioManager.STREAM_MUSIC, 0);
    	
    	//intialize context
    	
    	correctsoundid=new int[CORRECT_SOUNDS];
    	wrongsoundid=new int[WRONG_SOUNDS];
    	pContext = appContext;
    }
  
 // Load up a sound and return the id
 	public void load(int[] correct_id,int [] wrong_id)
 	{
 		for(int i=0;i<correct_id.length;i++)
 		{
 			correctsoundid[i]= correct_id[i];
 			
 		}
 		for(int j=0;j<wrong_id.length;j++)
 		{
 			wrongsoundid[j]= wrong_id[j];
 		}
 		
 	}
    public void playcorrectSound()
    {
    
    	int playsound= getrandomnumber(CORRECT_SOUNDS);
    	mysound = correctSoundPool.load(pContext,correctsoundid[playsound] , 1);

    	correctSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
    	    public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
    	    	correctSoundPool.play(mysound, leftVolume, rightVolume, 0, 0, rate);
    	    }
    	});

 
    }
    public void playwrongSound()
    {
    	int playsound= getrandomnumber(WRONG_SOUNDS);
    	mysound = wrongSoundPool.load(pContext,wrongsoundid[playsound] , 1);

    	wrongSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
    	    public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
    	    	wrongSoundPool.play(mysound, leftVolume, rightVolume, 0, 0, rate);
    	    }
    	});
 
    }
    
    public int  getrandomnumber(int upto){
    	int randomInt=0;
    	
    	long seed =System.currentTimeMillis();
    	Random randomGenerator=new Random(seed);
    	 randomInt=randomGenerator.nextInt(upto);
    	return randomInt;
    }

}