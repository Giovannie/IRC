package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
import ms.irc.bot.userdata.DataManager;
import ms.irc.bot.userdata.Message;

public class UJoinCommand implements MessageCommand {

    @Override
    public void executeCommand(Message m, IRCnet ircCore) {
        
        //check params
        String[] params = m.getParams();
        if (params == null || params.length < 1 || params.length > 2) {
            ircCore.putUM("Error. Got either to many or to few Arguments.");
            return;
        }
        
        //add Channel to list of known Channels
        DataManager manager = ircCore.getDataManager();
        Channel chan = manager.getChannel(m.getParams()[0]);
        if (chan == null)
            manager.addChannel(new Channel(m.getParams()[0], ircCore));

        //write Message
        ircCore.write(new Message(null, "JOIN", m.getParams(), m.getTrailing()));
    }

}
