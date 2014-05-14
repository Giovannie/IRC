package ms.irc.bot.command;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;


public class SPrivmsgCommand implements MessageCommand {

	private SCTCPCommand ctcp = new SCTCPCommand();
	
	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		
		if (m.getTrailing() != null && m.getTrailing() != "" && m.getTrailing().charAt(0) == (char)1 ) {
			ctcp.executeCommand(m, ircCore);
			return;
		}
		
		if (m.getParams() == null) {
			ircCore.putUM("<" + m.getNick() + ">" + m.getTrailing());
		} else {
			ircCore.putUM("[" + m.getParams()[0] + "] <" + m.getNick() + ">" + m.getTrailing());
		}
	}

}
