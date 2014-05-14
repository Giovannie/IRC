package ms.irc.bot.command;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;

public interface MessageCommand {

	/**
	 * This Method gets a Message m (read from IRC Server),
	 * executes all possibly included commands
	 * 
	 * @param m
	 * @return
	 */
	public void executeCommand(Message m, IRCnet ircCore);
}
