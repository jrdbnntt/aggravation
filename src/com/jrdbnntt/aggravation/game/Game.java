/**
 * Handles game turn management.
 */
package com.jrdbnntt.aggravation.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import com.jrdbnntt.aggravation.Aggravation;
import com.jrdbnntt.aggravation.board.space.Space;
import com.jrdbnntt.aggravation.game.Player.Status;

public class Game {
	private static enum STATUS {
		NEW, STARTED, ENDED
	}
	
	private static final Game CURRENT_INSTANCE = new Game();
	
	private Player[] players = new Player[Aggravation.MAX_PLAYERS];
	private ArrayList<Integer> turnOrder = new ArrayList<Integer>();	//Randomized order of player indexes
	private int currentPlayer;				//Current index in turnOrder
	private GameDisplay display;
	private Game.STATUS currentStatus = Game.STATUS.NEW;
	
	private ArrayList<Space> pSpaces;
	
	public Game() {
		init();
	}
	
	public static Game getCurrentInstance() {
		return Game.CURRENT_INSTANCE;
	}
	
	/**
	 * Initializes empty game=
	 */
	public void init() {
		//initialize with a null set of players
		for(int i = 0; i < this.players.length; ++i)
			this.players[i] = null;
		
		this.currentStatus = Game.STATUS.NEW;
		System.out.println("GAME: Initialized.");
	}
	
	public void start() {
		System.out.println("GAME: START");
		definePlayers();
		this.display = new GameDisplay();
		this.currentStatus = Game.STATUS.STARTED;
		this.startTurn();
	}
	
	public void end() {
		System.out.println("GAME: END");
		this.currentStatus = Game.STATUS.ENDED;
		
		//update statuses
		for(int i : turnOrder)
			players[i].setStatus(Status.LOSER);
		this.getCurrentPlayer().setStatus(Status.WINNER);
		
	}
	
	
	
	/**
	 * Sets up the players in the game
	 */
	private void definePlayers() {
		//prompt for user creation TODO
		
		//For now, just make default ones. Null = no player
		this.players[0] = new Player(Color.RED, "Player 1");
//		this.players[1] = new Player(Color.ORANGE, "Player 2");
//		this.players[2] = new Player(Color.BLUE, "Player 3");
		this.players[3] = new Player(Color.WHITE, "Player 4");
//		this.players[4] = new Player(Color.GREEN, "Player 5");
		this.players[5] = new Player(Color.YELLOW, "Player 6");
		
		//Create turn order
		turnOrder = new ArrayList<Integer>();
		for(int i = 0; i < Aggravation.MAX_PLAYERS; ++i)
			if(this.players[i] != null)
				turnOrder.add(i);
		Collections.shuffle(turnOrder);
		
		
		currentPlayer = 0;
		
		System.out.print("GAME: "+turnOrder.size()+" Players defined with order ");
		for(int i : turnOrder)
			System.out.print(i+" ");
		System.out.println();
	}
	
	
	/**
	 * Updates player information based upon current player + game status
	 */
	public void updatePlayers() {
		//Reset statuses
		for(int i : turnOrder)
			players[i].setStatus(Status.WAITING);
		this.getCurrentPlayer().setStatus(Status.CURRENT_PLAYER);
	}
	
	/**
	 * Retrieve player by index
	 * @param i
	 * @throws NullPointerException when player has not been set
	 * @return
	 */
	public Player getPlayer(int i) throws NullPointerException {
		return this.players[i];
	}
	public ArrayList<Integer> getTurnOrder() {
		return this.turnOrder;
	}
	
	public Player getCurrentPlayer() {
		return players[turnOrder.get(currentPlayer)];
	}
	
	public GameDisplay getDisplay() {
		return this.display;
	}
	
	
	/**
	 * Handle the current turn
	 */
	private void startTurn() {		
		
		promptRoll();
		updatePlayers();
		display.refresh();
	}
	
	private void promptRoll() {
		
	}
	
	
	private void endCurrentTurn() {
		//Check for end game  TODO
		if(true) {
			this.end();
		} else {
			//Set to next player in order (rotating back to start)
			++currentPlayer;
			if(currentPlayer == turnOrder.size())
				currentPlayer = 0;
		}
	}
	
}
