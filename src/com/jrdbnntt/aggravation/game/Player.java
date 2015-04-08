/**
 * Contains basic player information
 */
package com.jrdbnntt.aggravation.game;

import java.awt.Color;

public class Player {
	private Color color;	//their color on the board
	private String name;	//their ingame name put in by the user
	
	public Player(Color c, String n) {
		this.color = c;
		this.name = n;
	}
	
	//Accessors
	public Color getColor()	{ return this.color; }
	public String getName()	{ return this.name;	}
	
}
