/*
 * Copyright (C) 2013 Eiichiro Uchiumi. All Rights Reserved.
 */
package org.eiichiro.ash;

import java.io.IOException;

import org.eiichiro.reverb.lang.UncheckedException;

import com.google.common.base.Preconditions;

import jline.console.ConsoleReader;

/**
 * {@code Console}
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Console {

	private final ConsoleReader reader;
	
	public Console() {
		try {
			reader = new ConsoleReader();
		} catch (IOException e) {
			throw new UncheckedException(e);
		}
	}
	
	public void print(String string) {
		try {
			reader.print(string);
			reader.flush();
		} catch (IOException e) {
			throw new UncheckedException(e);
		}
	}
	
	public void println(String string) {
		try {
			reader.println(string);
			reader.flush();
		} catch (IOException e) {
			throw new UncheckedException(e);
		}
	}
	
	public void prompt(String prompt) {
		Preconditions.checkArgument(prompt != null && !prompt.isEmpty(), "Parameter 'prompt' must not be [" + prompt + "]");
		reader.setPrompt(prompt);
	}

	public String prompt() {
		return reader.getPrompt();
	}
	
	public String readLine() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new UncheckedException(e);
		}
	}
	
	public ConsoleReader reader() {
		return reader;
	}
	
}
