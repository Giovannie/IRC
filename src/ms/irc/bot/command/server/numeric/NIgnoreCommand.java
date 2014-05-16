package ms.irc.bot.command.server.numeric;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class NIgnoreCommand implements MessageCommand {

    @Override
    public void executeCommand(Message m, IRCnet ircCore) {
        String enabled = ircCore.getConfigEntry("numIgnore");
        if (enabled != null && enabled.equals("false"))
            ircCore.putUM("PING/PONG");
    }

}
