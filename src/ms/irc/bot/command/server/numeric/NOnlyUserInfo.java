package ms.irc.bot.command.server.numeric;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class NOnlyUserInfo implements MessageCommand {

    @Override
    public void executeCommand(Message m, IRCnet ircCore) {
        String message = "";
        if (m.getParams() != null && m.getParams().length > 1) {
            message = m.getParams()[1];
            for (int i = 2; i < m.getParams().length; i++) {
                message += " " + m.getParams()[i];
            }
        }
        if (m.getTrailing() != null)
            message += m.getTrailing();
        ircCore.putUM("<" + m.getPrefix() + ">: " + message);
    }

}
