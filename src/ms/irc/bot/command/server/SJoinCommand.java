package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
import ms.irc.bot.userdata.Message;
import ms.irc.bot.userdata.Nick;

public class SJoinCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		if (m.getPrefix() == null || m.getParams() == null) {
			ircCore.putUM(m.toString());
		}
		
		String chanName = m.getParams()[0];
		Channel jChan = ircCore.getChannel(chanName);
		
		//test if jChan exists (if not create it)
		if (jChan == null) {
			jChan = new Channel(chanName, ircCore);
			ircCore.addChannel(jChan);
		}
		
		Nick newNick = ircCore.getUser(m.getNick());
		if (newNick == null) {
			newNick = new Nick(m.getPrefix(), ircCore);
			ircCore.addUser(newNick);
		}
		jChan.addUser(newNick);
		
		ircCore.putUM("[" + chanName + "] <" + newNick.getNick() + "> joined.");
	}

}
