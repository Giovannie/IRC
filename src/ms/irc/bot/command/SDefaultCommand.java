package ms.irc.bot.command;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;

public class SDefaultCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		ircCore.putUM(m.toString());
	}

}
