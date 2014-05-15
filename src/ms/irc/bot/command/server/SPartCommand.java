package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
import ms.irc.bot.userdata.DataManager;
import ms.irc.bot.userdata.Message;
import ms.irc.bot.userdata.Nick;

/**
 * command class for incoming PART commands
 * 
 * @author Giovannie
 * @version 1.0.2
 */
public class SPartCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
	    
	    /*
	     * check params
	     */
		if (m.getPrefix() == null || m.getParams() == null) {
			ircCore.putUM(m.toString());
			return;
		}
		
		/*
		 * adding/updating userdata
		 */
        DataManager manager = ircCore.getDataManager();
		String chanName = m.getParams()[0];
		Channel chan = manager.getChannel(chanName);
		//this should never happen, as I have to join a channel bevor being able to see any PARTs
		if (chan == null) {
			chan = new Channel(chanName, ircCore);
			manager.addChannel(chan);
		}
		Nick partNick = manager.getNick(m.getNick());
		if (partNick == null) {
			partNick = new Nick(m.getPrefix(), ircCore);
			manager.addNick(partNick);
	        chan.deleteUser(partNick);
		}

		/*
		 * write user message
		 */
		ircCore.putUM("[" + chanName + "] <" + partNick.getNick() + "> left.");
		
	}

}
