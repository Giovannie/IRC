package ms.irc.bot.command.server;

import java.util.Date;

import ms.irc.bot.IRCnet;
import ms.irc.bot.command.general.MessageCommand;
import ms.irc.bot.userdata.Message;

public class SCTCPCommand implements MessageCommand {

	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		
		if (m.getTrailing().length() <= 3) {
			//ToDo: put some kind of warning
			return;
		}
		int pos = m.getTrailing().indexOf(" ");
		if (pos < 0)
			pos = m.getTrailing().length() - 1;
		String command = m.getTrailing().substring(1, pos);
		
		switch (command) {
		case "PING":
		{
			ircCore.userCommand("/notice " + m.getNick() + " " + m.getTrailing().replace("I", "O"));
			ircCore.putUM("CTCP Ping/Pong by " + m.getNick());
			break;
		}
		
		case "VERSION":
		{
			ircCore.userCommand("/notice " + m.getNick() + " " + m.getTrailing().replace("VERSION", "VERSION aicab V" + ircCore.getBuildNumber() + " on " + System.getProperty("os.name") + " JavaVersion:" + System.getProperty("java.version")));
			ircCore.putUM("CTCP Version by " + m.getNick());
			break;
		}
		
		case "TIME": 
		{
			ircCore.userCommand("/notice " + m.getNick() + " " + m.getTrailing().replace("TIME", "TIME " + new Date().toString()));
			ircCore.putUM("CTCP Time by " + m.getNick());
			break;
		}
		case "ACTION":
		{
			if (m.getParams() == null) {
				ircCore.putUM("*" + m.getNick() + m.getTrailing().substring(m.getTrailing().indexOf(" "), m.getTrailing().length() - 2) + "*");
			} else {
				ircCore.putUM("[" + m.getParams()[0] + "] *" + m.getNick() + m.getTrailing().substring(m.getTrailing().indexOf(" "), m.getTrailing().length() - 2) + "*");
			}
			
		}
		}
	}

}
