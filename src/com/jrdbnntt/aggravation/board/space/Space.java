/**
 * A space to go on the board.
 * 
 * Similar to a component, but not exactly the same, since it is painted off another component.
 */
package com.jrdbnntt.aggravation.board.space;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

import com.jrdbnntt.aggravation.board.Marble;

abstract public class Space {
	public static enum Type {
		HOME, BASE, CENTER, LOOP, CORNER, ENTRANCE
	}
	public static enum BorderStatus {
		BOLD, NORMAL, NONE
	}
	
	public static final Stroke
		BORDER_BOLD = new BasicStroke(6),
		BORDER_NORMAL = new BasicStroke(3);
	
	public static final Color 
		BORDER_COLOR_DEFAULT = Color.BLACK,
		FILL_COLOR_DEFAULT = new Color(117,117,117,100);
	
	
	private static double diameter = 50;
	public static Color COLOR = new Color(245, 245, 220);
	
	private Ellipse2D.Double circle = null;
	private Marble marble = null;
	private int id;
	private boolean
		hoverHighight = false,		//for on hover
		possibleHighlight = false;	//for when a possible choice
		
	protected BorderStatus borderStatus = Space.BorderStatus.NORMAL;
	
	public Space(int id) {
		this.id = id;
		this.setLocation(0,0);
	}
	
	public void setLocation(double x, double y) {
		this.circle = new Ellipse2D.Double(
				x-Space.diameter/2,
				y-Space.diameter/2,
				Space.diameter, 
				Space.diameter);
	}
	
	abstract public Type getType();
	abstract public void paint(Graphics2D g2d);
	
	
	//statics
	public static double getDiameter() { return Space.diameter; }
	public static void setDiameter(double d) { Space.diameter = d; }
	
	
	//common access calculations
	public String getLabel() { 
		return "" + this.getType().name().charAt(0) + this.id;
	}
	public void setFocus(boolean b) {
		if(b)
			this.borderStatus = Space.BorderStatus.BOLD;
		else
			this.borderStatus = Space.BorderStatus.NORMAL;
	}
	
	/**
	 * A smaller version of the circle
	 */
	public Ellipse2D.Double getHoverShape() {
		double x, y, d;
		
		if(circle != null) {
			d = Space.diameter / 4;
			x = circle.getCenterX() - d/2;
			y = circle.getCenterY() - d/2;
			
			return new Ellipse2D.Double(x, y, d, d);
		} else {
			return new Ellipse2D.Double();
		}
	}
	
	
	//accessors
	public Ellipse2D.Double getShape() { return this.circle; }	
	public Marble getMarble() { return this.marble; }
	public int getId() { return this.id; }
	public boolean hasHoverHighlight() { return this.hoverHighight; }
	public boolean hasPossibleHighlight() { return this.possibleHighlight; }
	
	
	
	//mutators
	public void setMarble(Marble m) { this.marble = m; }
	public void setHoverHighlight(Boolean b) { this.hoverHighight = b; }
	public void setPossibleHighlight(Boolean b) { this.possibleHighlight = b; }
	
	
	//Marble stuff
	public void clearMarble() {
		this.marble = null;
	}
	public boolean hasMarble() {
		return this.marble != null;
	}
}
