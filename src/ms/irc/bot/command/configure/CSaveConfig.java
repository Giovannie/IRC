package ms.irc.bot.command.configure;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

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
