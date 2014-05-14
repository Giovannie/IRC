package ms.irc.bot.command;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;

public class UCTCPCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {

		String[] params = m.getParams();
		if (params == null || params.length != 2) {
			ircCore.putUM("Error. Needs 2 Parameters <target> <ctcpcommand>.");
			return;
		}
		
		String msg = ":" + (char)1 + params[1].toUpperCase();
		if (params[1].toUpperCase().equals("PING"))
			msg += " " + System.currentTimeMillis();
		msg += "" + (char)1;
		String[] mparams = new String[1];
		mparams[0] = params[0];
		ircCore.write(new Message(null, "PRIVMSG", mparams, msg));
	}

}
