package ms.irc.bot;

/**
 * 
 * A class to model an IRC User.
 * Note: all getter return copies of the real values.
 * 
 * @author Giovannie
 * @version 0.1.1
 */
public class Nick {
	
	private String nick;
	private String user;
	private String host;
	private IRCnet ircCore;
	
	public Nick(String prefix, IRCnet ircCore) {
		
		if (prefix == null || ircCore == null)
			throw new IllegalArgumentException("Argument was a nullpointer.");
		
		//Host
		int t;
		if ((t = prefix.lastIndexOf("@")) >= 0 && (t +1) < prefix.length()) {
			host = prefix.substring(t + 1);
			prefix = prefix.substring(0, t);
		}
		
		//User
		if ((t = prefix.indexOf("!")) >= 0 && (t + 1) < prefix.length()) {
			user = prefix.substring(t+1);
			prefix = prefix.substring(0, t);
		}
		
		//Nick (/servername)
		nick = prefix;
		this.ircCore = ircCore;
		
	}

	public Nick(String nick, String user, String host, IRCnet ircCore) {
		if (nick == null || ircCore == null) {
			throw new IllegalArgumentException("Nick and ircCore must not be null.");
		}
		
		this.nick = nick;
		this.user = user;
		this.host = host;
		this.ircCore = ircCore;
	}
	
	public String getNick() {
		return new String(nick);
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getUser() {
		return new String(user);
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getHost() {
		return new String(host);
	}
	public void setHost(String host) {
		this.host = host;
	}

	public void msg(String message) {
		ircCore.userCommand("/msg " + nick + " " + message);
	}
	
}
