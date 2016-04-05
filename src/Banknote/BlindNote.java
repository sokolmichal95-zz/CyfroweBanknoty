package Banknote;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to store blinded parts of each Note
 *
 * Created by msokol on 2/21/16.
 */
public class BlindNote implements Serializable{
    public BigInteger amount;
    public BigInteger id;

    public List<BigInteger> leftOut;
    public List<BigInteger> rightOut;

    public List<BigInteger> leftHash;
    public List<BigInteger> rightHash;

    public  BlindNote (){
        leftOut = new ArrayList<>();
        rightOut = new ArrayList<>();

        leftHash = new ArrayList<>();
        rightHash = new ArrayList<>();
    }
}
