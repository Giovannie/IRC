package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.DataManager;
import ms.irc.bot.userdata.Message;

public class SQuitCommand implements MessageCommand {

    @Override
    public void executeCommand(Message m, IRCnet ircCore) {


        /*
         * check params
         */
        if (m.getPrefix() == null || m.getNick() == null) {
            ircCore.putUM(m.toString());
            return;
        }
        
        DataManager manager = ircCore.getDataManager();
        manager.deleteNickFromChannels(m.getNick());
        
        /*
         * write message
         */
        ircCore.putUM("<" + m.getNick() + "> quit irc); " + m.getTrailing());
    }

}
