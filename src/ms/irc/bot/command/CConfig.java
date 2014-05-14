package ms.irc.bot.command;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;

public class CConfig implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		if (m.getParams().length < 3) {
			ircCore.putUM("Error! To few Params.");
		} else {
			ircCore.addConfigEntry(m.getParams()[1], m.getParams()[2]);
			ircCore.putUM("Config updated.");
		}
	}

}
