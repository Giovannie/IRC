package ms.irc.bot.bot;

import ms.irc.bot.IRCnet;

/**
 * 
 * This class is the mainclass of my IRC-Bot.
 * It shall grow to an easy to configure, self-learning
 * bot, which can be used f.e. as an animal-bot, simple
 * chat bot, admin-bot etc...
 * 
 * TODO: whole bot still under construction.
 * 
 * @author Giovannie
 * @version 0.0.1
 *
 */
public class BotCore {
	

    private final static String user = "aicab001";
    
	private String pwHash;
	private String seed;

    private String server = "se.quakenet.org";
    private String nick = "aicab";
    
    private IRCnet netCore;
    

	public BotCore() {
	    
	}
}
