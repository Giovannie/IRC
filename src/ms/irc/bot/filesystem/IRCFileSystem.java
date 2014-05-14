package ms.irc.bot.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class IRCFileSystem {
	
	public static Properties readProperties(String name) {
		Properties properties = new Properties();
		File file = new File(name + ".properties");
		try {
			properties.loadFromXML(new FileInputStream(file));
		} catch (IOException e) {
			return null;
		}
		return properties;
	}
	
	public static boolean writeProperties(Properties properties, String name) {
		File file = new File(name + ".properties");
		try {
			properties.storeToXML(new FileOutputStream(file), name);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}