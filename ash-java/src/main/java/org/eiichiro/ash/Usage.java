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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * {@code Usage} represents a command usage.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Usage {

	private final String syntax;
	
	private Set<Option> options = new TreeSet<>(new Comparator<Option>() {

		@Override
		public int compare(Option o1, Option o2) {
			return o1.opt.compareTo(o2.opt);
		}
		
	});
	
	/**
	 * Constructs a new {@code Usage} with the specified command syntax.
	 * 
	 * @param syntax Command syntax.
	 */
	public Usage(String syntax) {
		this.syntax = syntax;
	}
	
	/**
	 * Returns the command syntax.
	 * 
	 * @return The command syntax.
	 */
	public String syntax() {
		return syntax;
	}
	
	public Set<Option> options() {
		return options;
	}
	
	public void option(String opt, boolean required, String description) {
		option(opt, null, required, description, null);
	}
	
	public void option(String opt, boolean required, String description, String arg) {
		option(opt, null, required, description, arg);
	}
	
	public void option(String opt, String longOpt, boolean required, String description) {
		option(opt, longOpt, required, description, null);
	}
	
	public void option(String opt, String longOpt, boolean required, String description, String arg) {
		Option[] opts = options.toArray(new Option[options.size()]);
		
		for (int i = 0; i < opts.length; i++) {
			if ((opts[i].opt != null && opts[i].opt.equals(opt))
					|| (opts[i].longOpt != null && opts[i].longOpt.equals(longOpt))) {
				options.remove(opts[i]);
			}
		}
		
		options.add(new Option(opt, longOpt, required, description, arg));
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String usage = "usage: ";
		String string = usage + syntax;
		int tab = usage.length() + syntax.indexOf(" ") + 1;
		String padding = StringUtils.leftPad("", tab);
		int pos = wrap(string, 0);
		boolean end = false;
		
		while (!end) {
			if (pos == -1) {
				builder.append(StringUtils.stripEnd(string, null));
				end = true;
			} else {
				builder.append(StringUtils.stripEnd(string.substring(0, pos), null)).append('\n');
				string = padding + string.substring(pos).trim();
				pos = wrap(string, tab);
			}
		}
		
		if (!options.isEmpty()) {
			String lpad = StringUtils.leftPad("", 1);
			String dpad = StringUtils.leftPad("", 3);
			int max = 0;
			List<StringBuilder> opts = new ArrayList<>();
			Iterator<Option> iterator = options.iterator();
			
			while (iterator.hasNext()) {
				StringBuilder b = new StringBuilder();
				Option option = iterator.next();
				
				if (option.opt() == null || option.opt().isEmpty()) {
					b.append(lpad).append("   --" + option.longOpt());
				} else {
					b.append(lpad).append("-" + option.opt());
					
					if (option.longOpt() != null && !option.longOpt().isEmpty()) {
						b.append(",--" + option.longOpt());
					}
				}
				
				if (option.arg() != null && !option.arg().isEmpty()) {
					b.append(" <" + option.arg() + ">");
				}
				
				opts.add(b);
				
				if (b.length() > max) {
					max = b.length();
				}
			}
			
			iterator = options.iterator();
			int i = 0;
			tab = max + dpad.length();
			
			while (iterator.hasNext()) {
				StringBuilder b = opts.get(i);
				
				if (b.length() < max) {
					b.append(StringUtils.leftPad("", max - b.length()));
				}
				
				b.append(dpad);
				Option option = iterator.next();
				
				if (option.description() != null && !option.description().isEmpty()) {
					b.append(option.description);
				}
				
				string = b.toString();
				pos = wrap(string, tab);
				end = false;
				
				while (!end) {
					if (pos == -1) {
						builder.append(StringUtils.stripEnd(string, null));
						end = true;
					} else {
						builder.append(StringUtils.stripEnd(string.substring(0, pos), null)).append('\n');
						string = StringUtils.leftPad("", tab) + string.substring(pos).trim();
						pos = wrap(string, tab);
					}
				}
				
				if (iterator.hasNext()) {
					builder.append('\n');
				}
			}
		}
		
		return builder.toString();
	}
	
	private int wrap(String string, int from) {
		int pos = -1;
		
		if ((pos = string.indexOf('\n', from)) != -1 && pos <= Terminal.width) {
			return pos + 1;
		}
		
		if (string.length() <= Terminal.width) {
			return -1;
		}
		
		pos = Terminal.width;
		
		while (pos >= from) {
			if (string.charAt(pos) == ' ' || string.charAt(pos) == '\t') {
				return pos;
			}
			
			pos--;
		}
		
		pos = Terminal.width + 1;
		
		while (pos <= string.length()) {
			if (string.charAt(pos) == ' ' || string.charAt(pos) == '\t') {
				return pos;
			}
			
			pos++;
		}
		
		return -1;
	}
	
	static class Option {

		private final String opt;
		
		private final String longOpt;
		
		private final boolean required;
		
		private final String description;
		
		private final String arg;
		
		private Option(String opt, String longOpt, boolean required, String description, String arg) {
			Preconditions.checkArgument(!(opt == null || opt.isEmpty()), "Parameter 'opt' must not be [" + opt + "]");
			this.opt = opt;
			this.longOpt = longOpt;
			this.required = required;
			this.description = description;
			this.arg = arg;
		}
		
		String opt() {
			return opt;
		}
		
		String longOpt() {
			return longOpt;
		}
		
		boolean required() {
			return required;
		}
		
		String description() {
			return description;
		}
		
		String arg() {
			return arg;
		}
		
	}
	
}
