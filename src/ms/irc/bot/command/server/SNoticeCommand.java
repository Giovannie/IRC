package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class SNoticeCommand implements MessageCommand {
	
	@Override
	public void executeCommand(Message m, IRCnet ircCore) {

		if (m.getParams() == null) {
			ircCore.putUM("<" + m.getNick() + " fl�stert>" + m.getTrailing());
		} else {
			ircCore.putUM("[" + m.getParams()[0] + "] <" + m.getNick() + " fl�stert>" + m.getTrailing());
		}
	}

}
