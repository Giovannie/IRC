package ms.irc.bot.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ms.irc.bot.IRCnet;

public class TerminalIn implements Runnable {

	private IRCnet client;
	
	public TerminalIn(IRCnet client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				client.userCommand(in.readLine());
			} catch (IOException e) {
				System.out.println("I/O Error");
			}
		}
	}

}
