package ms.irc.bot;

/**
 * 
 * @author Giovannie
 * @version 0.9b
 */
public class Message {
	
	private String prefix = null;
	private String command = null;
	private String params[] = null;
	private String trailing = null;
	
	/**
	 * creates a new Message out of a IRC-Protocol conform line of text.
	 * 
	 * @param line
	 */
	public Message (String line) {
		
		//check if line is not null
		if (line == null)
			throw new IllegalArgumentException("line must not be null.");
		
		//look for a prefix
		if (line.startsWith(":")) {
			int t = line.indexOf(" ");
			prefix = line.substring(1, t);
			line = line.substring(t + 1);
		}
		
		//look for the command
		int t = line.indexOf(" ");
		if (t == -1) {
			t = line.length();
		}
		command = line.substring(0, t);
		if (t + 1 < line.length())
			line = line.substring(t + 1);
		
		//look for all possible params
		int t2 = line.indexOf(":");
		if (t2 != -1) {
			trailing = line.substring(t2 + 1);
			line = line.substring(0, t2);
		}
		params = line.split(" ");
	}
	
	/**
	 * creates a new Message. prefix trailing and params might be null if not needed.
	 * 
	 * @param prefix
	 * @param command
	 * @param params
	 * @param trailing
	 */
	public Message(String prefix, String command, String[] params, String trailing) {
		this.prefix = prefix;
		this.command = command;
		this.params = params;
		this.trailing = trailing;
//		System.out.println(this.toString());
	}

	public String getPrefix() {
		return prefix;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String[] getParams() {
		return params;
	}
	
	public String getTrailing() {
		return trailing;
	}
	
	public String getNick() {
		if (prefix == null) {
			return null;
		}
		if (!prefix.contains("!")) {
			return null;
		}
		return prefix.substring(0, prefix.indexOf("!"));
	}
	
	public String getUser() {
		if (prefix == null) {
			return null;
		}
		return prefix.substring(prefix.indexOf("!") + 1, prefix.indexOf("@"));
	}
	
	public String getHost() {
		if (prefix == null) {
			return null;
		}
		return prefix.substring(prefix.indexOf("@") + 1);
	}
	
	public boolean isNumeric() {
		try {
			Integer.parseInt(command);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public int getNumeric() {
		try {
			return Integer.parseInt(command);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	@Override
	public String toString() {

		//compile the outputString to be written
		String out = "";
		//prefix
		if (prefix != null) {
			out = ":" + prefix + " ";
		}
		//command
		out = out + command;
		//params
		if (params != null) {
			for (String param : params) {
				out = out + " " + param;
			}
		}
		//trailing
		if (trailing != null) {
			out = out + " :" + trailing;
		}
		
		return out;
	}
}
