package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class UAwayCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		String[] params = m.getParams();
		String msg = "";
		if (params != null) {
			msg = params[1];
			for (int i = 2; i < params.length; i++) {
					msg = msg + " " + params[i];
			}
		}
		if (m.getTrailing() != null)
			msg = msg + " :" + m.getTrailing();
		
		params = new String[1];
		params[0] = m.getParams()[0];
		ircCore.write(new Message(null, "AWAY", null, msg));
	}

}
