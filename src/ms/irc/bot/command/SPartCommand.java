package ms.irc.bot.command;

import ms.irc.bot.Channel;
import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;
import ms.irc.bot.Nick;

public class SPartCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		if (m.getPrefix() == null || m.getParams() == null) {
			ircCore.putUM(m.toString());
		}
		
		String chanName = m.getParams()[0];
		Channel pChan = ircCore.getChannel(chanName);
		
		//this should never happen, as I have to join a channel bevor being able to see any PARTs
		if (pChan == null) {
			pChan = new Channel(chanName, ircCore);
			ircCore.addChannel(pChan);
		}
		
		Nick partNick = ircCore.getUser(m.getNick());
		if (partNick == null) {
			partNick = new Nick(m.getPrefix(), ircCore);
			ircCore.addUser(partNick);
		}
		
		pChan.deleteUser(partNick);

		ircCore.putUM("[" + chanName + "] <" + partNick.getNick() + "> left.");
		
	}

}
