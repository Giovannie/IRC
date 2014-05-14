package ms.irc.bot.encrypt;

import java.security.KeyException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

/**
 * This class stores public Keys and one
 * private/public KeyPair
 * 
 * @author Giovannie
 * @version 0.1.1a
 */
public class RSAKeyStorage {

	private static final String algorithmName = "MS-RSA-1.0";
	private KeyPair key;
	private HashMap<String, RSAPublicKey> keys;
	
	/**
	 * creates a new RSAKeyStorage, see createNewStorage()
	 * 
	 * @param key a private/public Keypair
	 * @throws KeyException if key incompatible with this algorithm.
	 */
	protected RSAKeyStorage(KeyPair key) throws KeyException {
		if (!key.getPrivate().getAlgorithm().equals(algorithmName) || 
				!key.getPublic().getAlgorithm().equals(algorithmName) )
			throw new KeyException("Key is not compatible with this Algorithm.");
		this.key = key;
		keys = new HashMap<String, RSAPublicKey>();
		keys.put("this", (RSAPublicKey)key.getPublic());
	}
	
	/**
	 * Factory function to create new KeyStorages.
	 * 
	 * @return a new RSAKeyStorage
	 * @throws KeyException if key incompatible with this algorithm.
	 */
	public static final RSAKeyStorage createNewStorage() throws KeyException {
		return new RSAKeyStorage(RSAEncrypter.genKey());
	}
	
	/**
	 * 
	 * 
	 * @param idString an identification String that will be linked to the key
	 * @param key a public RSA key
	 * @throws KeyException if key incompatible with this algorithm.
	 */
	public void addPublicKey(String idString, RSAPublicKey key) throws KeyException {
		if (!key.getAlgorithm().equals(algorithmName))
			throw new KeyException("Key is not compatible with this Algorithm.");
	}
	
	/**
	 * returns the public key linked to the specified idString.
	 * 
	 * @param idString an identification String
	 * @return a public RSA key or null if no key linked to this String
	 */
	public RSAPublicKey getPublicByID(String idString) {
		return keys.get(idString);
	}
	
	/**
	 * returns a private RSA key which can be used to
	 * decrypt messages that got encrypted with the
	 * public RSA key returned by this.getPublic()
	 * 
	 * @return a private RSA key
	 */
	public RSAPrivateKey getPrivate() {
		return (RSAPrivateKey)key.getPrivate();
	}
	
	/**
	 * returns a public RSA key. Messages encrypted with
	 * this key can be decrypted with the key obtained by
	 * this.getPrivate().
	 * 
	 * Note: result should be the same as by 
	 * 		this.getPublicByID("this")
	 * 
	 * @return a public RSA key
	 */
	public RSAPublicKey getPublic() {
		return (RSAPublicKey)key.getPublic();
	}
	
	/**
	 * returns a String to identify the used encryption,
	 * in this case "MS-RSA-1.0"
	 *
	 * @return the algorithm name.
	 */
	public static String getAlgrorithm() {
		return algorithmName;
	}
}
