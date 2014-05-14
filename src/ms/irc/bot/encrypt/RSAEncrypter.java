package ms.irc.bot.encrypt;

import java.math.BigInteger;
import java.security.KeyException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

/**
 * This class is my own implementation of
 * the RSA encryption. It is not meant to
 * work with any other implementation.
 * Other implementations need at least the same
 * Padding Scheme for making them work fine.
 * 
 * Note: Encryption and Keygeneration functions
 * may block due to use of Java.security.SecureRandom.
 * 
 * Note: This encryption might not be strong enough in
 * every case. I especially don't know if the Keygen is
 * good enough. Also if using this in a network, there
 * probably are side-channel attacks depending also on
 * the hardware it runs on.
 * 
 * TODO: solve possible charset problems
 *
 * TODO: Method to save Key (ENCRYPTED with some SECURE
 * 		symmetric encryption)
 * 
 * TODO: further Testing on multiple computers/platforms
 * 
 * TODO: Good idea for real Random seed
 * 
 * TODO: what to do with messages that are very long?
 * 		how long can a String get? how long a byte[]?
 * 		what if I want to encrypt some Gigabyte of Data?
 * 		(probably not the best idea because of the
 * 		overhead but still should be possible.
 * 
 * TODO: a "no SecurityException mode"? Maybe one really
 * 		has to use this in a Java VM without supported 
 * 		Random algorithms...
 * 
 * TODO: change Algorithm to use Chinese Reminder Theorem.
 * 
 * @author Giovannie
 * @version 0.3.3
 */
public class RSAEncrypter {
	
	/*
	 * I'd like to use 4096 but it needs an insane amount of time to
	 * calculate the prime numbers then. Feel free to use
	 * 4096 as bitLength and 1024 as rndLength if you have enough
	 * CPU Power and Time to Calculate a Key. Once you have the Key,
	 * no problem.
	 */
	private static int bitLength = 2048;
	private static int rndLength = 512;
	private static int radix = Character.MAX_RADIX;
	private static String algorithmName = "MS-RSA-1.0";
	
	/**
	 * The method to encrypt RSA messages via the public key pair.
	 * 
	 * note: may block due to Java.security.SecureRandom
	 * 
	 * @param key the Public Key for the RSA encryption
	 * @param message the message to be encrypted
	 * @return the encrypted message (in the format "block1:block2:block3...")
	 * 		with each block as a BigInteger in the form that
	 * 		BigInteger.toString(Character.MAX_RADIX) returns.
	 * @throws KeyException if key incompatible.
	 */
	public static final String encrypt(RSAPublicKey key, String message) throws KeyException {
	    /*
	     * check arguments
	     */
	    if (key == null || message == null)
	        throw new NullPointerException("Either key or message was null.");
		if (!key.getAlgorithm().equals(algorithmName))
			throw new KeyException("Key is not compatible with this Algorithm.");
		SecureRandom rnd = new SecureRandom();
		if (rnd.getAlgorithm().equals("unknown"))
			throw new SecurityException("Could not validate Random algorithm.");

        /*
         * first some Variables i will often use
         */
		BigInteger e = key.getPublicExponent();
		BigInteger n = key.getModulus();
		byte[] rndBytes = new byte[rndLength / 8];
		String encrMessage = "";
		
		
		//cut Message in bytes, calculate blocksize and number of blocks
		byte[] bytes = message.getBytes();
		int blocksize = RSAEncrypter.getBlockSize(key);
		int blocks = ((bytes.length) / blocksize) + 1;
		
		/*
		 * the encryption, first copying the bytes of the next block, than
		 * filling up with some random bytes and at least encrypting the whole lot.
		 */
		BigInteger messageInBigInt = BigInteger.ONE;
		for (int i = 0; i < blocks; i++) {
			messageInBigInt = new BigInteger(Arrays.copyOfRange(bytes, i * blocksize, (i + 1) * blocksize));
			messageInBigInt = messageInBigInt.shiftLeft(rndLength);
			rnd.nextBytes(rndBytes);
			messageInBigInt = messageInBigInt.add(new BigInteger(rndBytes));
			
			//now the real encryption:
			messageInBigInt = messageInBigInt.modPow(e, n);
			encrMessage += ":" + messageInBigInt.toString(radix);
		}
		
		return encrMessage;
	}
	
