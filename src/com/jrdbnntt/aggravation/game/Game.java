/**
 * Handles game turn management.
 */
package com.jrdbnntt.aggravation.game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JButton;

import com.jrdbnntt.aggravation.Aggravation;
import com.jrdbnntt.aggravation.Util.Log;
import com.jrdbnntt.aggravation.board.space.Space;
import com.jrdbnntt.aggravation.game.Player.Status;

public class Game implements ActionListener {
	private static enum Status {
		NEW, STARTED, ENDED, 
		WAITING_FOR_ROLL, 
		WAITING_FOR_MARBLE_SELECTION,
		WAITING_FOR_MOVE_CHOICE,
		PROCESSING
	}
	public static final String
		AK_ROLL = "AK_ROLL";
	
	private static final Game CURRENT_INSTANCE = new Game();
	private static final int DIE_SIDES = 6;
	
	private Player[] players = new Player[Aggravation.MAX_PLAYERS];
	private ArrayList<Integer> turnOrder = new ArrayList<Integer>();	//Randomized order of player indexes
	private int currentPlayer;				//Current index in turnOrder
	private GameDisplay display;
	private Game.Status currentStatus = Game.Status.NEW;
	private int roll = 0;	//current turn role
	private Random rand = new Random(System.currentTimeMillis());
	
	private ArrayList<Space> possibleEndpoints;		//possible moves from choice
	private Space currentChoice;
	
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
		
		this.setStatus(Game.Status.NEW);
		Log.d("GAME","Initialized.");
	}
	
	public void start() {
		this.setStatus(Game.Status.STARTED);
		definePlayers();
		this.display = new GameDisplay();
		this.startTurn();
	}
	
	public void end() {
		Log.d("GAME","END");
		this.setStatus(Game.Status.ENDED);
		
		//update statuses
		for(int i : turnOrder)
			players[i].setStatus(Player.Status.LOSER);
		this.getCurrentPlayer().setStatus(Player.Status.WINNER);
		
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
		this.players[4] = new Player(Color.GREEN, "Player 5");
		this.players[5] = new Player(Color.YELLOW, "Player 6");
		
		//Create turn order
		turnOrder = new ArrayList<Integer>();
		for(int i = 0; i < Aggravation.MAX_PLAYERS; ++i)
			if(this.players[i] != null)
				turnOrder.add(i);
		Collections.shuffle(turnOrder);
		
		
		currentPlayer = 0;
		
		String str = turnOrder.size()+" Players defined with order ";
		for(int i : turnOrder)
			str+= i+" ";
		Log.d("GAME", str);
	}
	
	
	/**
	 * Updates player information based upon current player + game status
	 */
	public void updatePlayers() {
		//Reset statuses
		for(int i : turnOrder)
			players[i].setStatus(Player.Status.WAITING);
		this.getCurrentPlayer().setStatus(Player.Status.CURRENT_PLAYER);
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
		display.getToolBox().addLogMessage(
				this.getCurrentPlayer().getName()
				+" it is your turn to roll!",false);
		display.getToolBox().getRollButton().setEnabled(true);
		display.getToolBox().getRollButton().addActionListener(this);
		updatePlayers();
		
		this.setStatus(Game.Status.WAITING_FOR_MOVE_CHOICE);
		display.refresh();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		Log.d("GAME-ACTION", e.getActionCommand());
		switch(e.getActionCommand()) {
		case Game.AK_ROLL:
			roll = this.rand.nextInt(Game.DIE_SIDES)+1;
			display.getToolBox().getRollButton().setEnabled(false);
			display.getToolBox().addLogMessage(getCurrentPlayer().getName() + " rolls " + roll);
			display.getToolBox().addLogMessage(getCurrentPlayer().getName() + ", Please choose your next move",false);
			this.setStatus(Game.Status.WAITING_FOR_MARBLE_SELECTION);
		}
	}
	
	private void setStatus(Status s) {
		Log.d("GAME-Status Change",s.name());
		this.currentStatus = s;
	}
	
	
}
