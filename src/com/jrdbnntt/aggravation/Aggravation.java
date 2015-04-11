/**
 * Main class to manage the entire game.
 */

package com.jrdbnntt.aggravation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;

import com.jrdbnntt.aggravation.Util.Log;
import com.jrdbnntt.aggravation.game.Game;
import com.jrdbnntt.aggravation.game.GameDisplay;

@SuppressWarnings("serial")
public class Aggravation extends JFrame implements ActionListener {
	//Game constants
	public static final int MAX_PLAYERS = 6;		//playerNum = index CW from N
	public static final int MARBLES_PER_PLAYER = 4;
		
	private static final String GAME_TITLE = "Aggravation - by Jared Bennett";
	private static final Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	
	//size constraints
	public static final Dimension
	MINIMUM_SIZE = GameDisplay.MINIMUM_SIZE,
	PREFERRED_SIZE = GameDisplay.PREFERRED_SIZE;
	
	//Menu bar action commands
	public static final String
		AC_GAME_START = "AC_GAME_START",
		AC_GAME_LOAD = "AC_GAME_LOAD",
		AC_GAME_SAVE = "AC_GAME_SAVE",
		AC_GAME_SAVE_AS = "AC_GAME_SAVE_AS",
		AC_GAME_TITLE = "AC_GAME_TITLE",
		AC_GAME_EXIT = "AC_GAME_EXIT",
		AC_VIEW_RULES = "AC_VIEW_RULES";
	
	private JPanel content;		//container for everything
	private JMenuBar menuBar;
	
//	private Board board = new Board();
//	private ToolBox toolBox = new ToolBox();
	
	public static void main(String[] args){
		Aggravation game;
		
		//Use System Theme if possible (looks nice)
		try {
			UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// ignore
		} catch (ClassNotFoundException e) {
			// ignore
		} catch (InstantiationException e) {
			// ignore
		} catch (IllegalAccessException e) {
			// ignore
		}
		
		
		game = new Aggravation(GAME_TITLE); 
		game.setSize(Aggravation.PREFERRED_SIZE);
		game.setMinimumSize(Aggravation.MINIMUM_SIZE);
		game.setPreferredSize(PREFERRED_SIZE);
		game.getContentPane().setBackground(GameStyle.COLOR_BACKGROUND);
		
		//display in center
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		game.setLocation(dim.width/2-game.getSize().width/2, dim.height/2-game.getSize().height/2);
		
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		game.setVisible(true);
		
		Log.d("APP", "START");		
	}
	
	public Aggravation(String title) {
		super(title);
		
		this.buildMenuBar();
		this.content = new JPanel(new BorderLayout());
		this.content.setBorder(Aggravation.padding);
		this.content.setBackground(GameStyle.COLOR_BACKGROUND);
		this.add(content);
	}
	
	
	/**
	 * Sets up the menu bar
	 */
	private void buildMenuBar() {
		JMenu menu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		
		//make bar
		this.menuBar = new JMenuBar();
		
		//make Game menu
		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);
		menu.getAccessibleContext().setAccessibleDescription("Game management");
		this.menuBar.add(menu);
		
		//add Game options
		menuItem = new JMenuItem("Start New");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.getAccessibleContext().setAccessibleDescription("Start a new game");
		menuItem.setActionCommand(Aggravation.AC_GAME_START);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Load game...");
		menuItem.setMnemonic(KeyEvent.VK_L);
		menuItem.getAccessibleContext().setAccessibleDescription("Load game from file");
		menuItem.setActionCommand(Aggravation.AC_GAME_LOAD);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Save");
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.getAccessibleContext().setAccessibleDescription("Save game");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(Aggravation.AC_GAME_SAVE);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Save as...");
		menuItem.setMnemonic(KeyEvent.VK_A);
		menuItem.getAccessibleContext().setAccessibleDescription("Save game to new file");
		menuItem.setActionCommand(Aggravation.AC_GAME_SAVE_AS);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menu.addSeparator();
		menuItem = new JMenuItem("Return to Title");
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.getAccessibleContext().setAccessibleDescription("Quit game to title screen");
		menuItem.setActionCommand(Aggravation.AC_GAME_TITLE);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Exit");
		menuItem.setMnemonic(KeyEvent.VK_X);
		menuItem.getAccessibleContext().setAccessibleDescription("Exit program");
		menuItem.setActionCommand(Aggravation.AC_GAME_EXIT);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		
		//make View menu
		menu = new JMenu("View");
		menu.setMnemonic(KeyEvent.VK_V);
		menu.getAccessibleContext().setAccessibleDescription("Display management");
		this.menuBar.add(menu);
		
		//add View options
		menuItem = new JMenuItem("Rules...");
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.getAccessibleContext().setAccessibleDescription("Display game rules in a new window");
		menuItem.setActionCommand(Aggravation.AC_VIEW_RULES);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		cbMenuItem = new JCheckBoxMenuItem("Space Numbers");
		cbMenuItem.setMnemonic(KeyEvent.VK_N);
		cbMenuItem.getAccessibleContext().setAccessibleDescription("Display space numbers next to spaces");
		cbMenuItem.setSelected(false);
		cbMenuItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					Log.d("ITEM_EVENT","View->Space Numbers = OFF");
					GameStyle.OPTION_VIEW_SPACE_NUMBERS = false;
					repaint();
				} else {
					Log.d("ITEM_EVENT","View->Space Numbers = ON");
					GameStyle.OPTION_VIEW_SPACE_NUMBERS = true;
					repaint();
				}
			}
		});
		menu.add(cbMenuItem);
		
		
		
		this.setJMenuBar(this.menuBar);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Log.d("ACTION_EVENT", e.getActionCommand());
		
		switch(e.getActionCommand()) {
		case Aggravation.AC_GAME_EXIT:
			this.setVisible(false);
			this.dispose();
			break;
		case Aggravation.AC_GAME_LOAD:	//TODO
			
			break;
		case Aggravation.AC_GAME_SAVE:	//TODO
			
			break;
		case Aggravation.AC_GAME_SAVE_AS:	//TODO
			
			break;
		case Aggravation.AC_GAME_START:
			Game.getCurrentInstance().start();
			this.content.removeAll();
			this.content.add(Game.getCurrentInstance().getDisplay(), BorderLayout.CENTER);
			this.pack();
			Game.getCurrentInstance().getDisplay().refresh();
			break;
		case Aggravation.AC_GAME_TITLE:	//TODO
			
			break;
		case Aggravation.AC_VIEW_RULES:	//TODO
			
			break;
		}
	}
}