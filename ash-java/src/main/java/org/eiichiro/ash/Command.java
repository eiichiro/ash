/*
 * Copyright (C) 2013 Ash Uchiumi. All Rights Reserved.
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

/**
 * {@code Command} is a script to be run by Ash {@link Shell}.
 * You can declare your own command class by implementing this interface and 
 * register a command instance into the shell with its command name via 
 * {@code Shell#register} method.
 * 
 * @author <a href="mailto:mail@eiichiro.org">Eiichiro Uchiumi</a>
 */
public interface Command {

	/**
	 * Returns the command name.
	 * 
	 * @return The command name.
	 */
	public String name();
	
	/**
	 * Returns the command usage.
	 * 
	 * @return The command usage.
	 */
	public Usage usage();
	
	/**
	 * Runs a command with the specified command line.
	 * 
	 * @param line Command line input.
	 */
	public void run(Line line) throws Exception;
	
}
