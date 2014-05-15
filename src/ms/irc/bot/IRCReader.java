package ms.irc.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import ms.irc.bot.userdata.Message;

/**
 * Class designed for nothing else than listening for
 * IRC messages on a given reader.
 * 
 * @author Giovannie
 * @version 0.1.1
 */
public class IRCReader implements Runnable {

	private BufferedReader reader;
	private LinkedBlockingQueue<Message> inMessages;
	
	/**
	 * Constructor of Class IRCReader.
	 * 
	 * @param reader a BufferedReader (with incoming IRC Messages)
	 * @param inMessages a LinkedBlockingQueue
	 */
	public IRCReader(BufferedReader reader, LinkedBlockingQueue<Message> inMessages) {
		if (reader == null || inMessages == null) {
			throw new IllegalArgumentException("Intempted to create an IRCReader either without writer or message queue.");
		}
		this.reader = reader;
		this.inMessages = inMessages;
	}
	
	/**
	 * Runs a while(true) queue all the time, waiting for incoming messages
	 * and writing them to the inMessages Queue.
	 */
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