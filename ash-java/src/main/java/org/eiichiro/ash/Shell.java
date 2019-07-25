/*
 * Copyright (C) 2013 Ash project. All Rights Reserved.
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

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.base.Preconditions;

/**
 * {@code Shell} is the shell object provided by Ash.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Shell {

	private Console console = new Console();
	
	private Map<String, Command> commands = new ConcurrentHashMap<>();
	
	private Map<String, Options> options = new ConcurrentHashMap<>();
	
	public Shell() {
		console.reader().addCompleter(new CommandCompleter(this));
	}
	
	/**
	 * Registers the specified command instance into the shell.
	 * 
	 * @param command Command to be registered.
	 */
	public void register(Command command) {
		Preconditions.checkArgument(command != null, "Parameter 'command' must not be [" + command + "]");
		register(command.name(), command);
	}
	
	/**
	 * Registers the specified command instance into the shell with the specified name.
	 * 
	 * @param name Name of command.
	 * @param command Command to be registered.
	 */
	public void register(String name, Command command) {
		Preconditions.checkArgument(name != null && !name.isEmpty(), "Parameter 'name' must not be [" + name + "]");
		Preconditions.checkArgument(command != null, "Parameter 'command' must not be [" + command + "]");
		commands.put(name, command);
	}
	
	/**
	 * Unregisters the command corresponding to the specified name from the shell.
	 * 
	 * @param name Name of command.
	 */
	public void unregister(String name) {
		Preconditions.checkArgument(name != null && !name.isEmpty(), "Parameter 'name' must not be [" + name + "]");
		commands.remove(name);
	}
	
	/**
	 * Returns the command map registered in the shell.
	 * 
	 * @return The command map registered in the shell.
	 */
	public Map<String, Command> commands() {
		return commands;
	}
	
	/**
	 * Executes the specified command line input.
	 * 
	 * @param line Command line input.
	 */
	@SuppressWarnings("unchecked")
	public void exec(String line) {
		String[] strings = line.split("\\s");
		
		if (strings.length == 0) {
			return;
		}
		
		String cmd = strings[0];
		
		if (strings[0] == null || strings[0].isEmpty()) {
			return;
		}
		
		String[] args = {};
		
		if (strings.length > 1) {
			args = new String[strings.length - 1];
			System.arraycopy(strings, 1, args, 0, args.length);
		}
		
		Command command = commands.get(cmd);
		
		if (command == null) {
			//$NON-NLS-N$
			console.println(cmd + ": command not found");
			return;
		}
		
		Usage usage = command.usage();
		Options opts = options.get(command.name());
		
		if (opts == null) {
			opts = new Options();
			
			for (Usage.Option option : usage.options()) {
				String arg = option.arg();
				Option opt = new Option(option.opt(), option.longOpt(), arg != null && !arg.isEmpty(), option.description());
				opt.setRequired(option.required());

				if (opt.hasArg()) {
					opt.setArgName(arg);
				}
				
				opts.addOption(opt);
			}
			
			options.put(command.name(), opts);
		}
		
		CommandLineParser parser = new GnuParser();
		CommandLine commandLine = null;
		
		try {
			commandLine = parser.parse(opts, args);
		} catch (ParseException e) {
			console().println(usage.toString());
			return;
		}
		
		Map<String, String> options = new HashMap<>();
		
		for (Option option : commandLine.getOptions()) {
			String opt = option.getOpt();
			
			if (opt != null && !opt.isEmpty()) {
				options.put(opt, option.getValue());
			}
			
			String longOpt = option.getLongOpt();
			
			if (longOpt != null && !longOpt.isEmpty()) {
				options.put(longOpt, option.getValue());
			}
		}
		
		Line l = new Line(cmd, options, commandLine.getArgList());
		
		try {
			command.run(l);
		} catch (Exception e) {
			e.printStackTrace(new PrintWriter(console.reader().getOutput()));
		}
	}
	
	private AtomicBoolean repl = new AtomicBoolean(false);
	
	/**
	 * Runs into the REPL mode.
	 */
	public void start() {
		repl.set(true);
		String line = null;
		
		while (repl.get() && ((line = console.readLine()) != null)) {
			exec(line);
		}
	}
	
	/**
	 * Quit from the REPL mode.
	 */
	public void stop() {
		repl.set(false);
	}

	public boolean started() {
		return repl.get();
	}

	/**
	 * @return the console
	 */
	public Console console() {
		return console;
	}

}
