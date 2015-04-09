/**
 * Contains basic player information
 */
package com.jrdbnntt.aggravation.game;

import java.awt.Color;

public class Player {
	private Color color;	//their color on the board
	private String name;	//their ingame name put in by the user
	private int 
		homeMarbles = 0,	//number of marbles in home
		baseMarbles = 0,	//number of marbles in base
		freeMarbles = 0;	//number of marbles in loop or center
	
	public Player(Color c, String n) {
		this.color = c;
		this.name = n;
	}
	
	//Accessors
	public Color getColor()	{ return this.color; }
	public String getName()	{ return this.name;	}
	public int getHomeMarbles() { return this.homeMarbles; }
	public int getBaseMarbles() { return this.baseMarbles; }
	public int getFreeMarbles() { return this.freeMarbles; }
	
}
