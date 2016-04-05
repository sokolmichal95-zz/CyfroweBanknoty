package Banknote;

import java.io.Serializable;
import java.util.ArrayList;

import static Banknote.NoteCreate.mysterySeparate;
import static Utils.Utils.*;

public class Note implements Serializable {

    private byte[] amount = new byte[10];
    private byte[] id = new byte[256];

    private byte[][] leftOut = new byte[256][100];
    private byte[][] rightOut = new byte[256][100];

    private byte[][] leftHash = new byte[256][100];
    private byte[][] rightHash = new byte[256][100];

    private byte[][] leftMystery = new byte[256][100];
    private byte[][] rightMystery = new byte[256][100];

    /**
     * <code>Note</code> class default constructor<p>
     * Construct empty <code>Note</code> instance</p>
     */
    public Note() {
    }

    /**
     * <code>Note</code> class constructor
     *
     * @param amount - amount of the Note
     * @param lSafe  - array of bytes used to make bit commitment for left XOR
     * @param rSafe  - array of bytes used to make bit commitment for right XOR
     */
    public Note(byte[] amount, byte[] lSafe, byte[] rSafe) throws Exception {
        this.amount = amount;
        id = generateRandomBytes(256, Mode.BYTE);

        mysterySeparate();
        leftMystery = NoteCreate.leftMystery;
        rightMystery = NoteCreate.rightMystery;

        ArrayList<byte[][]> lBitComm = NoteCreate.
                BitCommitment(NoteCreate.leftMystery, lSafe);
        leftHash = lBitComm.get(0);
        leftOut = lBitComm.get(1);

        ArrayList<byte[][]> rBitComm = NoteCreate.
                BitCommitment(NoteCreate.rightMystery, rSafe);
        rightHash = rBitComm.get(0);
        rightOut = rBitComm.get(1);
    }

    public byte[] getAmount() {
        return amount;
    }

    public byte[] getId() {
        return id;
    }

    public byte[] getLeftOut(int i) {
        return leftOut[i];
    }

    public byte[] getRightOut(int i) {
        return rightOut[i];
    }

    public byte[] getLeftHash(int i) {
        return leftHash[i];
    }

    public byte[] getRightHash(int i) {
        return rightHash[i];
    }
    
    public byte[] getLeftMystery(int i){
    	return leftMystery[i];
    }
    
    public byte[] getRightMystery(int i){
    	return rightMystery[i];
    }

    public void setAmount(byte[] amount) {
        this.amount = amount;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public void setLeftOut(ArrayList<byte[]> leftOut) {
        for (int i = 0; i < leftOut.size(); i++) {
            this.leftOut[i] = leftOut.get(i);
        }
    }

    public void setLeftHash(ArrayList<byte[]> leftHash) {
        for (int i = 0; i < leftHash.size(); i++) {
            this.leftHash[i] = leftHash.get(i);
        }
    }

    public void setRightOut(ArrayList<byte[]> rightOut) {
        for (int i = 0; i < rightOut.size(); i++) {
            this.rightHash[i] = rightOut.get(i);
        }
    }

    public void setRightHash(ArrayList<byte[]> rightHash) {
        for (int i = 0; i < rightHash.size(); i++) {
            this.rightHash[i] = rightHash.get(i);
        }
    }
}
