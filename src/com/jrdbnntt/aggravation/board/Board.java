/**
 * The Game board, containing all game elements.
 */
package com.jrdbnntt.aggravation.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import com.jrdbnntt.aggravation.Aggravation;
import com.jrdbnntt.aggravation.GameStyle;
import com.jrdbnntt.aggravation.Util.Log;
import com.jrdbnntt.aggravation.board.space.BaseSpace;
import com.jrdbnntt.aggravation.board.space.CenterSpace;
import com.jrdbnntt.aggravation.board.space.CornerSpace;
import com.jrdbnntt.aggravation.board.space.HomeSpace;
import com.jrdbnntt.aggravation.board.space.LoopSpace;
import com.jrdbnntt.aggravation.board.space.Space;
import com.jrdbnntt.aggravation.game.Game;
import com.jrdbnntt.aggravation.game.Player;


@SuppressWarnings("serial")
public class Board extends JPanel implements ComponentListener, MouseMotionListener, MouseInputListener {
	//board config
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
	
	//Mouse space pointers
	Space foundSpace, hoverSpace;
	
	//board components
	Rectangle2D bg;
	double size;
	
	public Board() {
		super();
		this.setMinimumSize(new Dimension(Board.MIN_SIZE, Board.MIN_SIZE));
		
		this.setBackground(GameStyle.COLOR_BACKGROUND);
		this.addComponentListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
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
	public void updateGeometry() {
		Log.d("BOARD", "Updating geometry...");
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
		center.setLocation(origin.getX(),origin.getY());
		for(int zone = 0, pos, curr; zone < Aggravation.MAX_PLAYERS; ++zone) {
			pos = zone*ZONE_OFFSET;
			rad = Math.PI/2 - zone*ZONE_RAD_OFFSET;
			
			//corner
			r = CENTER_OFFSET_UNITS*u;
			x = r*Math.cos(rad);
			y = r*Math.sin(rad);
			loop[pos].setLocation(origin.getX()+x,origin.getY()-y);
			
			//base
			for(int i = 0; i < Aggravation.MARBLES_PER_PLAYER; ++i) {
				r = CENTER_OFFSET_UNITS*u + (BASE_OFFSET_UNITS+SPACE_OFFSET_UNITS*i)*u;
				x = r*Math.cos(rad);
				y = r*Math.sin(rad);
				base[zone][i].setLocation(origin.getX()+x,origin.getY()-y);
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
						loop[curr].setLocation(zGrid[i][j].getX(),zGrid[i][j].getY());
						break;
					case 1:
						if(i == zGrid.length-1) {
							curr = pos+1 + zGrid.length + j - 1;
							loop[curr].setLocation(zGrid[i][j].getX(),zGrid[i][j].getY());
						}
							
						break;
					case 2:
						if(i == zGrid.length-1) {
							curr = pos+1 + zGrid.length + j - 1;
							loop[curr].setLocation(zGrid[i][j].getX(),zGrid[i][j].getY());
						} else {
							curr = home[zone].length - i - 1;
							home[zone][curr].setLocation(zGrid[i][j].getX(),zGrid[i][j].getY());
						}
						break;
					case 3:
						if(i == zGrid.length-1) {
							curr = pos+1 + zGrid.length + j - 1;
							loop[curr].setLocation(zGrid[i][j].getX(),zGrid[i][j].getY());
						}
						break;
					case 4:
						curr = pos+1 + zGrid.length*2 + zGrid[i].length-3 - i;
						loop[curr].setLocation(zGrid[i][j].getX(),zGrid[i][j].getY());
						break;
					}
				}
			}
			
		}
		Log.d("BOARD", "Geometry update complete.");
		repaint();
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		int pos = 0;
		Color c;
		
		g2d.setRenderingHints(GameStyle.RENDERING_HINTS);
		g2d.setFont(GameStyle.FONT_SMALL);
		
		//Draw Background
		g2d.setColor(GameStyle.COLOR_BOARD_BACKGROUND);
		g2d.fill(bg);
		
		//Draw Spaces + marbles
		g2d.setColor(Space.COLOR);
		
