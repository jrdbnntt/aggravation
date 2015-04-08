/**
 * Main class to manage the entire game.
 */

package com.jrdbnntt.aggravation;

import javax.swing.JFrame;

class Aggravation extends JFrame {
	static final String GAME_TITLE = "Aggravation - by Jared Bennett";
	static final int START_WIDTH = 1000;
	static final int START_HEIGHT = 1000;
	
	
	public static void main(String[] args){
		Aggravation game = new Aggravation(GAME_TITLE);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setSize(START_WIDTH,START_HEIGHT);
		game.setVisible(true);
	}
	
	Aggravation(String title) {
		super(title);
		
	}
}