package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.DataManager;
import ms.irc.bot.userdata.Message;
import ms.irc.bot.userdata.Nick;

public class SNickCommand implements MessageCommand {

    @Override
    public void executeCommand(Message m, IRCnet ircCore) {

        /*
         * check params
         */
        if (m.getPrefix() == null || m.getNick() == null) {
            ircCore.putUM(m.toString());
        }
        
        DataManager manager = ircCore.getDataManager();
        manager.changeNick(m.getNick(), m.getTrailing());
        
        /*
         * write message
         */
        ircCore.putUM(m.getNick() + " changed his nick to: " + m.getTrailing());
    }

}
