package ms.irc.bot.encrypt;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 * 
 * @author Giovannie
 * @version 0.1.1a
 */
public class RSAGioPrivateKey implements RSAPrivateKey {

	private static final long serialVersionUID = -7658263843899567659L;
	private static final String algorithmName = "MS-RSA-1.0";
	private BigInteger exponent;
	private BigInteger modulus;

	/**
	 * Constructor for class RSAGioPrivateKey
	 * 
	 * @param d the private exponent for the RSA Encryption
	 * @param n the modulus for the RSA Encryption
	 */
	protected RSAGioPrivateKey(BigInteger d, BigInteger n) {
		exponent = d;
		modulus = n;
	}
	
	/**
	 * 
	 * @see java.security.Key#getAlgorithm()
	 * @return the algorithmName
	 */
	@Override
	public String getAlgorithm() {
		return algorithmName;
	}

	/**
	 * 
	 * @see java.security.Key#getEncoded()
	 * @return null
	 */
	@Override
	public byte[] getEncoded() {
		return null;
	}

	/**
	 * 
	 * @see java.security.Key#getFormat()
	 * @return null
	 */
	@Override
	public String getFormat() {
		return null;
	}

	/**
	 * 
	 * @see java.security.interfaces.RSAKey#getModulus()
	 * @return the modulus for the RSA decryption
	 */
	@Override
	public BigInteger getModulus() {
		return modulus;
	}

	/**
	 * 
	 * @see java.security.interfaces.RSAPrivateKey#getPrivateExponent()
	 * @return the private Exponent for the RSA decryption
	 */
	@Override
	public BigInteger getPrivateExponent() {
		return exponent;
	}
	
}
