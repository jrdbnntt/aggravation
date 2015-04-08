/**
 * The Game board, containing all game elements.
 */
package com.jrdbnntt.aggravation.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.jrdbnntt.aggravation.Aggravation;
import com.jrdbnntt.aggravation.game.Game;
import com.jrdbnntt.aggravation.game.Player;
import com.sun.javafx.geom.Shape;


@SuppressWarnings("serial")
public class Board extends JPanel implements ComponentListener {
	//board config
	private static final Color backgroundColor = Color.BLACK;
	private static final int MIN_SIZE = 500;	//min size of board square
	
	
	
	
	//space offsets
	private static final int ZONE_OFFSET = 14;	//spaces from corner to corner
	private static final int START_OFFSET = 9;	//spaces from corner to start
	private static final int HOME_OFFSET = 7;	//spaces from corner to home entrance
	
	//Board spaces
	Space[] loop = new Space[84];	//contains all zones' connected spaces
	Space center;					//special center space
	Space[][] home = new Space[Aggravation.MAX_PLAYERS][Aggravation.MARBLES_PER_PLAYER];	//player homes
	Space[][] base = new Space[Aggravation.MAX_PLAYERS][Aggravation.MARBLES_PER_PLAYER];	//player bases
	
	//board components
	Rectangle2D bg;
	double size;
	
	public Board() {
		super();
		this.setMinimumSize(new Dimension(Board.MIN_SIZE, Board.MIN_SIZE));
		
		this.setBackground(backgroundColor);
		this.addComponentListener(this);
		updateBoard();
		initializeBoard();
		
	}
	
	/**
	 * Calculates player home entrance position in the loop
	 * @param playerNum player to check if it is home (CW index)
	 * @return position of player's home entrance in loop
	 */
	public int getPlayerHomeEntrance(int playerNum) {
		return playerNum * ZONE_OFFSET + HOME_OFFSET;
	}
	
	/**
	 * Calculates player start position in the loop
	 * @param playerNum player to check if it is home (CW index)
	 * @return position of player's start in loop
	 */
	public int getPlayerStart(int playerNum) {
		return playerNum * ZONE_OFFSET + START_OFFSET;
	}
	
	/**
	 * Checks a loop position to see if it is a corner
	 * @param pos
	 * @return true/false
	 */
	public boolean isCorner(int pos) {
		return (pos % ZONE_OFFSET) == 0;
	}
	
