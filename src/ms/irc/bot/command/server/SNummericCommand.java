package ms.irc.bot.command.server;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.command.server.numeric.NChannelInfo;
import ms.irc.bot.command.server.numeric.NChannelWhoIsOnCommand;
import ms.irc.bot.command.server.numeric.NIgnoreCommand;
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
        MessageCommand ignore = new NIgnoreCommand();
        MessageCommand chanCommand = new NChannelInfo();
        
        commands[1] = info;//Welcome Message
        commands[2] = info;//Registration Process
        commands[3] = info;//Registration Process
        commands[4] = new NRegistrationCommand();//Registration Process
        
        
        commands[251] = info;
        commands[252] = info;
        commands[253] = info;
        commands[254] = info;
        commands[255] = info;
        
        commands[332] = chanCommand;//Topic
        commands[333] = chanCommand;
        
        commands[353] = new NChannelWhoIsOnCommand();
        
        commands[366] = ignore;
        
        commands[372] = info;
        
        commands[375] = info;
        commands[376] = ignore;
                
                
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
