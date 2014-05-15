package ms.irc.bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import ms.irc.bot.command.general.IRCCommandManager;
import ms.irc.bot.filesystem.IRCFileSystem;
import ms.irc.bot.filesystem.IRCLogger;
import ms.irc.bot.userdata.Channel;
import ms.irc.bot.userdata.Message;
import ms.irc.bot.userdata.Nick;

/**
 * This is the Core of the IRC Client. May be used
 * as an underlaying structure for (GUI-based)
 * IRC-Clients or IRC-Bot.
 * 
 * TODO: UserManagement
 * 
 * TODO: Restart
 * 
 * TODO: LinkedBlockingQueue... use either add or put, not both!
 * 
 * TODO: Unimplemented user commands (command structure?)
 * 
 * TODO: forceDisconnect function that terminates the writer, no matter what messages
 * 		shall still be written.
 * 
 * TODO: decoding does not work with Latin/Unicode Hybrid Encoding
 * 
 * TODO: /msg bla: or /notice bla: puts an aditional blank befor the :
 * 
 * TODO: add Javadoc comments
 * 
 * TODO: Logging Structure rewrite, don't use IRCnet.java to pass messages to the Logger.
 * 
 * @author Giovannie
 * @version 0.3.5
 *
 */
public class IRCnet implements Thread.UncaughtExceptionHandler{

	private String server;
	private ArrayList<Channel> channel;
	private ConcurrentHashMap<String, Nick> nicks;
	private int port = -1;
	private String nick;
	private String serverPW;
	private String user = "aicab";
	private String buildNr = "0.3.5";
	
	//some properties and Logger containing the log and config data.
	private IRCLogger ircLogger;
	private Properties config;
	
	//the stuff needed for network connection. Note: Not ThreadSafe.
	private Socket socket;
	private BufferedWriter writer;
	private BufferedReader reader;
	
	//the Queues of input and output Messages.
	private LinkedBlockingQueue<Message> inMessages;
	private LinkedBlockingQueue<Message> outMessages;
	private LinkedBlockingQueue<Message> userCommands;
	private LinkedBlockingQueue<Message> blockedUserCommands;
	private LinkedBlockingQueue<String> userStrings;
	private LinkedBlockingQueue<LogRecord> logRecords;
	
	//Note: might be modified simultaneously by various threads...
	private boolean registered;
	
	//the Threads in which the Writer and Reader (see IRCWriter.java and IRCReader.java) will run.
	private Thread ircWriterThread;
	private Thread ircReaderThread;
	private Thread ircCommandThread;
	private Thread userCommandThread;
	private Thread ircLoggerThread;
	
	private IRCWriter ircWriter;
	
	/**
	 * Constructor of Class IRCnet
	 */
	public IRCnet() {
		
		registered = false;
		channel = new ArrayList<Channel>();
		nicks = new ConcurrentHashMap<String, Nick>();
		
		//initialize the config
		config = new Properties();
		
		//initialize Queues
		inMessages = new LinkedBlockingQueue<Message>();
		outMessages = new LinkedBlockingQueue<Message>();
		userCommands = new LinkedBlockingQueue<Message>();
		blockedUserCommands = new LinkedBlockingQueue<Message>();
		userStrings = new LinkedBlockingQueue<String>();
		logRecords = new LinkedBlockingQueue<LogRecord>();
		
		//put first log info
		addLogEntry(Level.INFO, "Started Client.");
		
		//start the logger
		startLoggerThread();
	}
	
