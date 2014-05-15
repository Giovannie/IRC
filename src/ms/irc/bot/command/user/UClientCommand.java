package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;
import ms.irc.bot.command.configure.CConfig;
import ms.irc.bot.command.configure.CReadConfig;
import ms.irc.bot.command.configure.CSaveConfig;
import ms.irc.bot.command.general.MessageCommand;

/**
 * 
 * a user command managing the clients config etc...
 * for more information see interface MassageCommand or
 * class IRCCommandManager.
 * 
 * @author Giovannie
 * @version 0.2.0
 *
 */
public class UClientCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		if (m.getParams() == null || m.getParams().length < 1) {
			ircCore.putUM("Error! Command needs at least 1 argument");
			return;
		}
		
		switch (m.getParams()[0].toLowerCase()) {
		case "saveconfig":
			new CSaveConfig().executeCommand(m, ircCore);
			break;
		case "config":
			new CConfig().executeCommand(m, ircCore);
			break;
		case "readconfig":
			new CReadConfig().executeCommand(m, ircCore);
			break;
		default:
			ircCore.putUM("Unknown client command.");
		}
	}

}
