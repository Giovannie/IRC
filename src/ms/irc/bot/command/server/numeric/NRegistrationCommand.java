package ms.irc.bot.command.server.numeric;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class NRegistrationCommand implements MessageCommand {

    @Override
    public void executeCommand(Message m, IRCnet ircCore) {
        if (m == null || ircCore == null) 
            throw new NullPointerException("Got a Nullpointer while processing nummeric command 004.");
        if (m.getParams() == null || m.getParams().length < 2) {
            ircCore.putUM("--! Got invalid Message from Server. Message was: !--");
            ircCore.putUM(m.toString());
            return;
        }
            
        String message = m.getParams()[1];
        for (int i = 2; i < m.getParams().length; i++)
            message += " " + m.getParams()[i];
        
        ircCore.putUM("<" + m.getPrefix() + ">: " + message );
        
        ircCore.register();
    }

}
