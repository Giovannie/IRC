package ms.irc.bot.encrypt;

import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

/**
 * This class encrypts an OutputStream using
 * my RSAEncrypter.
 * It basically wraps any OutputStream and
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
 * TODO: No Strings within code!!! (have to fix
 *      this for the whole package)
 * 
 * @author Giovannie
 * @version 0.1.1a
 */
public class RSAEncryptedOutputStream extends OutputStream {

	private static final String algorithmName = "MS-RSA-1.0";
	private OutputStream out;
    private RSAPublicKey key;
    private int blockSize;
    private byte[] plainBuffer;
    private int plainBufferPos;
    private boolean eof;
    
	/**
     * constructs a new RSAEncryptedOutputStream.
     * This basically wraps an (plaintext) OutputStream
     * and encrypts it.
     * 
	 * @param key the (public) RSA key for the encryption
	 * @param out an OutputStream
	 * @throws KeyException if key not compliant with algorithm
	 */
	public RSAEncryptedOutputStream(RSAPublicKey key, OutputStream out) throws KeyException {
	    
        /*
         * check arguments
         */
        if (key == null || out == null)
            throw new IllegalArgumentException("Got a null Argument");
        if (!key.getAlgorithm().equals(algorithmName))
            throw new KeyException("Key is not compatible with this Algorithm.");
        
        blockSize = RSAEncrypter.getBlockSize(key);
        plainBuffer = new byte[blockSize];
        plainBufferPos = 0;
        this.key = key;
        this.out = out;
		
	}
	
	@Override
	public void close() throws IOException {
	    this.flush();
	    eof = true;
	    out.close();
	}
	
	@Override
	public void flush() throws IOException {
        try {
            out.write(RSAEncrypter.encrypt(key, new String(Arrays.copyOfRange(plainBuffer, 0, plainBufferPos))).getBytes());
            out.flush();
        } catch (KeyException e) {
            // TODO Well, shit.
        }
	}
	
	@Override
	public void write(byte[] arg0) throws IOException {
	    this.write(arg0, 0, arg0.length);
	}
	
    @Override
    public void write(byte[] arg0, int offset, int length) throws IOException {
        
        if (arg0 == null)
            throw new NullPointerException("Got null argument instead of byte array.");
        if (offset < 0 || offset + length > arg0.length)
            throw new IndexOutOfBoundsException("Offset or length point to invalid array positions.");
        
        if (eof)
            throw new IOException("Received data after stream was closed.");
        
        
        int i = 0;
        while (plainBufferPos <= plainBufferPos + --length && plainBufferPos < plainBuffer.length) {
            plainBuffer[plainBufferPos++] = arg0[offset + i++];
        }
        
        plainBufferPos = 0;
        try {
            out.write(RSAEncrypter.encrypt(key, new String(plainBuffer)).getBytes());
        } catch (KeyException e) {
            // TODO Well, shit
        }
            
        while (length > plainBuffer.length) {
            try {
                out.write(RSAEncrypter.encrypt(key,
                        new String(Arrays.copyOfRange(arg0, offset, offset + plainBuffer.length))).getBytes());
            } catch (KeyException e) {
                // TODO Well, shit
            }
            length -= plainBuffer.length;
            offset += plainBuffer.length;
        }
        
        i = 0;
        while (plainBufferPos <= plainBufferPos + --length) {
            plainBuffer[plainBufferPos++] = arg0[offset + i++];
        }
        
    }
	
	@Override
	public void write(int arg0) throws IOException {
	    
	    /*
	     * check if eof has yet been reached
	     */
	    if (eof)
	        throw new IOException("Received data after stream was closed.");
	    
	    byte b = (byte) arg0; //ignoring the upper 24 bits if int to big for byte cast
	    
	    /*
	     * if plainBuffer not full, fill it up.
	     */
        if (plainBufferPos < plainBuffer.length - 1) {
            plainBuffer[plainBufferPos++] = b;
            return;
        }
        
        /*
         * if plainBuffer is full, encrypt and write it.
         */
        try {
            plainBuffer[plainBufferPos] = b;
            plainBufferPos = 0;
            out.write(RSAEncrypter.encrypt(key, new String(plainBuffer)).getBytes());
            //TODO: should I flash the plainBuffer?
        } catch (KeyException e) {
            // TODO Well... shit
        }

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
