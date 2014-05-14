package ms.irc.bot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Writes IRC Messages to a BufferedWriter
 * 
 * @author Giovannie
 * @version 0.1.2
 */
public class IRCWriter implements Runnable{
	
	private BufferedWriter writer;
	private LinkedBlockingQueue<Message> outMessages;
	private boolean running;
	
	/**
	 * Constructor. Initializes instance with a writer whith which to write
	 * messages and a LinkedBlockingQueue from where to get the messages
	 * 
	 * @param writer
	 * @param outMessages
	 */
	public IRCWriter(BufferedWriter writer, LinkedBlockingQueue<Message> outMessages) {
		if (writer == null || outMessages == null) {
			throw new IllegalArgumentException("Intempted to create an IRC Writer either without writer or message queue.");
		}
		this.writer = writer;
		this.outMessages = outMessages;
	}

	@Override
	public void run() {
		
		running = true;
		while (running || outMessages.peek() != null) {
		
		//NOTE: take() includes the wait() command if no message available.
		Message currentMessage = null;
		try {
			currentMessage = outMessages.take();
		} catch (InterruptedException e1) {
			return;//TODO: wtf?
		}
		
		
		
		//now write it:
		try {
			writer.write(currentMessage + "\r\n");
			writer.flush();
		} catch (IOException e) {
		    //happens f.e. if Connection closed
			return;
		}
		
		}
	}

	/**
	 * Method for ending the writer (afer
	 * @param b
	 */
	public void setRunning(boolean b) {
		running = b;
	}

}