	/**
	 * Calculates geometry of objects on board relative to size
	 */
	private void updateBoard() {
		final double ZONE_RAD_OFFSET = 2*Math.PI / Aggravation.MAX_PLAYERS;
		final int SPACE_OFFSET_UNITS = 2;
		final int CENTER_OFFSET_UNITS = SPACE_OFFSET_UNITS*4;
		final int BASE_OFFSET_UNITS = SPACE_OFFSET_UNITS*3;
		
		Rectangle2D b = (Rectangle2D) this.getBounds();
		Point2D origin = new Point2D.Double(b.getWidth()/2, b.getHeight()/2);
		
		Point2D[][] zGrid = new Point2D[Aggravation.MARBLES_PER_PLAYER+1][5];	//grid of points to make up zone
		
		double u; //unit base (space size)
		double rad; 		//current rotation
		double x, y, r, d;		//origin offsets
		
		//Calculate background
		if(b.getWidth() < b.getHeight())
			size = b.getWidth();
		else
			size = b.getHeight();
		if(size < Board.MIN_SIZE)
			size = Board.MIN_SIZE;
		bg = new Rectangle2D.Double(origin.getX()-size/2, origin.getY()-size/2, size, size);
		
		
		//Calculate spaces, keeping any marbles if they exist
		u = size/45;
		
		
		/* Get the base zone grid points
		 * 
		 * KEY
		 *    01234
		 *  4 LLLLS
		 *  3 L-H-L
		 *  2 L-H-L
		 *  1 L-H-L
		 *  0 L-H-L
		 *    C
		 *  	^
		 *      |
		 *      O
		 */
		for(int i = 0; i < zGrid.length; ++i) {
			r = (CENTER_OFFSET_UNITS + i*SPACE_OFFSET_UNITS+1)*u;
			for(int j = 0; j < zGrid[i].length; ++j) {
				d = SPACE_OFFSET_UNITS*(j-2)*u; 
				zGrid[i][j] = new Point2D.Double(origin.getX()+d, origin.getY()-r);
				zGrid[i][j] = AffineTransform.getRotateInstance(-ZONE_RAD_OFFSET/2, origin.getX(), origin.getY()).transform(zGrid[i][j], null);
			}
		}
		
		//make the actual spaces
		Space.setDiameter(u*1.3);
		center = new Space(origin.getX(),origin.getY(), (center != null)? center.getMarble() : null);
		for(int zone = 0, pos, curr; zone < Aggravation.MAX_PLAYERS; ++zone) {
			pos = zone*ZONE_OFFSET;
			rad = Math.PI/2 - zone*ZONE_RAD_OFFSET;
			
			//corner
			r = CENTER_OFFSET_UNITS*u;
			x = r*Math.cos(rad);
			y = r*Math.sin(rad);
			loop[pos] = new Space(origin.getX()+x,origin.getY()-y, (loop[pos] != null)? loop[pos].getMarble() : null);
			
			
			//base
			for(int i = 0; i < Aggravation.MARBLES_PER_PLAYER; ++i) {
				r = CENTER_OFFSET_UNITS*u + (BASE_OFFSET_UNITS+SPACE_OFFSET_UNITS*i)*u;
				x = r*Math.cos(rad);
				y = r*Math.sin(rad);
				base[zone][i] = new Space(origin.getX()+x,origin.getY()-y, (base[zone][i] != null)? base[zone][i].getMarble() : null);
			}
			
			//home + loop
			rad -= ZONE_RAD_OFFSET/2;
			for(int i = 0; i < zGrid.length; ++i) {
				for(int j = 0; j < zGrid[i].length; ++j) {
					//transform point
					zGrid[i][j] = AffineTransform.getRotateInstance(ZONE_RAD_OFFSET, origin.getX(), origin.getY()).transform(zGrid[i][j], null);
					
					//create appropriate spaces
					switch(j) {
					case 0:
						curr = pos+1+i;
						loop[curr] = new Space(zGrid[i][j].getX(),zGrid[i][j].getY(), (loop[curr] != null)? loop[curr].getMarble() : null);
						break;
					case 1:
						if(i == zGrid.length-1) {
							curr = pos+1 + zGrid.length + j - 1;
							loop[curr] = new Space(zGrid[i][j].getX(),zGrid[i][j].getY(), (loop[curr] != null)? loop[curr].getMarble() : null);
						}
							
						break;
					case 2:
						if(i == zGrid.length-1) {
							curr = pos+1 + zGrid.length + j - 1;
							loop[curr] = new Space(zGrid[i][j].getX(),zGrid[i][j].getY(), (loop[curr] != null)? loop[curr].getMarble() : null);
						} else {
							curr = home[zone].length - i - 1;
							home[zone][curr] = new Space(zGrid[i][j].getX(),zGrid[i][j].getY(), (home[zone][curr] != null)? home[zone][curr].getMarble() : null);
						}
						break;
					case 3:
						if(i == zGrid.length-1) {
							curr = pos+1 + zGrid.length + j - 1;
							loop[curr] = new Space(zGrid[i][j].getX(),zGrid[i][j].getY(), (loop[curr] != null)? loop[curr].getMarble() : null);
						}
						break;
					case 4:
						curr = pos+1 + zGrid.length*2 + zGrid[i].length-3 - i;
						loop[curr] = new Space(zGrid[i][j].getX(),zGrid[i][j].getY(), (loop[curr] != null)? loop[curr].getMarble() : null);
						break;
					}
				}
			}
			
			
		}
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int pos = 0;
		Color c;
		
		g2d.setRenderingHints(Aggravation.RENDERING_HINTS);
		
		//Draw Background
		g2d.setColor(Color.GRAY);
		g2d.fill(bg);
		
		//Draw Spaces
		g2d.setColor(Space.COLOR);
		g2d.fill(center.getShape());
		for(Space s : loop) {
			if(s != null) {
				Ellipse2D circle = s.getShape();
				g2d.draw(circle);
				g2d.drawString(""+(pos++), (float)circle.getX(), (float)circle.getY());
									
				if(s.hasMarble()) {
					//draw the marble
					c = g2d.getColor();
					g2d.setColor(s.getMarble().getColor());
					g2d.fill(circle);
					g2d.setColor(c);
				}
				
			}
		}
		for(Space[] b : base) {
			pos = 0;
			for(Space s : b) {
				if(s != null) {
					Ellipse2D circle = s.getShape();
					g2d.draw(circle);
					g2d.drawString(""+(pos++), (float)circle.getX(), (float)circle.getY());
										
					if(s.hasMarble()) {
						//draw the marble
						c = g2d.getColor();
						g2d.setColor(s.getMarble().getColor());
						g2d.fill(circle);
						g2d.setColor(c);
					}
				
				}
			}
		}
		for(Space[] h : home) {
			pos = 0;
			for(Space s : h) {
				if(s != null) {
					Ellipse2D circle = s.getShape();
					g2d.draw(circle);
					g2d.drawString(""+(pos++), (float)circle.getX(), (float)circle.getY());
										
					if(s.hasMarble()) {
						//draw the marble
						c = g2d.getColor();
						g2d.setColor(s.getMarble().getColor());
						g2d.fill(circle);
						g2d.setColor(c);
					}
				}
			}
		}
		
		
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentShown(ComponentEvent e) {}
	@Override
	public void componentResized(ComponentEvent e) {
		updateBoard();
	}
	
	
	/**
	 * Sets up player marbles in base for all in current game
	 */
	private void initializeBoard() {
		Player p;
		
		for(int zone = 0; zone < base.length; ++zone) {
			for(Space s : base[zone]) {
				p = Game.getCurrentInstance().getPlayer(zone);
				if(p != null) {
					s.setMarble(new Marble(zone, p.getColor()));
				}
			}
		}
	}
}
