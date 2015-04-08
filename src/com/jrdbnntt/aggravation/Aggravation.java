/**
 * Main class to manage the entire game.
 */

package com.jrdbnntt.aggravation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jrdbnntt.aggravation.board.Board;
import com.jrdbnntt.aggravation.toolbox.ToolBox;

@SuppressWarnings("serial")
public class Aggravation extends JFrame implements ComponentListener {
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
	
	private JPanel cont;
	
	private Board board = new Board();
	private ToolBox toolBox = new ToolBox();
	
	public static void main(String[] args){
		Aggravation game = new Aggravation(GAME_TITLE);
		
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
	
}