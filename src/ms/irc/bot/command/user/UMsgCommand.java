package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class UMsgCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		String[] params = m.getParams();
		if (params == null || params.length < 1) {
			ircCore.putUM("Error. To few prameters.");
			return;
		}
		
		String msg = "";
		if (params != null && params.length >= 2)
			msg = params[1];
		for (int i = 2; i < params.length; i++) {
			msg = msg + " " + params[i];
		}
		if (m.getTrailing() != null)
			msg = msg + ":" + m.getTrailing();
		
		params = new String[1];
		params[0] = m.getParams()[0];
		ircCore.write(new Message(null, "PRIVMSG", params, msg));
	}

}
