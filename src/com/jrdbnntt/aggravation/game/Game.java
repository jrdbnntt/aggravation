/**
 * Handles game turn management.
 */
package com.jrdbnntt.aggravation.game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JButton;

import com.jrdbnntt.aggravation.Aggravation;
import com.jrdbnntt.aggravation.Util.Log;
import com.jrdbnntt.aggravation.board.Marble;
import com.jrdbnntt.aggravation.board.space.HomeSpace;
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
	
	private static Game currInstance;
	private static final int DIE_SIDES = 6;
	
	private Player[] players = new Player[Aggravation.MAX_PLAYERS];
	private ArrayList<Integer> turnOrder = new ArrayList<Integer>();	//Randomized order of player indexes
	private GameDisplay display;
	private Game.Status currentStatus = Game.Status.NEW;
	private int roll = 0;	//current turn role
	private Random rand = new Random(System.currentTimeMillis());
	
	//Turn vars
	private int currPlayerIndex;				//Current index in turnOrder
	private Player currPlayer;					//Ref to current player
	private Map<Space,ArrayList<Space>> allPossDst;		//possible moves from choice
	private Space selectedSource;					//has the marble
	private Space selectedDestination;				//has no marble, where it is moved to
	private boolean marbleMoved;
	
	
	public Game() {
		init();
	}
	
	
	public static void load() {
		Game.currInstance = new Game();
	}
	public static Game getCurrentInstance() {
		return Game.currInstance;
	}
	
	/**
	 * Initializes empty game=
	 */
	public void init() {
		//initialize with a null set of players
		for(int i = 0; i < this.players.length; ++i)
			this.players[i] = null;
		
		this.setStatus(Game.Status.NEW);
		Log.v("GAME","Initialized.");
	}
	
	public void start(Player[] pSet) {
		this.setStatus(Game.Status.STARTED);
		definePlayers(pSet);
		this.display = new GameDisplay();
		this.startTurn();
	}
	
	public void end() {
		this.setStatus(Game.Status.ENDED);
		display.getToolBox().addLogMessage(currPlayer + " has won!", false);
		
		//update statuses
		for(int i : turnOrder)
			players[i].setStatus(Player.Status.LOSER);
		currPlayer.setStatus(Player.Status.WINNER);
		display.refresh();
	}
	
	
	/**
	 * Sets up the players in the game
	 */
	private void definePlayers(Player[] pSet) {
		//prompt for user creation TODO
		for(int i = 0; i < pSet.length; ++i) {
			this.players[i] = pSet[i];
		}

		//Create turn order
		turnOrder = new ArrayList<Integer>();
		for(int i = 0; i < Aggravation.MAX_PLAYERS; ++i)
			if(this.players[i] != null)
				turnOrder.add(i);
		Collections.shuffle(turnOrder);
		
		
		currPlayerIndex = 0;
		
		String str = turnOrder.size()+" Players defined with order ";
		for(int i : turnOrder)
			str+= i+" ";
		Log.v("GAME", str);
	}
	
	
	/**
	 * Updates player information based upon current player + game status
	 */
	public void updatePlayers() {
		//Reset statuses
		for(int i : turnOrder)
			players[i].setStatus(Player.Status.WAITING);
		currPlayer.setStatus(Player.Status.CURRENT_PLAYER);
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
		return players[turnOrder.get(currPlayerIndex)];
	}
	
	public GameDisplay getDisplay() {
		return this.display;
	}
	
	
	/**
	 * Handle the current turn
	 */
	private void startTurn() {
		currPlayer = getCurrentPlayer();
		Log.d("GAME", "New turn started for Player #"
				+ turnOrder.get(currPlayerIndex) + ", \'"
				+ currPlayer+"\'");
		
		marbleMoved = false;
		display.getToolBox().addLogMessage(
				currPlayer
				+" it is your turn to roll!",false);
		display.getToolBox().getRollButton().setEnabled(true);
		display.getToolBox().getRollButton().addActionListener(this);
		updatePlayers();
		
		this.setStatus(Game.Status.WAITING_FOR_ROLL);
		display.refresh();
	}
	
	private void endCurrentTurn() {
		boolean gameOver = true;
		
		for(Space s : display.getBoard().getPlayerHomes(currPlayer)) {
			if(!s.hasMarble())
				gameOver = false;
		}
		
		if(gameOver) {
			this.end();
			this.setStatus(Game.Status.ENDED);
		} else {
			//Switch to next player, or stay the same if 6
			if(roll != 6 || !marbleMoved) {
				++currPlayerIndex;
				if(currPlayerIndex == turnOrder.size())
					currPlayerIndex = 0;
			}
			this.startTurn();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Log.v("GAME-ACTION", e.getActionCommand());
		switch(e.getActionCommand()) {
		case Game.AK_ROLL:
			display.getToolBox().getRollButton().setEnabled(false);
			if(this.currentStatus == Game.Status.WAITING_FOR_ROLL) {
				this.setStatus(Game.Status.PROCESSING);
				roll = this.rand.nextInt(Game.DIE_SIDES)+1;
				display.getToolBox().addLogMessage(currPlayer + " rolls " + roll,false);
				Log.d("GAME", "Player "+currPlayer + " rolled a " + roll);
				
				this.findPossibleDestinations();
				
				String str = "Possible Destinations ("+allPossDst.size()+"): ";
				for(Space key : allPossDst.keySet()) {
					str += key + "[";
					for(Space dst : allPossDst.get(key))
						str += dst + " ";
					str += "] ";
				}
				Log.d("GAME", str);
				
				if(allPossDst.isEmpty()) {
					//player cannot move
					Log.d("GAME", "Player cannot move, skipping");
					display.getToolBox().addLogMessage(currPlayer+" cannot move any marbles!");
					display.refresh();
					
					this.endCurrentTurn();
				} else {
					this.setStatus(Game.Status.WAITING_FOR_MARBLE_SELECTION);
					
					display.getToolBox().addLogMessage(currPlayer + ", Please select one of your marbles to move "+roll);
					display.refresh();
				}
				
				
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
				Log.d(KEY, currPlayer + " selected marble at " + s);
			}
			break;
		case WAITING_FOR_MOVE_CHOICE:
			if(checkMarbleDestination(s)) {
				this.setStatus(Game.Status.PROCESSING);
				Log.d(KEY, currPlayer + " selected open space at " + s);
				this.selectedDestination = s;
				this.makeMove();
			} else if(chooseMarbleSource(s)) {
				Log.d(KEY, currPlayer + " selected marble at " + s);
			} else {
				Log.d("GAME", "Player " + currPlayer+ " chose invalid move for marble at "+selectedSource);
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
		if(s.hasMarble() && s.getMarble().getOwner() == currPlayer) {
			this.setStatus(Game.Status.PROCESSING);
			this.selectedSource = s;
			s.setFocus(true);

			if(allPossDst.get(selectedSource) == null) {
				//player cannot move
				Log.d("GAME", "Player "+currPlayer+" selected invalid marble at " + selectedSource);
				s.setFocus(false);
				selectedSource = null;
				setStatus(Game.Status.WAITING_FOR_MARBLE_SELECTION);
				display.getToolBox().addLogMessage("You cannot move that marble!");
				display.refresh();
				return false;
			} else {
				this.setStatus(Game.Status.WAITING_FOR_MOVE_CHOICE);
				display.getToolBox().addLogMessage(currPlayer+", Please select a space to move the marble " + roll);
				display.refresh();
				return true;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Checks to see if the current player can move the currentSource here.
	 * @return
	 */
	private boolean checkMarbleDestination(Space s) {
		for(Space valid : allPossDst.get(selectedSource))
			if(s == valid)
				return true;
		
		return false;
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
		
		Log.d("GAME-Move", "Marble moved from "+this.selectedSource+" to "+this.selectedDestination);
		this.marbleMoved = true;
		this.endCurrentTurn();
	}
	
	/**
	 * Finds all the possible moves the player could chose
	 */
	private void findPossibleDestinations() {
		allPossDst = new HashMap<Space, ArrayList<Space>>();
		for(Space initial : display.getBoard().getPlayerMarbleSpaces(currPlayer)) {
			ArrayList<Space> possDst = new ArrayList<Space>();
			switch(initial.getType()) {
			case BASE:
				if(roll == 1 || roll == 6) {
					//can exit base
					Space start = display.getBoard().getPlayerStart(currPlayer);
					if(!start.hasMarble() || start.getMarble().getOwner() != currPlayer)
						possDst.add(start);
				}
				break;
			case CENTER:
				if(roll == 1) {
					//can exit center
					Space[] corners = display.getBoard().getCorners();
					for(Space c : corners) {
						if(!c.hasMarble() || c.getMarble().getOwner() != currPlayer)
							possDst.add(c);
					}
				}
				break;
			case LOOP:
			case CORNER:
			case HOME:
				findPaths(initial, possDst, initial, roll);
			}
			if(!possDst.isEmpty())
				allPossDst.put(initial, possDst);
		}
	}
	private void findPaths(Space initial, ArrayList<Space> possDst, Space src, int moves) {
		Boolean goodDst;
		if(moves == 0) {
			possDst.add(src);
		} else {
			Space[] adj = display.getBoard().getNextSpaces(src);
			
			for(Space s : adj) {
				//End path if cannot continue
				Log.e("PATH", ""+src);
				if(!s.hasMarble() || s.getMarble().getOwner() != currPlayer) {
					goodDst = true;
					switch(src.getType()) {
					case LOOP:
						goodDst = !((s.getType() == Space.Type.HOME &&
							((HomeSpace)s).getOwner() != currPlayer) || //cannot enter another player's home
							src == display.getBoard().getPlayerHomeEntrance(currPlayer)); //must go home if at entrance
						break;
					case CENTER:
						goodDst = moves == roll; //can only move once from center
						break;
					case CORNER:
						goodDst = !((s.getType() == Space.Type.CORNER &&
								initial.getType() != Space.Type.CORNER) || //have moved along non-corner
								(src != initial &&
										initial.getType() == Space.Type.CORNER &&
								s.getType() != Space.Type.CORNER));	//only moving among corners
						break;
					default: break;
					}
					
					if(goodDst)
						findPaths(initial, possDst,s, moves - 1);
				}
			}
		}
	}
	
	
}
