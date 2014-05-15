package ms.irc.bot.command.general;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.server.SDefaultCommand;
import ms.irc.bot.command.server.SJoinCommand;
import ms.irc.bot.command.server.SNickCommand;
import ms.irc.bot.command.server.SNoticeCommand;
import ms.irc.bot.command.server.SNummericCommand;
import ms.irc.bot.command.server.SPartCommand;
import ms.irc.bot.command.server.SPingCommand;
import ms.irc.bot.command.server.SPrivmsgCommand;
import ms.irc.bot.command.server.SQuitCommand;
import ms.irc.bot.command.user.UActionCommand;
import ms.irc.bot.command.user.UAwayCommand;
import ms.irc.bot.command.user.UCTCPCommand;
import ms.irc.bot.command.user.UClientCommand;
import ms.irc.bot.command.user.UConnectCommand;
import ms.irc.bot.command.user.UEasyCommand;
import ms.irc.bot.command.user.UExitCommand;
import ms.irc.bot.command.user.UHelpCommand;
import ms.irc.bot.command.user.UJoinCommand;
import ms.irc.bot.command.user.UMsgCommand;
import ms.irc.bot.command.user.UNoticeCommand;
import ms.irc.bot.command.user.UPartCommand;
import ms.irc.bot.command.user.UQuitCommand;
import ms.irc.bot.command.user.UUnknownCommand;
import ms.irc.bot.userdata.Message;

/**
 * The IRCCommandManager
 * 
 * @author Giovannie
 * @version 0.2.1
 *
 */
public class IRCCommandManager implements Runnable {

	private LinkedBlockingQueue<Message> inMessages;
	private IRCnet netCore;
	
	private HashMap<String, MessageCommand> commands;
	private MessageCommand defaultCmd;
	
	public IRCCommandManager(LinkedBlockingQueue<Message> inMessages, IRCnet netCore, boolean server) {
		
		if (inMessages == null ||  netCore == null)
			throw new IllegalArgumentException("Arguments must be not null.");
		
		this.inMessages = inMessages;
		this.netCore = netCore;
		
		commands = new HashMap<String, MessageCommand>();
		
		if (server) {
			initializeCommands();
		} else {
			initializeCommandsUser();
		}
	}

	@Override
	public void run() {
		
		while (true) {
		
		//NOTE: take() includes the wait() command if no message available.
		Message currentMessage = null;
		try {
			currentMessage = inMessages.take();
		} catch (InterruptedException e1) {
			//normal Exception if getting closed.
			return;
		}
		
		//log the message (if log = true in config)
		netCore.addLogEntry(currentMessage.toString());
		
		//look if its a Numeric Command
		try {
			Integer.parseInt(currentMessage.getCommand());
			commands.get("NUMERIC").executeCommand(currentMessage, netCore);
			continue;
		} catch (NumberFormatException e) {
			//if not a Number, no Problem, do nothing and search for the command...
		}
		
		//Search for command
		MessageCommand cmd = commands.get(currentMessage.getCommand());
		if (cmd != null) {
			cmd.executeCommand(currentMessage, netCore);
		} else {
			defaultCmd.executeCommand(currentMessage, netCore);
		}
		
		}
	}
	
	/*
	 * TODO: implement unimplemented commands
	 */
	private void initializeCommands() {
		defaultCmd = new SDefaultCommand();
		
//		commands.put("PASS", TODO);
//		commands.put("NICK", TODO);
//		commands.put("USER", value);
		
		commands.put("QUIT", new SQuitCommand());
//		commands.put("SQUIT", value);//probably I'll never use this
		
		commands.put("JOIN", new SJoinCommand());
		commands.put("PART", new SPartCommand());
//		commands.put("MODE", new SModeCommand());
//		commands.put("TOPIC", value);
//		commands.put("NAMES", value);
		commands.put("NICK", new SNickCommand());
//		commands.put("LIST", arg1);
//		commands.put("INVITE", value);
//		commands.put("KICK", value);
		
//		commands.put("VERSION", value);
//		commands.put("STATS", value);
//		commands.put("LINKS", value);
//		commands.put("TIME", value);
		
		
		commands.put("PING", new SPingCommand());
		commands.put("PRIVMSG", new SPrivmsgCommand());
		commands.put("NOTICE", new SNoticeCommand());//TODO: incoming CTCP Responses
		commands.put("NUMERIC", new SNummericCommand());
		
	}
	
	/*
	 * TODO: implement unimplemented commands (some more might be implemented
	 * 		by UEasyCommand which just sends the command (in capitals without
	 * 		leading / ) and parses all the rest as simple params.
	 */
	private void initializeCommandsUser() {
		defaultCmd = new UUnknownCommand();
		MessageCommand easyCommand = new UEasyCommand();
		commands.put("/away", new UAwayCommand());
		commands.put("/cc", new UClientCommand());
		commands.put("/connect", new UConnectCommand());
//		commands.put("/cnotice", new UCNoticeCommand());
//		commands.put("/cprivmsg", new UCPrivmsgCommand());
		commands.put("/ctcp", new UCTCPCommand());
		commands.put("/exit", new UExitCommand());
		commands.put("/help", new UHelpCommand());
		commands.put("/info", easyCommand);
		commands.put("/invite", easyCommand);
		commands.put("/ison", easyCommand);
		commands.put("/join", new UJoinCommand());
//		commands.put("/kick", new UKickCommand());
//		commands.put("/knock", new UKnockCommand());
		commands.put("/list", easyCommand);
		commands.put("/me", new UActionCommand());
		commands.put("/mode", easyCommand);
		commands.put("/names", easyCommand);
		commands.put("/nick", easyCommand);
		commands.put("/notice", new UNoticeCommand());
		commands.put("/oper", easyCommand);
		commands.put("/part", new UPartCommand());
		commands.put("/msg", new UMsgCommand());
		commands.put("/quit", new UQuitCommand());
		commands.put("/time", easyCommand);
		commands.put("/topic", easyCommand);
		commands.put("/trace", easyCommand);
		commands.put("/userhost", easyCommand);
		commands.put("/userip", easyCommand);
		commands.put("/version", easyCommand);
//		commands.put("/wallops", new UWallopsCommand());
		commands.put("/watch", easyCommand);
//		commands.put("/who", new UWhoCommand());
		commands.put("/whois", easyCommand);
		commands.put("/whowas", easyCommand);
	}

}