	/**
	 * writes the Message (over an existing connection)
	 * if no connection present the reaction is undefined.
	 * It might result in sending the message over any
	 * future connection (probably before sending the
	 * NICK and USER command) or this message might be
	 * completely lost.
	 * 
	 * Note: deprecated. please use userCommand whenever possible.
	 * 
	 * @param m
	 * @return
	 */
	public boolean write(Message m) {
		try {
			outMessages.put(m);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	/**
	 * connects to a specified Server and port.
	 * 
	 * @param server
	 * @param port
	 * @param nick
	 * @return
	 */
	public boolean connect(String server, int port, String nick) {
		
		addLogEntry(Level.INFO, "Trying to log on. Server: " + server + " Port: " + port + " Nick: " + nick);
		
		registered = false;
		
		//initializes Socket and reader/writer
		if (!iniConnect(server, port))
			return false;

		//start the Reader and Writer Threads
		if (!startReaderWriter())
			return false;
		
		//start the CommandManagerThreads
		if (!startCommandThread())
			return false;
		
		//register Nick and User
		if (!registerNickUser(nick))
			return false;
		
		//reset server password to none.
		serverPW = null;

		return true;
	}

	/**
	 * connects to a specified Server and port using the password to log on.
	 * 
	 * @param serverPW
	 * @param server
	 * @param port
	 * @param nick
	 * @return
	 */
	public boolean connect(String serverPW, String server, int port, String nick) {

		
		addLogEntry(Level.INFO, "Trying to log on. Server: " + server + " Port: " + port + " Nick: " + nick + " Password: " + serverPW);
		registered = false;
		
		//initializes Socket and reader/writer
		if (!iniConnect(server, port))
			return false;
		
		//start the Reader and Writer Threads
		if (!startReaderWriter())
			return false;
		
		//start the CommandManagerThreads
		if (!startCommandThread())
			return false;
		
		//send the PASS command
		String params[] = new String [1];
		params[0] = serverPW;
		try {
			outMessages.put(new Message(null, "PASS", params, null));
			this.serverPW = serverPW;
		} catch (InterruptedException e) {
			return false;
		}
		
		//register Nick and User
		if (!registerNickUser(nick)) 
			return false;
		
		return true;
	}
	
	/**
	 * disconnects from server with QUIT.
	 * 
	 * result is equal to disconnect("")
	 * 
	 * Note: waits for all messages to be written, if some
	 * thread is putting new messages may wait forever...
	 * 
	 * @return
	 */
	public boolean disconnect() {
		return disconnect("");
	}
	
	/**
	 * disconnects from server with QUIT sending the 
	 * specified String as a QUIT message.
	 * 
	 * Note: waits for all messages to be written, if some
	 * thread is putting new messages may wait forever...
	 * 
	 * @param m
	 * @return
	 */
	public boolean disconnect(String m) {
		
		addLogEntry(Level.INFO, "Disconnecting.");
		
		write(new Message(null, "QUIT", null, " " + m));
		/*
		 * End the Reader/Writer Threads on this connection
		 * 
		 * Note: waits for the Writer to write all pending messages,
		 * may wait forever if some thread is still putting new messages in the queue.
		 */
		ircWriter.setRunning(false);
		try {
			ircWriterThread.join();
		} catch (InterruptedException e1) {
			uncaughtException(ircWriterThread, e1);//TODO: wtf?
		}
		ircReaderThread.interrupt();		
		
		try {
			socket.close();
		} catch (IOException e) {
			addLogEntry(Level.INFO, "Error on closeing socket.");
			putUM("Could not close connection correctly.");//TODO: show this only in debug Mode
		}
		
		return false;
	}
	
	/**
	 * same as disconnect(String m) but clears the Queues afterwards.
	 * 
	 * @param m
	 * @return
	 */
	public boolean disconnectClean(String m) {
		boolean ret = disconnect(m);
		if (ret)
			clearQueues();
		return ret;
	}
	
	/**
	 * reconnects with the settings of the last connect.
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean reconnect() throws InterruptedException {
		if (server == null || port <= 0) {
			return false;
		}
		if (serverPW != null) {
			return (disconnect("Reconnecting") && connect(serverPW, server, port, nick));
		} else {
			return (disconnect("Reconnecting") && connect(server, port, nick));
		}
	}

	/**
	 * Executes user commands. A user command is one String line starting with
	 * "/<command>" with <command> as a valid command of this irc client.
	 * for help with the valid commands sie command help.
	 * 
	 * Note if no command specified client will add /msg at the beginning.
	 * 
	 * @param command
	 * @return true if user command successfully executed
	 */
	public boolean userCommand(String command) {
		
		addLogEntry("UserCommand: " + command);
		
		if (!command.startsWith("/"))
			command = "/msg " + command;
		try {
			if (!registered && !(command.startsWith("/nick") || command.startsWith("/user") || command.startsWith("/pass")
					|| command.startsWith("/quit") || command.startsWith("/connect"))) {
				blockedUserCommands.add(new Message(command));
				putUM(command.split(" ")[0] + "(will be processed later)");
				return false;
			} else {
				userCommands.put(new Message(command));
//				putUM(command);
				return true;
			}
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	/**
	 * adds a user message to the queue of user messages which should be displayed to the
	 * user by the user interface (not part of the client)
	 * 
	 * @param s
	 * @return
	 */
	public boolean putUM(String s) {
		try {
			userStrings.put(s);
			addLogEntry("UserMessage: " + s);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	public String pollUM() throws InterruptedException {
		return userStrings.poll();
	}
	
	public LinkedBlockingQueue<String> getUMQueue() {
		return userStrings;
	}

	//TODO: ArrayList is NOT Threadsafe...
	public ArrayList<Channel> getChannels() {
		return channel;
	}

	/**
	 * Adds a channel to the internal list of known channels.
	 * 
	 * @param chan a Channel
	 */
	public void addChannel(Channel chan) {
		channel.add(chan);
	}

	/**
	 * Searches the internal channel list for a channel with the given name.
	 * 
	 * @param chanName a String
	 * @return a Channel
	 */
	public Channel getChannel(String chanName) {
		for (Channel chan : channel) {
			if (chan.getChanName().equals(chanName)) {
				return chan;
			}
		}
		return null;
	}

	public void register() {
		registered = true;
		
		//write blocked commands to CommandQueue
		Message m;
		while ((m = blockedUserCommands.poll()) != null) {
			userCommands.add(m);
		}
	}
	
	/**
	 * TODO: extract user management to an external class
	 *     in package ms.irc.bot.userdata
	 * @return
	 */
	public String getNick() {
		return nick;
	}
	
	public Nick getUser(String nick) {
		if (nick == null) {
			return null;
		}
		return nicks.get(nick);
	}
	
	public void addUser(Nick nick) {
		nicks.put(nick.getNick(), nick);
	}
	
	public void addLogEntry(String message) {
		addLogEntry(Level.FINEST, message);
	}
	
	public void addLogEntry(Level lvl, String message) {

		//lookup if log flag set (or if loglevel higher than fine) and log message.
		if ((new String("true")).equalsIgnoreCase(config.getProperty("log"))
				|| (lvl != Level.FINEST && lvl != Level.FINER && lvl != Level.FINE))
			logRecords.add(new LogRecord(lvl, message));
	}
	
	public void addConfigEntry(String name, String value) {
		config.put(name.toLowerCase(), value.toLowerCase());
		addLogEntry(Level.INFO, "Config Update: " + name + ", " + value);
	}
	
	public String getConfigEntry(String name) {
	    if (name == null)
	        return null;
	    String ret = config.getProperty(name.toLowerCase());
	    addLogEntry("Config Read: key: " + name + ", value: " + ret);
	    return ret;
	}
	
	public boolean saveConfig() {
		if (IRCFileSystem.writeProperties(config, "config")) {
			addLogEntry(Level.INFO, "Config Update: Config written to file.");
			return true;
		}
		addLogEntry(Level.WARNING, "Config Update: Could not write to file.");
		return false;
	}
	
	public boolean readConfig() {
		Properties c = IRCFileSystem.readProperties("config");
		if (c != null) {
			config = c;
			addLogEntry(Level.INFO, "Config Update: Config read from file.");
			return true;
		}
		addLogEntry(Level.WARNING, "Config Update: Could not read from file.");
		return false;
	}
	
	public void exit() {
		exit("");
	}
	
	public void exit(String message) {
		addLogEntry(Level.INFO, "Client Closeing.");
		disconnect(message);//disconnect should end Reader/Writer
		userCommandThread.interrupt();
		ircCommandThread.interrupt();
		ircLoggerThread.interrupt();
		try {
			ircWriterThread.join();
			ircReaderThread.join();
			userCommandThread.join();
			ircCommandThread.join();
			ircLoggerThread.join();
		} catch (InterruptedException e) {
			//haha
		}
		
		System.exit(0);
	}

	/**
	 * here I'm catching all Exceptions and restarting the corresponding modul.
	 * 
	 * TODO: separate exception Handling from core class
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		if (e.getClass().equals(InterruptedException.class)) {
			//Interrupted Exceptions a thrown when I interrupt Threads to close them.
			return;
		}
		
		//put a userMessage for the user to know the problem.
		if (config != null && new String("true").equalsIgnoreCase(config.getProperty("debug"))) {
			putUM("Error.");
			putUM(e.toString());
			for (StackTraceElement ste : e.getStackTrace()) {
				putUM(ste.toString());
			}
			putUM("However the Exception occurred, programm will try to continue.");
		}
		
		//log the Error
		String errorString = e.toString();
		for (StackTraceElement ste : e.getStackTrace()) {
			errorString +=  "-|-" + ste.toString();
		}
		addLogEntry(Level.WARNING, errorString);
		
		//"switch" on the Threads and restart the dead one. (only that switch isn't supported on Threads)
		if (t == ircWriterThread) {
			ircWriterThread = new Thread(new IRCWriter(writer, outMessages));
			ircWriterThread.setUncaughtExceptionHandler(this);
			ircWriterThread.start();
			return;
		}
		if (t == ircReaderThread) {
			ircReaderThread = new Thread(new IRCReader(reader, inMessages));
			ircReaderThread.setUncaughtExceptionHandler(this);
			ircReaderThread.start();
			return;
		}
		if (t == ircCommandThread) {
			ircCommandThread = new Thread(new IRCCommandManager(inMessages, this, true));
			ircCommandThread.setUncaughtExceptionHandler(this);
			ircCommandThread.start();
			return;
		}
		if (t == userCommandThread) {
			userCommandThread = new Thread(new IRCCommandManager(userCommands, this, false));
			userCommandThread.setUncaughtExceptionHandler(this);
			userCommandThread.start();
		}
		if (t == ircLoggerThread) {
			startLoggerThread();
		}
	}
	
	/**
	 * initializes the Socket and BufferedReader/-Writer to read/write
	 * to/from the server.
	 * 
	 * @param server
	 * @param port
	 * @return
	 */
	private boolean iniConnect(String server, int port) {
		try {
			socket = new Socket(server, port);
		    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream( ), "UTF-8"));
		    reader = new BufferedReader(new InputStreamReader(socket.getInputStream( ), "UTF-8"));
		    this.server = server;
		    this.port = port;
		    return true;
		} catch (IOException e) {
			return false;
		}
		
	}
	
	private boolean startReaderWriter() {

		//start the writer
		ircWriter = new IRCWriter(writer, outMessages);
		ircWriterThread = new Thread(ircWriter);
		ircWriterThread.setUncaughtExceptionHandler(this);
		ircWriterThread.start();
		addLogEntry(Level.INFO, "Startet ircWriterThread.");
		
		//start the reader
		ircReaderThread = new Thread(new IRCReader(reader, inMessages));
		ircReaderThread.setUncaughtExceptionHandler(this);
		ircReaderThread.start();
		addLogEntry(Level.INFO, "Startet ircReaderThread.");
		
		return true;
	}
	
	private boolean startCommandThread() {
		
		//the Command Thread managing the server commands
		ircCommandThread = new Thread(new IRCCommandManager(inMessages, this, true));
		ircCommandThread.setUncaughtExceptionHandler(this);
		ircCommandThread.start();
		addLogEntry(Level.INFO, "Startet ircCommandThread.");
		
		//the Command Thread managing the user commands
		userCommandThread = new Thread(new IRCCommandManager(userCommands, this, false));
		userCommandThread.setUncaughtExceptionHandler(this);
		userCommandThread.start();
		addLogEntry(Level.INFO, "Startet userCommandThread.");
		
		return true;
	}
	
	private boolean startLoggerThread() {

		ircLogger = new IRCLogger(logRecords);
		ircLoggerThread = new Thread(ircLogger);
		ircLoggerThread.setUncaughtExceptionHandler(this);
		ircLoggerThread.start();
		addLogEntry(Level.INFO, "Startet ircLoggerThread.");
		return true;
	}
	
	private boolean registerNickUser(String nick) {

		//TODO: create the loop that tries to log on with the next nickname if
		//nick already in use. Check if successfully registered.
		
		//put the first Messages
		
		//NICK command
		String[] params = new String[1];
		params[0] = nick;
		try {
			outMessages.put(new Message(null, "NICK", params, null));
		} catch (InterruptedException e) {
			return false;
		}
		//USER command
		params = new String[3];
		params[0] = user;
		params[1] = "\"\"";
		params[2] = "\"" + server + "\"";
		try {
			outMessages.put(new Message(null, "USER", params, "N/A"));
		} catch (InterruptedException e) {
			return false;
		}
		
		this.nick = nick;
		
		addLogEntry(Level.INFO, "Registered Nick and User.");
		
		return true;
	}
	
	private void clearQueues() {

		inMessages.clear();
		outMessages.clear();
		userCommands.clear();
		blockedUserCommands.clear();
		userStrings.clear();
	}

	public String getBuildNumber() {
		return buildNr;
	}
}