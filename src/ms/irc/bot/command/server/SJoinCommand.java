package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
import ms.irc.bot.userdata.DataManager;
import ms.irc.bot.userdata.Message;
import ms.irc.bot.userdata.Nick;

/**
 * command class for incoming JOIN commands.
 * 
 * @author Giovannie
 * @version 1.0.2
 */
public class SJoinCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
	    /*
	     * check params
	     */
		if (m.getPrefix() == null || m.getParams() == null) {
			ircCore.putUM(m.toString());
		}
		
		/*
		 * add new userdata
		 */
		DataManager manager = ircCore.getDataManager();
		String chanName = m.getParams()[0];
		Channel chan = manager.getChannel(chanName);
		//test if chan exists (if not create it)
		if (chan == null) {
			chan = new Channel(chanName, ircCore);
			manager.addChannel(chan);
		}
		chan.setActive(true);
		Nick newNick = manager.getNick(m.getNick());
		if (newNick == null) {
			newNick = new Nick(m.getPrefix(), ircCore);
			manager.addNick(newNick);
		}
		chan.addUser(newNick);
		
		/*
		 * write user message
		 */
		ircCore.putUM("[" + chanName + "] <" + newNick.getNick() + "> joined.");
	}

}
