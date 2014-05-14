package ms.irc.bot.command;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;

public class SPingCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		Message ret = new Message(null, "PONG", m.getParams(), m.getTrailing());
		ircCore.write(ret);
		ircCore.putUM("PING/PONG");
	}

}
