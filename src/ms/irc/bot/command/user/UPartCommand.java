package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
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
		
		ircCore.write(new Message(null, "PART", params, null));
		
		Channel chan = ircCore.getDataManager().getChannel(m.getParams()[0]);
		if (chan == null)
            ircCore.getDataManager().addChannel(new Channel(m.getParams()[0], ircCore));
		
		chan.setActive(false);
	}

}
