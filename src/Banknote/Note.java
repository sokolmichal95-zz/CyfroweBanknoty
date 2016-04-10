package Banknote;

import java.io.Serializable;
import java.util.ArrayList;

import static Banknote.NoteCreate.mysterySeparate;
import static Utils.Utils.RandomBytes;

public class Note implements Serializable {

    private final byte[] amount;
    private final byte[] id;

    private final byte[][] leftOut;
    private final byte[][] rightOut;

    private final byte[][] leftHash;
    private final byte[][] rightHash;

    private final byte[][] leftMystery;
    private final byte[][] rightMystery;

    /**
     * <code>Note</code> class default constructor<p>
     * Construct empty <code>Note</code> instance</p>
     */
    public Note() {
        amount = new byte[0];
        id = new byte[0];
        leftOut = new byte[0][];
        rightOut = new byte[0][];
        leftHash = new byte[0][];
        rightHash = new byte[0][];
        leftMystery = new byte[0][];
        rightMystery = new byte[0][];
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
        this.id = RandomBytes(256);

        mysterySeparate();
        leftMystery = NoteCreate.leftMystery;
        rightMystery = NoteCreate.rightMystery;

        ArrayList<byte[][]> lBitComm = NoteCreate.
                BitCommitment(leftMystery, lSafe);
        leftHash = lBitComm.get(0);
        leftOut = lBitComm.get(1);

        ArrayList<byte[][]> rBitComm = NoteCreate.
                BitCommitment(rightMystery, rSafe);
        rightHash = rBitComm.get(0);
        rightOut = rBitComm.get(1);
    }

    /**
     * Constructor used to make original note from blinded note
     *
     * @param amount
     * @param id
     * @param leftOut
     * @param rightOut
     * @param leftHash
     * @param rightHash
     */
    public Note(byte[] amount, byte[] id, byte[][] leftOut, byte[][] rightOut, byte[][] leftHash, byte[][] rightHash,
                byte[][] leftMystery, byte[][] rightMystery) {

        this.amount = amount;
        this.id = id;
        this.leftOut = leftOut;
        this.rightOut = rightOut;
        this.leftHash = leftHash;
        this.rightHash = rightHash;

        this.leftMystery = leftMystery;
        this.rightMystery = rightMystery;
    }

    public byte[] getAmount() {
        return amount;
    }

    public byte[] getId() {
        return id;
    }

    public byte[][] getLeftOut() {
        return leftOut;
    }

    public byte[][] getRightOut() {
        return rightOut;
    }

    public byte[][] getLeftHash() {
        return leftHash;
    }

    public byte[][] getRightHash() {
        return rightHash;
    }

    public byte[][] getLeftMystery() {
        return leftMystery;
    }

    public byte[][] getRightMystery() {
        return rightMystery;
    }
}
