package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class UConnectCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		String[] params = m.getParams();
		if (params == null || params.length < 2) {
			ircCore.putUM("Connect must have at least the following arguments: server, port");
			return;
		}
		
		String nick = ircCore.getNick();
		if (params.length <= 2) {
			try {
				ircCore.connect(params[0], Integer.parseInt(params[1]), nick);
			} catch (NumberFormatException e) {
				ircCore.putUM("Connect must have a valid port number as second argument");
				return;
			}
		} else {
			try {
				ircCore.connect(params[2], params[0], Integer.parseInt(params[1]), nick);
			} catch (NumberFormatException e) {
				ircCore.putUM("Connect must have a valid port number as second argument");
			}
		}
		ircCore.putUM("Connecting to Server: " + params[0]);
	}

}
