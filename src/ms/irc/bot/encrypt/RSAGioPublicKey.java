package ms.irc.bot.encrypt;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

/**
 * 
 * @author Giovannie
 * @version 0.1.1a
 */
public class RSAGioPublicKey implements RSAPublicKey {

	private static final long serialVersionUID = -7658263843899567659L;
	private static final String algorithmName = "MS-RSA-1.0";
	private BigInteger exponent;
	private BigInteger modulus;

	/**
	 * Constructor for class RSAGioPublicKey
	 * 
	 * @param d the private exponent for the RSA Encryption
	 * @param n the modulus for the RSA Encryption
	 */
	protected RSAGioPublicKey(BigInteger e, BigInteger n) {
		exponent = e;
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
	 * @return the modulus for the RSA encryption
	 */
	@Override
	public BigInteger getModulus() {
		return modulus;
	}

	/**
	 * 
	 * @see java.security.interfaces.RSAPublicKey#getPublicExponent()
	 * @return the public Exponent for the RSA encryption
	 */
	@Override
	public BigInteger getPublicExponent() {
		return exponent;
	}

}