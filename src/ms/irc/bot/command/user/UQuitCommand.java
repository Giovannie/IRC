package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;
import ms.irc.bot.command.general.MessageCommand;

public class UQuitCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		String message = "";
		if (m.getParams() != null) {
			for (String param : m.getParams()) {
				message = message + " " + param;
			}
		}
		if (m.getTrailing() != null && !m.getTrailing().equals("")) {
			message = message + " :" + m.getTrailing();
		}
		ircCore.disconnect(message);
	}

}
