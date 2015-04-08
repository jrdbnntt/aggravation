/**
 * Handles game turn management.
 */
package com.jrdbnntt.aggravation.game;

import java.awt.Color;

import com.jrdbnntt.aggravation.Aggravation;

public class Game {
	private static Game currentInstance = new Game();
	
	private Player[] players = new Player[Aggravation.MAX_PLAYERS];
	
	public Game() {
		definePlayers();
	}
	
	public static Game getCurrentInstance() {
		return Game.currentInstance;
	}
	
	
	/**
	 * Sets up the players in the game
	 */
	private void definePlayers() {
		//For now, just make default ones.
		this.players[0] = new Player(Color.RED, "Player 1");
		this.players[1] = new Player(Color.ORANGE, "Player 2");
		this.players[2] = new Player(Color.BLUE, "Player 3");
		this.players[3] = new Player(Color.WHITE, "Player 4");
		this.players[4] = new Player(Color.GREEN, "Player 5");
		this.players[5] = new Player(Color.YELLOW, "Player 6");
	}
	
	/**
	 * Retrieve player by index
	 * @param num
	 * @return
	 */
	public Player getPlayer(int i) {
		return this.players[i];
	}
	
}
