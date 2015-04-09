/**
 * Contains basic player information
 */
package com.jrdbnntt.aggravation.game;

import java.awt.Color;

public class Player {
	public static enum Status {
		CURRENT_PLAYER, WAITING, WINNER, LOSER
	}
	
	public static final Player NONE = new Player(new Color(0,0,0,0), "[NONE]");
	
	private Color color;	//their color on the board
	private String name;	//their in-game name put in by the user
	private Status status = Player.Status.WAITING;
	private int 
		homeMarbles = 0,	//number of marbles in home
		baseMarbles = 0,	//number of marbles in base
		freeMarbles = 0;	//number of marbles in loop or center
	
	public Player(Color c, String n) {
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
	public int getHomeMarbles() { return this.homeMarbles; }
	public int getBaseMarbles() { return this.baseMarbles; }
	public int getFreeMarbles() { return this.freeMarbles; }
	public String getStatus() { 
		switch(this.status) {
		case CURRENT_PLAYER: return "Current Player";
		case WAITING: return "Waiting";
		case WINNER: return "Winner";
		case LOSER: return "Loser";
		default: return "?";
		}
		
	}
	
}
