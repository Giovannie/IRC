package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
import ms.irc.bot.userdata.DataManager;
import ms.irc.bot.userdata.Message;
import ms.irc.bot.userdata.Nick;

public class SNoticeCommand implements MessageCommand {
	
	@Override
	public void executeCommand(Message m, IRCnet ircCore) {

	    /*
	     * TODO: look if message is a ctcp response
	     */
	    
	    /*
	     * catch nick being null (might occur on server messages
	     */
	    if (m.getNick() == null) {
	        if (m.getPrefix() != null) {
	            ircCore.putUM("<" + m.getPrefix() + " flüstert>: " + m.getTrailing());
	        } else {
                ircCore.putUM("<Server>: " + m.getTrailing());
	        }
	        return;
	    }

        /*
         * adding/updating userdata
         */
        DataManager manager = ircCore.getDataManager();
        Nick nick = manager.getNick(m.getNick());
        if (nick == null) {
            nick = new Nick(m.getNick(), ircCore);
            manager.addNick(nick);
        }

        /*
         * look if there is a channel named and act accordingly
         */
		if (m.getParams() == null) {
            /*
             * write message
             */
			ircCore.putUM("<" + m.getNick() + " flüstert>: " + m.getTrailing());
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
			ircCore.putUM("[" + m.getParams()[0] + "] <" + m.getNick() + " flüstert>: " + m.getTrailing());
		}
	}

}
