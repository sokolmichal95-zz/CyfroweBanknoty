package Banknote;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Class to store blinded parts of each Note
 *
 * Created by msokol on 2/21/16.
 */
public class BlindNote implements Serializable{
    private final BigInteger amount;
    private final BigInteger id;

    private final BigInteger[] leftOut;
    private final BigInteger[] rightOut;

    private final BigInteger[] leftHash;
    private final BigInteger[] rightHash;

    private final BigInteger[] leftMystery;
    private final BigInteger[] rightMystery;

    public  BlindNote (
            BigInteger amount,
            BigInteger id,
            BigInteger[] leftOut,
            BigInteger[] rightOut,
            BigInteger[] leftHash,
            BigInteger[] rightHash,
            BigInteger[] leftMystery,
            BigInteger[] rightMystery){

        this.amount = amount;
        this.id = id;
        this.leftOut = leftOut;
        this.rightOut = rightOut;
        this.leftHash = leftHash;
        this.rightHash = rightHash;
        this.leftMystery = leftMystery;
        this.rightMystery = rightMystery;

    }

    public BigInteger getAmount() {
        return amount;
    }

    public BigInteger getId() {
        return id;
    }

    public BigInteger[] getLeftOut() {
        return leftOut;
    }

    public BigInteger[] getRightOut() {
        return rightOut;
    }

    public BigInteger[] getLeftHash() {
        return leftHash;
    }

    public BigInteger[] getRightHash() {
        return rightHash;
    }

    public BigInteger[] getLeftMystery() {
        return leftMystery;
    }

    public BigInteger[] getRightMystery() {
        return rightMystery;
    }
}
