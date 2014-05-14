package ms.irc.bot.command;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;

public class CSaveConfig implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		if (ircCore.saveConfig()) {
			ircCore.putUM("Config succesfully saved");
		} else {
			ircCore.putUM("Error! Could not save config correctly");
		}
	}

}
