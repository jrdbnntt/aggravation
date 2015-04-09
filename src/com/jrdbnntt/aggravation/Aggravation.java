/**
 * Main class to manage the entire game.
 */

package com.jrdbnntt.aggravation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ItemSelectable;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jrdbnntt.aggravation.board.Board;
import com.jrdbnntt.aggravation.toolbox.ToolBox;
import com.sun.glass.events.KeyEvent;

@SuppressWarnings("serial")
public class Aggravation extends JFrame implements ComponentListener, ActionListener {
	//Game constants
	public static final int MAX_PLAYERS = 6;		//playerNum = index CW from N
	public static final int MARBLES_PER_PLAYER = 4;
	
	//Rendering hints
	private static final Map<RenderingHints.Key, Object> RENDERING_HINTS_MAP;
	static {
		RENDERING_HINTS_MAP = new HashMap<RenderingHints.Key, Object>();
		RENDERING_HINTS_MAP.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		RENDERING_HINTS_MAP.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		RENDERING_HINTS_MAP.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		RENDERING_HINTS_MAP.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		RENDERING_HINTS_MAP.put(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
		RENDERING_HINTS_MAP.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		RENDERING_HINTS_MAP.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		RENDERING_HINTS_MAP.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		RENDERING_HINTS_MAP.put(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
	}
	public static final RenderingHints RENDERING_HINTS = new RenderingHints(RENDERING_HINTS_MAP);
	
	private static final String GAME_TITLE = "Aggravation - by Jared Bennett";
	
	private static final double BOARD_WEIGHT_Y = .9;
	private static final int START_WIDTH = 800;
	private static final int START_HEIGHT = (int)(Aggravation.START_WIDTH/Aggravation.BOARD_WEIGHT_Y);
	
	public static final int MIN_WIDTH = 500;
	public static final int MIN_HEIGHT = (int)(Aggravation.MIN_WIDTH/Aggravation.BOARD_WEIGHT_Y);
	
	
	//Menu bar action commands
	public static final String
		AC_GAME_START = "AC_GAME_START",
		AC_GAME_LOAD = "AC_GAME_LOAD",
		AC_GAME_SAVE = "AC_GAME_SAVE",
		AC_GAME_SAVE_AS = "AC_GAME_SAVE_AS",
		AC_GAME_TITLE = "AC_GAME_TITLE",
		AC_GAME_EXIT = "AC_GAME_EXIT",
		
		AC_VIEW_RULES = "AC_VIEW_RULES"
		;
	
	
	
	private JPanel cont;
	private JMenuBar menuBar;
	
	private Board board = new Board();
	private ToolBox toolBox = new ToolBox();
	
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
		game.setSize(START_WIDTH,START_HEIGHT);
		game.setMinimumSize(new Dimension(
			MIN_WIDTH, MIN_HEIGHT
			));
		
		//display in center
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		game.setLocation(dim.width/2-game.getSize().width/2, dim.height/2-game.getSize().height/2);
		
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		game.setVisible(true);
		
		System.out.println("APPLICATION START");
		
	}
	
	public Aggravation(String title) {
		super(title);
		
		this.buildMenuBar();
		
		cont = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = Aggravation.BOARD_WEIGHT_Y;
		cont.add(board, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1 - Aggravation.BOARD_WEIGHT_Y;
		cont.add(toolBox, gbc);
//		
//		board.setAlignmentX(Component.CENTER_ALIGNMENT);
//		board.setMinimumSize(new Dimension(MIN_WIDTH, MIN_WIDTH));
//		toolBox.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT-MIN_WIDTH));
//		
//		cont.add(board);
//		cont.add(Box.createVerticalGlue());
//		cont.add(toolBox);
//		
		this.add(cont);
		
//		resizeBoard();
		addComponentListener(this);
	}
	
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Turn on global graphics options
		g2d.setRenderingHints(Aggravation.RENDERING_HINTS);
		
		super.paint(g2d);
	}
	
	
	@Override
	public void componentHidden(ComponentEvent e) {}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentShown(ComponentEvent e) {}
	@Override
	public void componentResized(ComponentEvent e) {
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
					System.out.println("ITEM_EVENT: View->Space Numbers = OFF");
				} else {
					System.out.println("ITEM_EVENT: View->Space Numbers = ON");
				}
			}
		});
		menu.add(cbMenuItem);
		
		
		
		this.setJMenuBar(this.menuBar);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("ACTION_EVENT: " + e.getActionCommand());
		
	}
}