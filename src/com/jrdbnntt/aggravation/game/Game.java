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
import com.jrdbnntt.aggravation.board.Marble;
import com.jrdbnntt.aggravation.board.space.Space;
import com.jrdbnntt.aggravation.game.Player.Status;

public class Game implements ActionListener {
	public static enum Status {
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
	
	//Turn vars
	private ArrayList<Space> possibleEndpoints;		//possible moves from choice
	private Space selectedSource;					//has the marble
	private Space selectedDestination;				//has no marble, where it is moved to
	private boolean marbleMoved;
	
	
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
		Log.d("GAME", "New turn started for Player #"
				+ turnOrder.get(currentPlayer) + ", \'"
				+ this.getCurrentPlayer().getName()+"\'");
		
		marbleMoved = false;
		display.getToolBox().addLogMessage(
				this.getCurrentPlayer().getName()
				+" it is your turn to roll!",false);
		display.getToolBox().getRollButton().setEnabled(true);
		display.getToolBox().getRollButton().addActionListener(this);
		updatePlayers();
		
		this.setStatus(Game.Status.WAITING_FOR_ROLL);
		display.refresh();
	}
	
	private void endCurrentTurn() {
		//Check for end game  TODO
		if(false) {
			this.end();
			this.setStatus(Game.Status.ENDED);
		} else {
			//Switch to next player, or stay the same if 6
			if(roll != 6 && marbleMoved) {
				++currentPlayer;
				if(currentPlayer == turnOrder.size())
					currentPlayer = 0;
			}
			this.startTurn();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Log.d("GAME-ACTION", e.getActionCommand());
		switch(e.getActionCommand()) {
		case Game.AK_ROLL:
			display.getToolBox().getRollButton().setEnabled(false);
			if(this.currentStatus == Game.Status.WAITING_FOR_ROLL) {
				this.setStatus(Game.Status.PROCESSING);
				roll = this.rand.nextInt(Game.DIE_SIDES)+1;
				display.getToolBox().addLogMessage(getCurrentPlayer().getName() + " rolls " + roll);
				display.getToolBox().addLogMessage(getCurrentPlayer().getName() + ", Please choose your next move",false);
				this.setStatus(Game.Status.WAITING_FOR_MARBLE_SELECTION);
			}
			
		}
	}
	
	private void setStatus(Status s) {
		Log.d("GAME-Status Change",s.name());
		this.currentStatus = s;
	}
	public Game.Status getStatus() {
		return this.currentStatus;
	}
	
	public void onSpaceClicked(Space s) {
		final String KEY = "GAME-SpaceSelect";
		switch(this.currentStatus) {
		case WAITING_FOR_MARBLE_SELECTION:
			if(chooseMarbleSource(s)) {
				Log.d(KEY, this.getCurrentPlayer().getName() + " selected marble at " + s.getLabel());
				this.setStatus(Game.Status.WAITING_FOR_MOVE_CHOICE);
			}
			break;
		case WAITING_FOR_MOVE_CHOICE:
			if(checkMarbleDestination(s)) {
				this.setStatus(Game.Status.PROCESSING);
				Log.d(KEY, this.getCurrentPlayer().getName() + " selected open space at " + s.getLabel());
				this.selectedDestination = s;
				this.makeMove();
			} else if(chooseMarbleSource(s)) {
				Log.d(KEY, this.getCurrentPlayer().getName() + " selected marble at " + s.getLabel());
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * Checks to see if the current player can move this marble, then does so if is valid.
	 * @return
	 */
	private boolean chooseMarbleSource(Space s) {
		if(s.hasMarble() && s.getMarble().getOwner() == this.getCurrentPlayer()) {
			this.selectedSource = s;
			s.setFocus(true);
			display.refresh();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks to see if the current player can move the currentSource here.
	 * @return
	 */
	private boolean checkMarbleDestination(Space s) {
		return true;
	}
	
	/**
	 * Performs marble movement
	 */
	private void makeMove() {
		Marble m = this.selectedSource.getMarble();
		
		//make swap
		this.selectedSource.clearMarble();
		this.selectedSource.setFocus(false);
		this.selectedDestination.setMarble(m);
		
		Log.d("GAME-Move", "Marble moved from "+this.selectedSource.getLabel()+" to "+this.selectedDestination.getLabel());
		this.marbleMoved = true;
		this.endCurrentTurn();
	}
	
	
}
