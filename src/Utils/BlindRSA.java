package Utils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class BlindRSA implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Blinding factor
	 */
	public BigInteger R;
	public BigInteger N;
	public BigInteger E;
	private BigInteger D;

	/**
	 * Constructor generates blinding factor
	 *
	 * @param publicKey
	 * @throws NoSuchAlgorithmException
	 */
	public BlindRSA(RSAPublicKey publicKey) throws NoSuchAlgorithmException, NoSuchProviderException {
		R = generateBlindingFactor(publicKey.getPublicExponent(), publicKey.getModulus(), new byte[10]);
		N = publicKey.getModulus();
		E = publicKey.getPublicExponent();
	}

	/**
	 * Constructor that sets private exponent;
	 * 
	 * @param privKey
	 */
	public BlindRSA(RSAPrivateKey privKey) {
		D = privKey.getPrivateExponent();
		N = privKey.getModulus();
	}

	/**
	 * Blind <code>Note</code>
	 *
	 * @param message
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public BigInteger blind(byte[] message) throws NoSuchAlgorithmException, NoSuchProviderException {
		return (R.modPow(E, N).multiply(Utils.getBigInteger(message))).mod(N);
	}

	/**
	 * Unblind <code>Note</code>
	 *
	 * @param blindMessage
	 * @return
	 */
	public byte[] unblind(BigInteger blindMessage) {
		BigInteger M = blindMessage.multiply(R.modPow(E.negate(), N)).mod(N);
		byte[] unblinded = M.toByteArray();
		if (unblinded.length < 256) {
			byte[] zeros = new byte[256 - unblinded.length];
			unblinded = ArrayUtils.addAll(zeros, unblinded);
		}
		return unblinded;
	}

	public BigInteger sign(BigInteger message) {
		return message.modPow(D, N);
	}

	/**
	 * Generate blinding factor which is used in the blind signature protocol
	 *
	 * @param E
	 * @param N
	 * @param randomBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	private static BigInteger generateBlindingFactor(BigInteger E, BigInteger N, byte[] randomBytes)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		BigInteger r = null;
		BigInteger gcd = null;
		BigInteger one = new BigInteger("1");

		do {
			random.nextBytes(randomBytes);
			r = new BigInteger(randomBytes);
			gcd = r.gcd(N);
			// System.out.println("gcd: " + gcd);
		} while (!gcd.equals(one) || r.compareTo(N) >= 0 || r.compareTo(one) <= 0);
		// System.out.println("r: " + r + "\nN: " + N);

		return r;
	}
}
