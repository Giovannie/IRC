package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;
import ms.irc.bot.command.general.MessageCommand;

/**
 * 
 * @author Giovannie
 * @version 1.0a
 *
 */
public class UPartCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		String[] params = m.getParams();
		if (params == null || params.length != 1) {
			ircCore.putUM("Error. Got either to many or to few Arguments.");
			return;
		}
		ircCore.write(new Message(null, "PART", params, null));
		ircCore.getChannels().remove(m.getParams()[0]);
	}

}
