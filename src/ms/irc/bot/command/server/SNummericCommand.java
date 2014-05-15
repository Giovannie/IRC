package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.command.server.numeric.NUnknownCommand;
import ms.irc.bot.userdata.Message;

public class SNummericCommand implements MessageCommand {

    MessageCommand[] commands;
    NUnknownCommand unknown;
    
    public SNummericCommand() {
        commands = new MessageCommand[1000];
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
		
		//TODO: copy paste to numeric command 4
		if (t == 4) {
			ircCore.register();
		}
	}

}
