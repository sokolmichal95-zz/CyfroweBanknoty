package Utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Utils {

	public final static String IP = "150.254.79.71";
	
    public static enum Mode {
        ALPHA, ALPHANUMERIC, NUMERIC, BYTE
    }

    /**
     * Exclusive OR that takes two characters and tests if they are either '0'
     * or '1' and return XORed value.
     *
     * @param c1
     * @param c2
     * @return
     */
    private static char charXOR(byte c1, byte c2) {
        char ret = 0;
        if ((c1 == 49 && c2 == 48) || (c1 == 48 && c2 == 49)) {
            ret = 49;
        } else if ((c1 == 49 && c2 == 49) || (c1 == 48 && c2 == 48)) {
            ret = 48;
        } else {
            System.out.println("///////////////////////");
            System.out.println("|Unsupported character|");
            System.out.println("///////////////////////");
        }
        return ret;
    }

    /**
     * Generate random long number
     *
     * @return
     */
    public static long generateRandomLong() {
        long range = 1234567L;
        Random rand = new Random();
        long number = (long) (rand.nextDouble() * range);
        return number;
    }

    /**
     * Generate random integer number
     *
     * @return
     */
    public static int generateRandomInteger() {
        int range = 100;
        Random rand = new Random();
        int number = (int) (rand.nextDouble() * range);
        return number;
    }

    /**
     * Generate random BigInteger
     *
     * @param n
     * @return
     */
    public static BigInteger generateRandomBigInteger(BigInteger n) {
        BigInteger r;
        do {
            r = new BigInteger(n.bitLength(), new Random());
        } while (r.compareTo(n) >= 0);
        return r;
    }

    /**
     * Get BigInteger value of any object. The value is get from the byte array
     * of the object.
     *
     * @param b
     * @return
     */
    public static BigInteger getBigInteger(byte[] b) {
        return new BigInteger(b);
    }

    /**
     * Get <code>Object</code> from <code>BigInteger</code>. The object is get
     * from the byte array get from big integer value.
     *
     * @param b
     * @return
     */
    public static byte[] getByteArray(BigInteger b) {
        return b.toByteArray();
    }

    /**
     * Generate random string of either letters, numbers, bytes
     *
     * @param length
     * @param mode
     * @return
     */
    public static String generateRandomString(int length, Mode mode) {

        StringBuilder buffer = new StringBuilder();
        String characters = "";

        switch (mode) {

            case ALPHA:
                characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                break;

            case ALPHANUMERIC:
                characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                break;

            case NUMERIC:
                characters = "1234567890";
                break;

            case BYTE:
                characters = "10";
                break;
        }

        int charactersLength = characters.length();

        for (int i = 0; i < length; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }
        return buffer.toString();
    }

    /**
     * Generate random string of either letters, numbers, bytes returned as a <code>byte</code> array
     *
     * @param length
     * @param mode
     * @return
     */
    public static byte[] generateRandomBytes(int length, Mode mode) {

        StringBuilder buffer = new StringBuilder();
        String characters = "";

        switch (mode) {

            case ALPHA:
                characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                break;

            case ALPHANUMERIC:
                characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                break;

            case NUMERIC:
                characters = "1234567890";
                break;

            case BYTE:
                characters = "10";
                break;
        }

        int charactersLength = characters.length();

        for (int i = 0; i < length; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }
        return buffer.toString().getBytes();
    }

    /**
     * Exclusive OR performed on two Strings.
     *
     * @param s1
     * @param s2
     * @return
     */
    public static byte[] getXOR(byte[] s1, byte[] s2) {
        byte[] result = new byte[s1.length];
    	for(int i =0 ; i< s1.length;i++){
        	result[i] = (byte) (s1[i]^s2[i]);
        }
        return result;
    }

    /**
     * Get SHA-256 hash value of String object.
     *
     * @param input
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getHash(String input) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes("UTF-8"));
        byte[] digest = md.digest();
        String output = Base64.encode(digest);
        return output;
    }
    private static File log = new File("log.txt");
    private static PrintWriter PrintWriterMaker(){
    	try {
			return new PrintWriter(log);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    private static PrintWriter out = PrintWriterMaker();
    public static void SysOut(Object in)  {
       
        
        out.println(in);
        
        out.flush();
        System.out.println(in);
        
    }
    public static String ArrayBytesToString(byte[] a){
    	StringBuffer string = new StringBuffer();  
    	for(byte b:a){
    		string.append(Integer.toBinaryString(b));
    	}
    	return string.toString();
    }
}
