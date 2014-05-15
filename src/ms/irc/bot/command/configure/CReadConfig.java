package ms.irc.bot.command.configure;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;
import ms.irc.bot.command.general.MessageCommand;

public class CReadConfig implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		if (ircCore.readConfig()) {
			ircCore.putUM("Config reset to stored Version");
		} else {
			ircCore.putUM("Error! Could not load config from file.");
		}
	}

}
