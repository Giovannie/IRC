package ms.irc.bot.userdata;

import java.util.ArrayList;

import ms.irc.bot.IRCnet;

public class Channel {
	
	private ArrayList<Nick> user;
	private IRCnet ircCore;
	private String chanName;
	private String topic;
    private boolean active;
	
	public Channel(String chanName, IRCnet ircCore) {
		if (chanName == null || ircCore == null)
			throw new IllegalArgumentException("Argument must not be null.");
		this.chanName = chanName;
		this.ircCore = ircCore;
		user = new ArrayList<Nick>();
	}

	public ArrayList<Nick> getUser() {
		return user;
	}
	
	public void addUser(Nick nick) {
		user.add(nick);
	}
	
	public Nick getUser(String nick) {
		for (Nick n : user) {
			if (n.getNick().equals(nick))
				return n;
		}
		return null;
	}
	
	public boolean deleteUser(Nick partNick) {
		Nick del = getUser(partNick.getNick());
		return user.remove(del);
	}

	public String getChanName() {
		return new String(chanName);
	}

	public void setChanName(String chanName) {
		this.chanName = chanName;
	}
	
	public void msg(String message) {
		ircCore.userCommand("/msg " + chanName + " " + message);
	}

	public String getTopic() {
		return new String(topic);
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isActive() {
        return active;
    }

}
