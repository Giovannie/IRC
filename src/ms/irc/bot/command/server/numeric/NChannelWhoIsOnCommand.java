package ms.irc.bot.command.server.numeric;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Channel;
import ms.irc.bot.userdata.DataManager;
import ms.irc.bot.userdata.Message;
import ms.irc.bot.userdata.Nick;

public class NChannelWhoIsOnCommand implements MessageCommand {

    @Override
    public void executeCommand(Message m, IRCnet ircCore) {
        if (m == null || ircCore == null)
            throw new NullPointerException("Got Nullpointer Argument while handling a numeric command.");

        if (m.getParams() == null || m.getParams().length != 3 || m.getTrailing() == null) {
            ircCore.putUM("--! Got invalid Message from Server. Message was: !--");
            ircCore.putUM(m.toString());
            return;
        }
        
        DataManager manager = ircCore.getDataManager();
        Channel chan = manager.getChannel(m.getParams()[2]);
        if (chan == null) {
            chan = new Channel(m.getParams()[2], ircCore);
            manager.addChannel(chan);
        }

        String[] names = m.getTrailing().split(" ");
        for (String name : names) {
            char firstChar = name.charAt(0);
            if (firstChar == '@' || firstChar == '+')//TODO implement modes...
                name = name.substring(1);
            
            Nick nick = manager.getNick(name);
            if (nick == null) {
                nick = new Nick(name, null, null, ircCore);
                manager.addNick(nick);
            }
            
            chan.addUser(nick);
        }
        
        ircCore.putUM("online on <" + chan.getChanName() + ">: " + m.getTrailing());
    }

}
