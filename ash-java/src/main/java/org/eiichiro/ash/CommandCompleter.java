/*
 * Copyright (C) 2013 Eiichiro Uchiumi. All Rights Reserved.
 */
package org.eiichiro.ash;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jline.console.completer.Completer;

/**
 * {@code CommandCompleter}
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
class CommandCompleter implements Completer {

	private final Shell shell;
	
	CommandCompleter(Shell shell) {
		this.shell = shell;
	}
	
	/* (non-Javadoc)
	 * @see jline.console.completer.Completer#complete(java.lang.String, int, java.util.List)
	 */
	@Override
	public int complete(String buffer, int cursor, List<CharSequence> candidates) {
		SortedSet<String> commands = new TreeSet<>(shell.commands().keySet());
		
		if (buffer == null) {
			candidates.addAll(commands);
		} else {
			for (String match : commands.tailSet(buffer)) {
				if (!match.startsWith(buffer)) {
					break;
				}
				
				candidates.add(match);
			}
		}
		
		if (candidates.size() == 1) {
			candidates.set(0, candidates.get(0) + " ");
		}
		
		return candidates.isEmpty() ? -1 : 0;
	}

}
