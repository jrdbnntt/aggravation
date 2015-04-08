/**
 * A space to go on the board.
 */
package com.jrdbnntt.aggravation.board;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class Space {
	private static double diameter = 50;
	public static Color COLOR = new Color(245, 245, 220);
	
	private Ellipse2D circle = null;
	private Marble marble = null;
	
	public Space(double x, double y, Marble m) {
		this.circle = new Ellipse2D.Double(
				x-Space.diameter/2,
				y-Space.diameter/2,
				Space.diameter, 
				Space.diameter);
		this.marble = m;
	}
	
	public static double getDiameter() {
		return Space.diameter;
	}
	public static void setDiameter(double d) {
		Space.diameter = d;
	}
	
	
	/**
	 * Returns shape geometry for drawing
	 */
	public Ellipse2D getShape() {
		return this.circle;
	}
	
	public Marble getMarble() {
		return this.marble;
	}
	
	public void setMarble(Marble m) {
		this.marble = m;
	}
	public boolean hasMarble() {
		return this.marble != null;
	}
}
