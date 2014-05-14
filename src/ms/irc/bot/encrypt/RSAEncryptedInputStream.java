package ms.irc.bot.encrypt;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyException;
import java.security.interfaces.RSAPublicKey;

/**
 * This class encrypts an InputStream using
 * my RSAEncrypter.
 * It basically wraps any InputStream and
 * encrypts it.
 * 
 * Note: As the Encryption works blockwise this
 *      class also buffers the input.
 *      If reading\writing linewise or similar
 *      encrypt each line as one message...
 * 
 * For further details on the Encryption method
 * see RSAEncrypter.java
 * 
 * TODO: prooved correct, not testet at all.
 * 
 * TODO: most important are EncryptedOutpudStream 
 *      and DecryptedInputStream. Implement those.
 * 
 * @author Giovannie
 * @version 0.1.3
 */
public class RSAEncryptedInputStream extends InputStream {

	private static final String algorithmName = "MS-RSA-1.0";
	private InputStream in;
	private RSAPublicKey key;
	private int blockSize;
	private int estimateBlockLength;
	private byte[] plainBuffer;
	private byte[] cryptoBuffer;
	private int cryptoBufferPos;
	private boolean eof;

	/**
	 * constructs a new RSAEncryptedInputStream.
	 * This basically wraps an (plaintext) InputStream
	 * and encrypts it. (what ever the reason might be
	 * to encrypt an input stream).
	 * 
     * @param key the (public) RSA key for the encryption
     * @param in an InputStream
     * @throws KeyException if key not compliant with algorithm
	 */
	RSAEncryptedInputStream(RSAPublicKey key, InputStream in) throws KeyException {
	    
	    /*
	     * check arguments
	     */
		if (key == null || in == null)
			throw new IllegalArgumentException("Got a null Argument");
		if (!key.getAlgorithm().equals(algorithmName))
			throw new KeyException("Key is not compatible with this Algorithm.");
		
		/*
		 * initialize attributes of this class
		 */
		blockSize = RSAEncrypter.getBlockSize(key);
		estimateBlockLength = key.getModulus().toString(Character.MAX_RADIX).length();
		cryptoBuffer = new byte[0];
		plainBuffer = new byte[blockSize];
		cryptoBufferPos = 0;
		eof = false;
		this.key = key;
		this.in = in;
	}
	
	/**
	 * calcutlates the estimated amount of available bytes
	 * 
	 * @return the estimated amount of available bytes
	 */
	public int available() throws IOException {
		int availableBlocks = in.available() / blockSize;
		return availableBlocks * estimateBlockLength;
	}
	
	@Override
	public int read() throws IOException {
		
		/*
		 * if there is still encrypted data in the
		 * crypto buffer, return those.
		 */
		if (cryptoBufferPos < cryptoBuffer.length)
			return cryptoBuffer[cryptoBufferPos++];
		

        /*
         * check if end of file
         */
        if (eof)
            return -1;
		
		/*
		 * read data to the plaintext buffer
		 */
		int amountRead = in.read(plainBuffer);
		
		/*
		 * if end of file reached (end of stream),
		 * set eof to true.
		 */
		if (amountRead == -1 || plainBuffer[amountRead - 1] == -1) {
            eof = true;
		}
		    
		try {
		    /*
		     * here the real encryption takes place.
		     */
			cryptoBuffer = RSAEncrypter.encrypt(key, new String(plainBuffer, 0, amountRead)).getBytes();
			cryptoBufferPos = 1;
		} catch (KeyException e) {
			//Well... Mega shit...
		}
		
		return cryptoBuffer[0];
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	
	/**
	 * see InputStream for how to use this method.
	 * 
	 * TODO: Should be possible to speed this up by really
	 *     implementing this method, instead of calling read()
	 *     many times.
	 * 
	 * @return the amount of data read to the byte array.
	 */
	@Override
	public int read(byte[] b, int off, int length) throws IOException {
	    
	    /*
	     * check if end of file
	     */
		if (eof)
			return -1;

		/*
		 * check arguments
		 */
		if (b == null)
			throw new NullPointerException("byte array argument was null");
		if (off < 0 || length > b.length - off || length < 0)
			throw new IndexOutOfBoundsException("offset or length were corresponding to a position outside the array bounds.");
		
		/*
		 * uses this.read() to read the specified amount of data into the array.
		 */
		for (int i = 0; i < length; i++) {
			b[off + i] = (byte)this.read();
			if (b[off + i] == -1) {
				eof = true;
				return i;
			}
		}
		return length;
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
