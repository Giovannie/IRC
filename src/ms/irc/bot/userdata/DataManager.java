package ms.irc.bot.userdata;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import ms.irc.bot.IRCnet;

/**
 * Class to manage Data such as known Channels and
 * Nick names.
 * 
 * Note: this class, its package and its functionality
 * might be augmented drastically in the future, as
 * the irc Client gets a real Database and stuff.
 * 
 * @author Giovannie
 * @version 0.0.1
 */
public class DataManager {

    private ConcurrentHashMap<String, Channel> channel;
    private ConcurrentHashMap<String, Nick> nicks;
    private IRCnet ircCore;
    
    /**
     * Constructor.
     * 
     */
    public DataManager(IRCnet ircCore) {
        
        this.ircCore = ircCore;
        channel = new ConcurrentHashMap<String, Channel>();
        nicks = new ConcurrentHashMap<String, Nick>();
    }
    
    /**
     * returns a Collection of all known Channels
     * 
     * @return a Collection of all known Channels
     */
    public Collection<Channel> getChannels() {
        return channel.values();
    }
    
    /**
     * Adds a channel to the internal list of known channels.
     * 
     * @param chan a Channel
     */
    public void addChannel(Channel chan) {
        if (chan == null)
            throw new NullPointerException("Argument of type Channel was null");
        channel.put(chan.getChanName(), chan);
    }

    /**
     * Returns the Channel with the given name, if a channel
     * with such name is known to the DataManager. If not,
     * it returns null.
     * 
     * @param chanName a String
     * @return a Channel
     */
    public Channel getChannel(String chanName) {
        if (chanName == null)
            return null;
        return channel.get(chanName);
    }
    
    /**
     * Returns the Nick with the given name.
     * Note that Objects of class Nick may contain a lot
     * of meta data, and not just the nick String itself.
     * For more information see Nick.java
     * 
     * @param nick a String
     * @return a Nick (or null if there is no nick with the
     *      given name)
     */
    public Nick getNick(String nick) {
        if (nick == null)
            return null;
        return nicks.get(nick);
    }
    
    /**
     * Adds a Nick to the List of known Nicks.
     * 
     * @param nick a Nick
     */
    public void addNick(Nick nick) {
        nicks.put(nick.getNick(), nick);
    }

    /**
     * changes a nickname (normally if received a NICK command)
     * 
     * @param oldnick a String
     * @param newnick a String
     */
    public void changeNick(String oldnick, String newnick) {
        
        if (oldnick == null || newnick == null)
            throw new IllegalArgumentException("Got a null argument while trying to change a nickname.");
        
        Nick nick = getNick(oldnick);
        //check if nick existed
        if (nick == null) {
            ircCore.addLogEntry(Level.WARNING, "Tried to change nonexistent Nickname. Creating new Nick.");
            addNick(new Nick(newnick, null, null, ircCore));
            return;
        }
        
        //change nick
        nicks.remove(oldnick);
        nick.setNick(newnick);
        nicks.put(newnick, nick);
        
        return;
    }
}
