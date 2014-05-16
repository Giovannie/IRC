package ms.irc.bot.command.server.numeric;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class NChannelInfo implements MessageCommand {

    @Override
    public void executeCommand(Message m, IRCnet ircCore) {
        if (m == null || ircCore == null)
            throw new NullPointerException("Got Nullpointer Argument while handling a numeric command.");
        
        if (m.getParams() == null || m.getParams().length != 2 || m.getTrailing() == null) {
            ircCore.putUM("--! Got invalid Message from Server. Message was: !--");
            ircCore.putUM(m.toString());
            return;
        }
        
        ircCore.putUM("<" + m.getPrefix() + "> [" + m.getParams()[1] + "]: " + m.getTrailing());
        
    }

}