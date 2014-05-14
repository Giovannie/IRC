package ms.irc.bot.test;

import ms.irc.bot.IRCnet;

public class Test {

	public static void main(String[] args) {
		
		IRCnet client = new IRCnet();
		client.connect("portlane.se.quakenet.org", 6667, "AlphatestIRC0");
		new Thread(new TerminalIn(client)).start();
		new Thread(new TerminalOut(client)).start();
	}

}
