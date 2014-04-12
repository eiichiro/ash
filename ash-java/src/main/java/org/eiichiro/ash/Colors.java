/*
 * Copyright (C) 2013 Eiichiro Uchiumi. All Rights Reserved.
 */
package org.eiichiro.ash;

import jline.TerminalFactory;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

/**
 * {@code Colors}
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Colors {

	private Colors() {}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String black(String string) {
		return color(Color.BLACK, string);
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String blue(String string) {
		return color(Color.BLUE, string);
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String cyan(String string) {
		return color(Color.CYAN, string);
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String green(String string) {
		return color(Color.GREEN, string);
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String magenta(String string) {
		return color(Color.MAGENTA, string);
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String red(String string) {
		return color(Color.RED, string);
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String white(String string) {
		return color(Color.WHITE, string);
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String yellow(String string) {
		return color(Color.YELLOW, string);
	}
	
	/**
	 * 
	 * @param color
	 * @param string
	 * @return
	 */
	public static String color(Color color, String string) {
		if (TerminalFactory.get().isAnsiSupported()) {
			return Ansi.ansi().fg(color).toString() + string + Ansi.ansi().fg(Color.DEFAULT);
		} else {
			return string;
		}
	}
	
}
