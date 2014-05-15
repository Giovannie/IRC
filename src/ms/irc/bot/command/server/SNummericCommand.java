package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.command.server.numeric.NOnlyUserInfo;
import ms.irc.bot.command.server.numeric.NRegistrationCommand;
import ms.irc.bot.command.server.numeric.NUnknownCommand;
import ms.irc.bot.userdata.Message;

public class SNummericCommand implements MessageCommand {

    MessageCommand[] commands;
    NUnknownCommand unknown;
    
    public SNummericCommand() {
        //TODO:initialise only partly and rest on demand for perfomance?
        commands = new MessageCommand[1000];
        MessageCommand info = new NOnlyUserInfo();
        commands[1] = info;//Welcome Message
        commands[2] = info;
        commands[3] = info;
        commands[4] = new NRegistrationCommand();
        
        unknown = new NUnknownCommand();
    }
    
	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		int t = Integer.parseInt(m.getCommand());
		
		if (t < 0 || t >= 1000 || commands[t] == null) {
		    unknown.executeCommand(m, ircCore);
		    return;
		}
		
		commands[t].executeCommand(m, ircCore);
	}

}
