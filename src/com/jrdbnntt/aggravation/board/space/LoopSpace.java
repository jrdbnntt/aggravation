package com.jrdbnntt.aggravation.board.space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class LoopSpace extends Space {
	private static final Color 
		BORDER_COLOR = Space.BORDER_COLOR_DEFAULT,
		FILL_COLOR = Space.FILL_COLOR_DEFAULT;
	
	public LoopSpace(int id) {
		super(id);
	}
	
	@Override
	public Type getType() {
		return Space.Type.LOOP;
	}

	@Override
	public void paint(Graphics2D g2d) {
		Color prevColor = g2d.getColor();
		Stroke prevStroke = g2d.getStroke();
		
		//fill it
		g2d.setColor(LoopSpace.FILL_COLOR);
		g2d.fill(this.getShape());
		
		//border it
		g2d.setColor(LoopSpace.BORDER_COLOR);
		switch(this.borderStatus) {
		case BOLD: 
			g2d.setStroke(Space.BORDER_BOLD);
			g2d.draw(this.getShape());
			break;
		case NORMAL: 
			g2d.setStroke(Space.BORDER_NORMAL); 
			g2d.draw(this.getShape());
			break;
		case NONE:
			break;
		}
			
			
		//reset settings
		g2d.setColor(prevColor);
		g2d.setStroke(prevStroke);
	}

}
