package ms.irc.bot.command.server.numeric;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class NRegistrationCommand implements MessageCommand {

    @Override
    public void executeCommand(Message m, IRCnet ircCore) {
        if (m == null || ircCore == null) 
            throw new NullPointerException("Got a Nullpointer while processing nummeric command 004.");
        if (m.getParams() == null) {
            ircCore.putUM("--! Got invalid Message from Server !--");
            return;
        }
            
        String message = m.getParams()[0];
        
        for (int i = 1; i < m.getParams().length; i++)
            message += " " + m.getParams()[1];
        
        ircCore.putUM("<" + m.getPrefix() + ">: " + message );
        
        ircCore.register();
    }

}
