package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class UEasyCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		
		ircCore.write(new Message(null, m.getCommand().substring(1).toUpperCase(), m.getParams(), m.getTrailing()));
	}

}
