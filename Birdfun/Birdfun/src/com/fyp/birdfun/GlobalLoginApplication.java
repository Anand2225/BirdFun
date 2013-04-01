package com.fyp.birdfun;

import com.fyp.birdfun.helpers.PlayerDetails;

import android.app.Application;

public class GlobalLoginApplication extends Application{
	private PlayerDetails currentPlayer=new PlayerDetails() ;
	private boolean loggedIn = false;
 
	
	public PlayerDetails getPlayerDetails(){
		return currentPlayer;
	}
	
	public void setPlayerDetails(PlayerDetails Details){
		currentPlayer = Details;
		loggedIn=true;
	}
	
	@Override
	public void onCreate(){
	 currentPlayer= new PlayerDetails();
	 loggedIn=false;
	}
	
	public boolean loginStatus(){
		return loggedIn;
	}


}
