package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

/**
 * 
 * @author Giovannie
 * @version 1.0a
 *
 */
public class UUnknownCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		ircCore.putUM("Error! Unknown command: " + m.getCommand());
	}

}
