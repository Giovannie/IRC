package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
import ms.irc.bot.userdata.DataManager;
import ms.irc.bot.userdata.Message;

/**
 * implementation of command /part <channel>
 * 
 * @author Giovannie
 * @version 1.0b
 */
public class UPartCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
	    
		String[] params = m.getParams();
		if (params == null || params.length != 1) {
			ircCore.putUM("Error. Got either to many or to few Arguments.");
			return;
		}
        
        /*
         * adding/updating userdata
         */
        DataManager manager = ircCore.getDataManager();
        Channel chan = manager.getChannel(m.getParams()[0]);
        if (chan == null) {
            chan = new Channel(m.getParams()[0], ircCore);
            manager.addChannel(chan);
        }
        chan.setActive(false);
		
        /*
         * writing message
         */
		ircCore.write(new Message(null, "PART", params, null));
	}

}
