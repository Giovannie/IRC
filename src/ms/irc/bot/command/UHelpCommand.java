package ms.irc.bot.command;

import java.util.HashMap;

import ms.irc.bot.IRCnet;
import ms.irc.bot.Message;
import ms.irc.bot.filesystem.IRCFileSystem;

public class UHelpCommand implements MessageCommand {

	private HashMap<String, String> help = null;
	@Override
	public void executeCommand(Message m, IRCnet ircCore) {
		if (help == null)
			iniHelp();
		
		if (m.getParams() == null || m.getParams().length == 0) {
			ircCore.putUM(help.get("general"));
			return;
		}
		
		String command = m.getParams()[0];
		if (!command.startsWith("/"))
			command = "/" + command;
		
		String helpText = help.get(command);
		if (helpText == null || helpText.equals("")) {
			ircCore.putUM(help.get("general"));
		} else {
			ircCore.putUM(helpText);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void iniHelp() {
		help = new HashMap(IRCFileSystem.readProperties("help"));
	}

}
