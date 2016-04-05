/*
import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import org.junit.Test;

import Banknote.Note;
import Utils.BlindRSA;
import Utils.RSA;

public class bindTest {

	@Test
	public void testBlind() throws NoSuchAlgorithmException, NoSuchProviderException {
	RSA rsa = new RSA();
	Note note = new Note();

	BlindRSA util = new BlindRSA(rsa.getPublicKey());
	BigInteger co = util.blind(note.getId());
	byte[] exe = util.unblind(co);
	assertArrayEquals(note.getId(), exe);
	System.out.println(Arrays.equals(note.getId(), exe));
		
	}

	

}
*/