		paintSpace(g2d,center);
		for(Space s : loop)
			paintSpace(g2d,s);
		for(Space[] b : base)
			for(Space s : b)
				if(s != null)
					paintSpace(g2d,s);
		for(Space[] h : home)
			for(Space s : h)
				paintSpace(g2d,s);
		
		
		
	}
	
	private void paintSpace(Graphics2D g2d, Space s) {
		Color c = g2d.getColor();
		Ellipse2D circle;
		
		if(s != null) {
			circle = s.getShape();
			s.paint(g2d);
			if(GameStyle.OPTION_VIEW_SPACE_NUMBERS)
				g2d.drawString(s.getLabel(), (float)circle.getX(), (float)circle.getY());
			if(s.hasMarble()) {
				//draw the marble
				c = g2d.getColor();
				g2d.setColor(s.getMarble().getColor());
				g2d.fill(circle);
				g2d.setColor(c);
				
				if((Game.getCurrentInstance().getStatus() == Game.Status.WAITING_FOR_MARBLE_SELECTION ||
						Game.getCurrentInstance().getStatus() == Game.Status.WAITING_FOR_MOVE_CHOICE) &&
						this.hoverSpace == s && 
						s.hasHoverHighlight() &&
						s.getMarble().getOwner() == Game.getCurrentInstance().getCurrentPlayer()) {
					//draw highlight
					g2d.setColor(GameStyle.COLOR_SPACE_HIGHLIGHT);
					g2d.fill(s.getHoverShape());
				}
			} else if(Game.getCurrentInstance().getStatus() == Game.Status.WAITING_FOR_MOVE_CHOICE &&
					this.hoverSpace == s && 
					s.hasHoverHighlight()) {
				//draw highlight
				g2d.setColor(GameStyle.COLOR_SPACE_HIGHLIGHT);
				g2d.fill(s.getHoverShape());
			}
			
		}
		
		g2d.setColor(c);
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentShown(ComponentEvent e) {}
	@Override
	public void componentResized(ComponentEvent e) {
		Log.d("BOARD", "Resized to "+ this.getWidth() + " x " + this.getHeight());
		updateGeometry();
	}
	
	
	/**
	 * Sets up player marbles in base for all in current game
	 */
	public void init() {
		Player p;
		
		//create all spaces
		center = new CenterSpace();
		for(int i = 0; i < loop.length; ++i)
			if(i % ZONE_OFFSET == 0)
				loop[i] = new CornerSpace(i % ZONE_OFFSET);
			else 
				loop[i] = new LoopSpace(i);
		for(int zone = 0; zone < base.length; ++zone)
			for(int i = 0; i < base[zone].length; ++i) {
				p = Game.getCurrentInstance().getPlayer(zone);
				if(p == null)
					p = Player.NONE;
				base[zone][i] = new BaseSpace(zone, i, p);
			}
				
		for(int zone = 0; zone < home.length; ++zone)
			for(int i = 0; i < home[zone].length; ++i) {
				p = Game.getCurrentInstance().getPlayer(zone);
				if(p == null)
					p = Player.NONE;
				home[zone][i] = new HomeSpace(zone, i, p);
			}
		
		//create space geometry
		this.updateGeometry(); 
		
		//fill spaces with marbles
		for(int zone = 0; zone < base.length; ++zone) {
			try {
				p = Game.getCurrentInstance().getPlayer(zone);
				Log.d("BOARD","Player \'"+p.getName()+"\' set for zone "+zone);
				for(Space s : base[zone])
					s.setMarble(new Marble(p, p.getColor()));
			} catch(NullPointerException e) {
				//player not set
				Log.d("BOARD","Player not set for zone "+zone);
			}
		}
	}
	
	private boolean boardContains(Point p) {
		if(bg != null) {
			return bg.contains(p);
		} else {
			return false;
		}
	}
	
	/**
	 * Finds a space and returns t/f. Space found gets stored,
	 * and must be retrieved in a later function
	 * @param p point to check
	 * @return found : not found
	 */
	private boolean findSpace(Point p) {
		//Check all space geometry
		if(center.getShape().contains(p)) {
			this.foundSpace = center;
			return true;
		}
		
		for(Space[] zone : base)
			for(Space s : zone)
				if(s.getShape().contains(p)) {
					this.foundSpace = s;
					return true;
				}
		for(Space[] zone : home)
			for(Space s : zone)
				if(s.getShape().contains(p)) {
					this.foundSpace = s;
					return true;
				}
		for(Space s : loop)
			if(s.getShape().contains(p)) {
				this.foundSpace = s;
				return true;
			}
		return false;
	}
	
	private Space getFoundSpace() { return this.foundSpace; }
	
	@Override
	public void mouseClicked(MouseEvent e) {
		final String KEY = "BOARD-Mouse-Click";
		Point p = e.getPoint();
		if(this.boardContains(p)) {
			Log.d(KEY, "Clicked on board");
			if(this.findSpace(p)) {
				Log.d(KEY, "Clicked on a space " + this.getFoundSpace().getLabel());
				Game.getCurrentInstance().onSpaceClicked(this.foundSpace);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		final String KEY = "BOARD-Mouse-Move";
		Point p = e.getPoint();
		
		if(this.boardContains(p) && this.findSpace(p)) {
			if(this.foundSpace != this.hoverSpace) {
				Log.d(KEY, "Entered space " + this.foundSpace.getLabel());
				this.hoverSpace = this.foundSpace;
				this.hoverSpace.setHoverHighlight(true);
				repaint();
			} 
		} else if(this.hoverSpace != null) {
			Log.d(KEY, "Exited space " + this.hoverSpace.getLabel());
			this.hoverSpace.setHoverHighlight(false);
			this.hoverSpace = null;
			repaint();
		}
		
	}
	
	//Space finders
	public Space getCenter() { return center; }
	public Space[] getCorners() {
		Space corners[] = new Space[Aggravation.MAX_PLAYERS];
		
		for(int i = 0; i < Aggravation.MAX_PLAYERS; ++i)
			corners[i] = loop[ZONE_OFFSET*i];
		
		return corners;
	}
	public Space[] getPlayerHomes(Player p) {
		Space[] homes = new Space[Aggravation.MARBLES_PER_PLAYER];
		
		for(int i = 0; i < Aggravation.MARBLES_PER_PLAYER; ++i)
			homes[i] = home[p.getZone()][i];
		
		return homes;
	}
	public Space[] getPlayerBases(Player p) {
		Space[] bases = new Space[Aggravation.MARBLES_PER_PLAYER];
		
		for(int i = 0; i < Aggravation.MARBLES_PER_PLAYER; ++i)
			bases[i] = base[p.getZone()][i];
		
		return bases;
	}
	public Space getPlayerStart(Player p) {
		return loop[p.getZone()*ZONE_OFFSET + START_OFFSET];
	}
	public Space getPlayerHomeEntrance(Player p) {
		return loop[p.getZone()*ZONE_OFFSET + HOME_OFFSET];
	}
	
	/**
	 * Finds which spaces src is connected to.
	 * @return The result spaces, with variable length
	 */
	public Space[] getNextSpaces(Space src) {
		Space[] adj = new Space[0];
		
		switch(src.getType()) {
		case BASE:
			adj = new Space[1]; //playerstart
			adj[0] = loop[((BaseSpace) src).getZone() % ZONE_OFFSET + START_OFFSET];
		case HOME:
			if(src.getId()+1 < Aggravation.MARBLES_PER_PLAYER) {
				adj = new Space[1];
				adj[0] = home[((HomeSpace) src).getZone()][src.getId()+1];
			}
			break;
		case CENTER:
			adj = getCorners();
			break;
		case LOOP:
			adj = new Space[1];
			break;
		case CORNER:
			adj = new Space[3];
			adj[0] = center;										//center
			adj[1] = loop[src.getId()+1];							//down row
			adj[2] = loop[(src.getId()+ZONE_OFFSET) % loop.length]; //next corner
			break;
		default: break;
		}
		
		return adj;
	}
	
	
}
