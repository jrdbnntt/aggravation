/**
 * App-wide utility functions
 */
package com.jrdbnntt.aggravation;

import java.awt.Color;

public class Util {
	public static class ColorManager {
		public static Color darken(Color c) {
			int div = 2;
			return new Color(
					c.getRed()/div,
					c.getGreen()/div,
					c.getBlue()/div,
					c.getAlpha()
					);
		}
		public static Color lighten(Color c) {
			int div = 2;
			return new Color(
					c.getRed() + (255 - c.getRed())/div,
					c.getGreen() + (255 - c.getGreen())/div,
					c.getBlue() + (255 - c.getBlue())/div,
					c.getAlpha()
					);
		}
		public static String getHexString(Color c) {
			return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
		}
	}
	
	public static class Log {
		public static void v(String key, String message) {
			if(GameStyle.OPTION_LOG_VERBOSE)
				System.out.printf("[BACKGROUND][%-15s] %s\n", key, message);
		}
		public static void d(String key, String message) {
			System.out.printf("[%-15s] %s\n", key, message);
		}
		public static void e(String key, String message) {
			System.err.printf("[%-15s] %s\n", key, message);
		}
	}
}
