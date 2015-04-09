/**
 * Handles game turn management.
 */
package com.jrdbnntt.aggravation.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import com.jrdbnntt.aggravation.Aggravation;

public class Game {
	private static Game currentInstance = new Game();
	
	private Player[] players = new Player[Aggravation.MAX_PLAYERS];
	private ArrayList<Integer> turnOrder;	//Randomized order of player indexes
	private int currentPlayer;				//Current index in turnOrder
	
	
	public Game() {
		
	}
	
	public static Game getCurrentInstance() {
		return Game.currentInstance;
	}
	
	
	public void start() {
		definePlayers();
	}
	
	public void end() {
		
	}
	
	
	/**
	 * Sets up the players in the game
	 */
	private void definePlayers() {
		//prompt for user creation TODO
		
		//For now, just make default ones. Null = no player
		this.players[0] = new Player(Color.RED, "Player 1");
		this.players[1] = new Player(Color.ORANGE, "Player 2");
		this.players[2] = new Player(Color.BLUE, "Player 3");
//		this.players[3] = new Player(Color.WHITE, "Player 4");
		this.players[4] = new Player(Color.GREEN, "Player 5");
		this.players[5] = new Player(Color.YELLOW, "Player 6");
		
		//Create turn order
		turnOrder = new ArrayList<Integer>();
		for(int i = 0; i < Aggravation.MAX_PLAYERS; ++i)
			turnOrder.add(i);
		Collections.shuffle(turnOrder);
		
		//Remove non-players
		for(int i = 0; i < turnOrder.size(); ++i) {
			if(this.players[turnOrder.get(i)] == null)
				turnOrder.remove(i);
		}
		
		//Print order (DEBUG)
		for(int i : turnOrder)
			System.out.println(i);
		
		
		currentPlayer = 0;
	}
	
	/**
	 * Retrieve player by index
	 * @param num
	 * @return
	 */
	public Player getPlayer(int i) {
		return this.players[i];
	}
	
	public Player getCurrentPlayer() {
		return players[turnOrder.get(currentPlayer)];
	}
	
	public void endCurrentTurn() {
		//Check for end game  TODO
		
		//Set to next player in order (rotating back to start)
		++currentPlayer;
		if(currentPlayer == turnOrder.size())
			currentPlayer = 0;
	}
	
}
