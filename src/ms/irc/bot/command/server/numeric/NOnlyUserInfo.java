package ms.irc.bot.command.server.numeric;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class NOnlyUserInfo implements MessageCommand {

    @Override
    public void executeCommand(Message m, IRCnet ircCore) {
        ircCore.putUM("<" + m.getPrefix() + ">: " + m.getTrailing());
    }

}
