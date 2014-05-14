package ms.irc.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author Giovannie
 * @version 0.1.0
 */
public class IRCReader implements Runnable {

	private BufferedReader reader;
	private LinkedBlockingQueue<Message> inMessages;
	
	public IRCReader(BufferedReader reader, LinkedBlockingQueue<Message> inMessages) {
		if (reader == null || inMessages == null) {
			throw new IllegalArgumentException("Intempted to create an IRCReader either without writer or message queue.");
		}
		this.reader = reader;
		this.inMessages = inMessages;
	}
	
	@Override
	public void run() {
		
		while (true) {
		//read the next line from the server...
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			//nothing special to do here, this happens if connection is closed.
			return;
		}
		
		if (line != null) {
			try {
				inMessages.put(new Message(line));
			} catch (InterruptedException e) {
				//got interrupted while waiting for Message to be put.
				return;
			}
		}
		
		}
	}

}