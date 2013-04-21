package com.fyp.birdfun;

import android.app.Application;

import com.fyp.birdfun.helpers.PlayerDetails;
import com.fyp.birdfun.helpers.SoundPoolManager;

public class GlobalLoginApplication extends Application{
	
	private PlayerDetails currentPlayer=new PlayerDetails() ;
	private boolean loggedIn = false;
	public SoundPoolManager sounds=new SoundPoolManager(this) ;
	
	
	public PlayerDetails getPlayerDetails(){
		return currentPlayer;
	}
	
	public void setPlayerDetails(PlayerDetails Details){
		currentPlayer = Details;
		loggedIn=true;
	}
	
	public void resetPlayerDetails(PlayerDetails Details){
		currentPlayer = null;
		loggedIn=false;
	}
	
	@Override
	public void onCreate(){
	 currentPlayer= new PlayerDetails();
	 loggedIn=false;
	}
	
	public boolean loginStatus(){
		return loggedIn;
	}
   
	public void setSounds(SoundPoolManager sounds)
	{
		this.sounds=sounds;
	}

}
