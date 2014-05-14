package ms.irc.bot.command;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;

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
