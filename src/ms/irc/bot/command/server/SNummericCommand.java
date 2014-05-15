package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;
import ms.irc.bot.command.general.MessageCommand;

public class SNummericCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		int t = Integer.parseInt(m.getCommand());
		
		if (t == 4) {
			ircCore.register();
		}
		
		ircCore.putUM(m.toString());
	}

}
