package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
import ms.irc.bot.userdata.DataManager;
import ms.irc.bot.userdata.Message;
import ms.irc.bot.userdata.Nick;


public class SPrivmsgCommand implements MessageCommand {

	private SCTCPCommand ctcp = new SCTCPCommand();
	
	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		
	    /*
	     * look if command is actually a ctcp command
	     */
		if (m.getTrailing() != null && m.getTrailing() != "" && m.getTrailing().charAt(0) == (char)1 ) {
			ctcp.executeCommand(m, ircCore);
			return;
		}
		

        /*
         * adding/updating userdata
         */
        DataManager manager = ircCore.getDataManager();
        Nick nick = manager.getNick(m.getNick());
        if (nick == null) {
            nick = new Nick(m.getPrefix(), ircCore);
            manager.addNick(nick);
        }
		
        /*
         * look if there is a channel named and act accordingly
         */
		if (m.getParams() == null) {
		    /*
		     * write message
		     */
			ircCore.putUM("<" + m.getNick() + ">" + m.getTrailing());
		} else {
	        /*
	         * adding/updating userdata
	         */
	        String chanName = m.getParams()[0];
	        Channel chan = manager.getChannel(chanName);
	        //this should never happen, as I have to join a channel before being able to see any PARTs
	        if (chan == null) {
	            chan = new Channel(chanName, ircCore);
	            manager.addChannel(chan);
	        }
	        chan.addUser(nick);
	        
	        /*
	         * write message
	         */
			ircCore.putUM("[" + chanName + "] <" + nick + ">" + m.getTrailing());
		}
	}

}
