package ms.irc.bot.test;

import java.util.concurrent.LinkedBlockingQueue;

import ms.irc.bot.IRCnet;

public class TerminalOut implements Runnable {

	private IRCnet client;
	
	public TerminalOut(IRCnet client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		LinkedBlockingQueue<String> in = client.getUMQueue();
		while (true) {
			try {
				String line = in.take();
				System.out.println(line);
			} catch (InterruptedException e) {
				System.out.println("InterruptedException");
			}
		}
	}

}
