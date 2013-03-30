package com.fyp.birdfun.helpers;



public class Card {
// Every card has a id no
 public int card_id;
 //image to draw in the front
 public int front_drawable_id;
 
 public int back_drawable_id;
 //this has the resource id of the front view
 
 
 public int front_view_id;
 
 public int back_view_id;
 
 public boolean solved;
 //this is set if the card solved in the current round.
 public int play_id;
 
 public int position_id;
 public Card(int id,int front_drawable,int back_drawable)
 {
	 card_id=id;
	 front_drawable_id=front_drawable;
	 back_drawable_id=back_drawable;
	 solved=false;
	 front_view_id=0;
	 play_id=0;
	 position_id=0;
 }


}
