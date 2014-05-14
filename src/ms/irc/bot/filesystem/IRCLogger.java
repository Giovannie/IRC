package ms.irc.bot.filesystem;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * 
 * This class provides a Thread to log activity of the client.
 * Any Message that should be logged has to be added to the
 * LinkedBlockingQueue which this Class gets on creation.
 * 
 * @author Giovannie
 * @version 0.1.4
 *
 */
public class IRCLogger implements Runnable {
	

	private Logger logger;
	private FileHandler errLogHandler;
	private FileHandler logHandler;
	private LinkedBlockingQueue<LogRecord> logRecords;
	private static final int MAX_LOGSIZE = 1048576;
	private static final int MAX_FILES = 100;
	
	/**
	 * 
	 * Constructor of Class IRCLogger
	 * 
	 * @param logRecords the Queue where to get all new log entries.
	 * @throws SecurityException should never occur
	 * @throws IOException if unable to create/open log files
	 */
	public IRCLogger(LinkedBlockingQueue<LogRecord> logRecords) {
		//ini logger and queue
		if (logRecords == null)
			throw new IllegalArgumentException("Argument was a nullpointer.");
		this.logRecords = logRecords;
		logger = Logger.getLogger("ms.irc.bot");
		//clear (default-) logger.
		for (Handler handler : logger.getHandlers()) {
			logger.removeHandler(handler);
		}
		
		//put creating Info and Sysinfo to Queue
		logRecords.add(new LogRecord(Level.INFO, "Created new IRCLogger."));
		logRecords.add(new LogRecord(Level.INFO, "System: " + System.getProperties().toString()));
	}
	
	@Override
	public void run() {
		
		try {
			errLogHandler = new FileHandler("error.log", MAX_LOGSIZE, MAX_FILES, true);
			logHandler = new FileHandler("log.log", MAX_LOGSIZE, MAX_FILES, true);
		} catch (SecurityException | IOException e1) {
			throw new RuntimeException("IOException occured but could only throw RuntimeException^^");
		}
		errLogHandler.setLevel(Level.INFO);
		logHandler.setLevel(Level.ALL);
		logger.setLevel(Level.ALL);
		logger.addHandler(logHandler);
		logger.addHandler(errLogHandler);
		
		while (true) {
			
			//get new LogRecords if available, end on interrupt.
			LogRecord logRec = null;
			try {
				logRec = logRecords.take();
			} catch (InterruptedException e) {
				errLogHandler.close();
				logHandler.close();
				return;
			}
			
			//log this thing^^
			logger.log(logRec);
		}
	}

}
