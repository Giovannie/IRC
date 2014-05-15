package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
import ms.irc.bot.userdata.Message;
import ms.irc.bot.userdata.Nick;

public class SPartCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		if (m.getPrefix() == null || m.getParams() == null) {
			ircCore.putUM(m.toString());
		}
		
		String chanName = m.getParams()[0];
		Channel pChan = ircCore.getDataManager().getChannel(chanName);
		
		//this should never happen, as I have to join a channel bevor being able to see any PARTs
		if (pChan == null) {
			pChan = new Channel(chanName, ircCore);
			ircCore.getDataManager().addChannel(pChan);
		}
		
		Nick partNick = ircCore.getDataManager().getNick(m.getNick());
		if (partNick == null) {
			partNick = new Nick(m.getPrefix(), ircCore);
			ircCore.getDataManager().addNick(partNick);
		}
		
		pChan.deleteUser(partNick);

		ircCore.putUM("[" + chanName + "] <" + partNick.getNick() + "> left.");
		
	}

}
