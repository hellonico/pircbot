/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package net.hellonico.pirc;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;

import org.jibble.pircbot.PircBot;

import processing.core.PApplet;

public class IRCLibrary extends PircBot {
	
	PApplet myParent;
	HashMap commands;
	
	public final static String VERSION = "##library.prettyVersion##";
	
	public IRCLibrary(PApplet theParent) {
		this(theParent, "profile");
	}
	public IRCLibrary(PApplet theParent, String profile) {
		myParent = theParent;
		commands = new HashMap();
	
		try {
			Class klass = Class.forName("net.hellonico.potato.Potato");
			Constructor c = klass.getConstructor(PApplet.class);
			Object potato = c.newInstance(theParent);
			Method m = klass.getMethod("getSettings", String.class);
			HashMap settings = (HashMap) m.invoke(potato, "irc_"+profile);
			
			String server = (String) settings.get("server");
			int port = Integer.parseInt((String) settings.get("port"));
			String channel = (String) settings.get("channel");
			
			this.connect(server, port);
			this.joinChannel(channel);
			
		} catch (Exception e) {
			throw new RuntimeException("This is carrot day."+e.getMessage());
		}
	}
	
	public void register(String keyword) {
		Method fancyEventMethod;
		String methodName = "on"+keyword.substring(0,1).toUpperCase()+keyword.substring(1);
		try {
			fancyEventMethod = myParent.getClass().getMethod(methodName, new Class[] { String.class });
			commands.put(keyword, fancyEventMethod);
			System.out.println("Registered callback:"+methodName);
		} catch (Exception e) {
			System.out.println("Register Callback has no method in the main applet:"+methodName);
		}
	}
	public void unregister(String keyword) {
		commands.remove(keyword);
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		Iterator iter = commands.keySet().iterator();
		int blankIndex = message.indexOf(" ");
		String command = blankIndex < 0 ? message.substring(1) : message.substring(1, blankIndex );
		String passMessage = blankIndex > 0 && blankIndex + 1 < message.length() ? message.substring(message.indexOf(" ")+1) : "";
		System.out.println("Looking for command:"+command);
		while(iter.hasNext()) {
			String next = (String) iter.next();
			if(command.equalsIgnoreCase(next)) {
				Method m = (Method) commands.get(next);
				try {
					m.invoke(myParent, new Object[] { message.substring(message.indexOf(" ")+1) });
				} catch (Exception e) {
					System.out.println("Error while executing callback:"+e.getMessage());
				}
			}
		}
	}
	
	public static String version() {
		return VERSION;
	}
	

}

