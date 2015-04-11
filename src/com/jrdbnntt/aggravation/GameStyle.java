/**
 * Style configuration for fonts, colors, etc.
 */

package com.jrdbnntt.aggravation;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class GameStyle {
	private final static String
		FONT_FACE = "Helvetica";
	
	public final static Font
		FONT_NORMAL = new Font(FONT_FACE, Font.PLAIN, 14),
		FONT_TITLE = new Font(FONT_FACE, Font.BOLD, 16),
		FONT_SMALL = new Font(FONT_FACE, Font.PLAIN, 10);
	
	public final static Color
		COLOR_BACKGROUND = Color.BLACK,
		COLOR_FONT = Color.WHITE,
		COLOR_BORDER = Color.WHITE,
		COLOR_SPACE_HIGHLIGHT = Color.BLACK,
		COLOR_BOARD_BACKGROUND = new Color(29, 41, 81);
	
	public final static Border
		BORDER_BASIC = BorderFactory.createLineBorder(COLOR_BORDER, 2,true);
	
	public static boolean
		OPTION_VIEW_SPACE_NUMBERS = false,
		OPTION_LOG_VERBOSE = false;
	
	public static final RenderingHints RENDERING_HINTS;
	
	//Complex Static initialization
	static {
		//Rendering hints
		Map<RenderingHints.Key, Object> rhMap;
		rhMap = new HashMap<RenderingHints.Key, Object>();
		rhMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rhMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		rhMap.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		rhMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		rhMap.put(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
		rhMap.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		rhMap.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		rhMap.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		rhMap.put(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
		RENDERING_HINTS = new RenderingHints(rhMap);
	}
}
