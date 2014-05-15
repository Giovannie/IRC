package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;
import ms.irc.bot.command.general.MessageCommand;

public class UActionCommand implements MessageCommand {

	/**
	 * Does basically the same as the /msg command, adding
	 * only the 001ACTION ...001 for the CTCP Action command
	 * (with 001 := ASCII char with number 1).
	 */
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
		
		//The part that differs from UMsgCommand.executeCommand().
		msg = (char)1 + "ACTION " + msg + (char)1;
		
		params = new String[1];
		params[0] = m.getParams()[0];
		ircCore.write(new Message(null, "PRIVMSG", params, msg));
	}

}
