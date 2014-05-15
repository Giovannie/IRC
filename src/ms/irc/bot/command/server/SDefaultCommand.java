package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class SDefaultCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		ircCore.putUM(m.toString());
	}

}
