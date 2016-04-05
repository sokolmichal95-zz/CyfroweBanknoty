package Banknote;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author msokol
 *
 */
public class SignedNote implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1751629949319778864L;
	
	private BlindNote blindNote;
	private BigInteger signature;
	
	public SignedNote(BlindNote n, BigInteger b){
		setBlindNote(n);
		setSignature(b);
	}
	
	public BlindNote getNote() {
		return blindNote;
	}
	public void setBlindNote(BlindNote blindNote) {
		this.blindNote = blindNote;
	}
	public BigInteger getSignature() {
		return signature;
	}
	public void setSignature(BigInteger signature) {
		this.signature = signature;
	}
	
}
