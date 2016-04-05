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
	
	private Note note;
	private BigInteger signature;
	
	public SignedNote(Note n, BigInteger b){
		setNote(n);
		setSignature(b);
	}
	
	public Note getNote() {
		return note;
	}
	public void setNote(Note note) {
		this.note = note;
	}
	public BigInteger getSignature() {
		return signature;
	}
	public void setSignature(BigInteger signature) {
		this.signature = signature;
	}
	
}