	/**
	 * The method to decrypt RSA encrypted messages via the private key pair.
	 * 
	 * Note: like it's my own padding scheme and representation it probably
	 * won't work with any other implementation.
	 * 
	 * @param key the private RSA key for the RSA decryption
	 * @param message the message which is to be decrypted (in the format "block1:block2:block3...")
	 * @return the decrypted message.
	 * @throws KeyException if key incompatible with this algorithm.
	 */
	public static final String decrypt(RSAPrivateKey key, String message) throws KeyException {
		
	    /*
	     * check arguments
	     */
        if (key == null || message == null)
            throw new NullPointerException("Either key or message was null.");
		if (!key.getAlgorithm().equals(algorithmName))
			throw new KeyException("Key is not compatible with this Algorithm.");

        /*
         * first some Variables i will often use
         */
		BigInteger d = key.getPrivateExponent();
		BigInteger n = key.getModulus();
		String[] messageS = message.split(":");
		BigInteger messageInBigInt = BigInteger.ONE;
		String decrMessage = "";
		
		/*
		 * decrypting one block after another and cut away the added random numbers:
		 */
		for (String block : messageS) {
			if (block.equals(""))
				continue;
			messageInBigInt = new BigInteger(block, radix);
			messageInBigInt = messageInBigInt.modPow(d, n);
			messageInBigInt = messageInBigInt.shiftRight(rndLength);
			messageInBigInt = messageInBigInt.add(BigInteger.ONE);//if you read this comment and you don't
					//know why I have to add 1: I have no fucking idea but it works!
			decrMessage += new String(messageInBigInt.toByteArray());
		}
		return decrMessage;
	}
	
	/**
	 * This method creates a new private/public key pair which
	 * can be used for RSA encryption and decryption.
	 * For getting the private/public Key please use the
	 * methods extractPublicKey/extractPrivateKey
	 * 
	 * TODO: not sure if prime1 and prime2 randomly enough.
	 * How does Java.security.SecureRandom work? No Backdoors for the
	 * NSA? doubt it.
	 * 
	 * TODO: "probable" prime problem not solved yet. If key does
	 * not work one of the primes wasn't prime enough^^
	 * 
	 * note: may block due to Java.security.SecureRandom
	 * 
	 * @return an Array containing both public and private 
	 * RSA key pairs in the format {d,n,e} with (e, n) is
	 * the public, (d, n) the private Key pair.
	 */
	public static final KeyPair genKey() {
	    /* 
	     * TODO: default seeding should be "okay" better would be true
	     * randomness which might be obtained f.e. through hashing of
	     * random user interaction.
	     */
		SecureRandom rnd = new SecureRandom();
		
		if (rnd.getAlgorithm().equals("unknown"))
			throw new SecurityException("Could not validate Random algorithm.");
		
		//get 2 different prime numbers...
		BigInteger prime1 = BigInteger.probablePrime(bitLength, rnd);
		BigInteger prime2 = BigInteger.probablePrime(bitLength, rnd);
		//calculate the private and public keys.
		BigInteger n = prime1.multiply(prime2);
		BigInteger e = new BigInteger("65537");
		if (e.equals(prime1) || e.equals(prime2))
			return genKey();//not nice but: if per chance e is a divisor of n we have a real problem.
		BigInteger eul = (prime1.subtract(BigInteger.ONE).multiply(prime2.subtract(BigInteger.ONE)));
		BigInteger d = e.modInverse(eul);
		
		/*
		 * save results to a public/private KeyPair.
		 */
		KeyPair result = new KeyPair(new RSAGioPublicKey(e, n), new RSAGioPrivateKey(d, n));
		return result;
	}
	
	/**
	 * returns the BlockSize this key will produce.
	 *
	 * TODO: any of my Keys should produce the same blocksize.
	 * 		(for security reasons) 
	 *
	 * @return the Blocksize
	 */
	protected static int getBlockSize(RSAKey key) {
        /*
         * check argument
         */
        if (key == null)
            throw new NullPointerException("Either key or message was null.");
        //TODO: check if Key matches this algorithm
        
		return ((key.getModulus().bitLength() - (8 - (key.getModulus().bitLength() % 8))) - rndLength) / 8;
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