package com.fyp.birdfun.helpers;

import android.os.Parcel;
import android.os.Parcelable;

/*This to hold the player details pulled from database */
public class PlayerDetails implements Parcelable  {

	public int Pid=0;
	public String Name="name";
	public int Age=0;
	public String School="school";
	public int Rank=0;
	public String Login="login";
	public int Total=0;
	public int SaveTheeggs=0;
	public int FantasticFeathers=0;
	public int TheWeapon=0;
	// No-arg Ctor
	
	public PlayerDetails(){}	 
	 
	public PlayerDetails(Parcel in)
	    {
		 	this.Pid = in.readInt();
	        this.Name = in.readString();
	        this.Age=in.readInt();
	        this.School=in.readString();
	        this.Login=in.readString();
	        this.Total = in.readInt();
	        this.SaveTheeggs=in.readInt();
	        this.FantasticFeathers=in.readInt();
	        this.TheWeapon=in.readInt();
	    }
		
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flag) {
		// TODO Auto-generated method stub
			dest.writeInt(Pid);
	        dest.writeString(Name);
	        dest.writeString(Login);
	        dest.writeInt(Total);
	        dest.writeInt(SaveTheeggs);
	        dest.writeInt(FantasticFeathers);
	        dest.writeInt(TheWeapon);
	}
			@SuppressWarnings("unchecked")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public PlayerDetails createFromParcel(Parcel in)
	    {return new PlayerDetails(in);}
	 
	    public PlayerDetails[] newArray(int size)
	    {return new PlayerDetails[size];}
	    
	};
 
	public void updatesavetheeggs(int savetheeggsscore){
				
	}
	public void updatetheweapon(int theweaponscore){
		int new_total=this.Total+theweaponscore;
		if(new_total>=Total)
			this.Total=new_total;
		
		if(this.TheWeapon>=theweaponscore)
			this.TheWeapon+=theweaponscore;
	}
	public void updatefatasticfeathers(){
		
	}

	
	
}