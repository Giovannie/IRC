package ms.irc.bot.command.general;

import ms.irc.bot.IRCnet;
import ms.irc.bot.userdata.Message;

/**
 * Simple interface for Message processing.
 * 
 * @author Giovannie
 * @version 1.0.0
 */
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
