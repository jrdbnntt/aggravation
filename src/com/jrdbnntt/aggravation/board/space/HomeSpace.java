package com.jrdbnntt.aggravation.board.space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.jrdbnntt.aggravation.game.Player;

public class HomeSpace extends PlayerSpace {
	
	public HomeSpace(Player p) {
		super(p);
	}
	
	@Override
	public Type getType() {
		return Space.Type.HOME;
	}
	
	@Override
	public void paint(Graphics2D g2d) {
		Color prevColor = g2d.getColor();
		Stroke prevStroke = g2d.getStroke();
		
		//fill it
		g2d.setColor(this.fillColor);
		g2d.fill(this.getShape());
		
		//border it
		g2d.setColor(this.borderColor);
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
