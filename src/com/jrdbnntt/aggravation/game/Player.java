/**
 * Contains basic player information
 */
package com.jrdbnntt.aggravation.game;

import java.awt.Color;

public class Player {
	public static enum Status {
		CURRENT_PLAYER, WAITING, WINNER, LOSER
	}
	
	public static final Player NONE = new Player(0,new Color(0,0,0,0), "[NONE]");
	
	private Color color;	//their color on the board
	private String name;	//their in-game name put in by the user
	private Status status = Player.Status.WAITING;
	private int zone;		//board zone assigned to player
	
	public Player(int zone, Color c, String n) {
		this.zone = zone;
		this.color = c;
		this.name = n;
	}
	
	//Mutators
	public void setStatus(Status s){
		this.status = s;
	}
	
	//Accessors
	public Color getColor()	{ return this.color; }
	public String getName()	{ return this.name;	}
	public int getZone() { return this.zone; }
	public Player.Status getStatus() { return this.status; }
	public String getStatusString() { 
		switch(this.status) {
		case CURRENT_PLAYER: return "Current Player";
		case WAITING: return "Waiting";
		case WINNER: return "Winner";
		case LOSER: return "Loser";
		default: return "?";
		}
		
	}
	
	@Override
	public String toString() { return this.name; }
	
}
