/*
 * Copyright (C) 2013 Eiichiro Uchiumi. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eiichiro.ash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code Line} represents a command line.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Line {

	private final String command;
	
	private Map<String, String> options = new HashMap<>();
	
	private List<String> args = new ArrayList<>();
	
	/**
	 * Constructs a new {@code Line} with the specified command.
	 * 
	 * @param command Command being input.
	 */
	public Line(String command) {
		this.command = command;
	}
	
	/**
	 * Constructs a new {@code Line} with the specified command.
	 * 
	 * @param command Command being input.
	 */
	public Line(String command, Map<String, String> options) {
		this.command = command;
		this.options.putAll(options);
	}
	
	/**
	 * Constructs a new {@code Line} with the specified command.
	 * 
	 * @param command Command being input.
	 */
	public Line(String command, List<String> args) {
		this.command = command;
		this.args.addAll(args);
	}
	
	/**
	 * Constructs a new {@code Line} with the specified command.
	 * 
	 * @param command Command being input.
	 */
	public Line(String command, Map<String, String> options, List<String> args) {
		this.command = command;
		this.options.putAll(options);
		this.args.addAll(args);
	}
	
	/**
	 * Returns the command being input.
	 * 
	 * @return The command being input.
	 */
	public String command() {
		return command;
	}
	
	public Map<String, String> options() {
		return options;
	}
	
	public List<String> args() {
		return args;
	}
	
}
