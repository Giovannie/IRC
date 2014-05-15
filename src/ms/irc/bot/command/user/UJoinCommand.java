package ms.irc.bot.command.user;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
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

        //write Message
        ircCore.write(new Message(null, "JOIN", m.getParams(), m.getTrailing()));
        
        //add Channel to list of known Channels
        Channel chan = ircCore.getDataManager().getChannel(m.getParams()[0]);
        if (chan == null)
            ircCore.getDataManager().addChannel(new Channel(m.getParams()[0], ircCore));
    }

}
