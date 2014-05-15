package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class SPingCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		Message ret = new Message(null, "PONG", m.getParams(), m.getTrailing());
		ircCore.write(ret);
		String enabled = ircCore.getConfigEntry("pingEnabled");
		if (enabled != null && enabled.equalsIgnoreCase("true"))
		    ircCore.putUM("PING/PONG");
	}

}
