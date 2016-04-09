package Banknote;


import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

import static Utils.Utils.*;
import static Utils.Utils.Mode.*;

public class NoteCreate {

    private static int LENGTH = 256; //długość wszystkich generowanych ciągów;
    // --Strings used to make bit commitment--//
    static byte[][] rightMystery = new byte[LENGTH][100];
    static byte[][] leftMystery = new byte[LENGTH][100];
    // ---------------------------------------//

    public static void mysterySeparate() throws Exception {
        byte[][] mystery = new byte[LENGTH][100];
        for (int i = 0; i < 100; i++) {
            byte[] I = generateRandomBytes(LENGTH, BYTE);
            mystery[i] = I;
            //System.out.println("I" + i + ": " + new String(I));
        }
        for (int i = 0; i < 100; i++) {
            rightMystery[i] = generateRandomBytes(LENGTH, BYTE);
            //System.out.println("R" + i + ": " + new String(rightMystery[i]));
        }
        for (int i = 0; i < 100; i++) {
            leftMystery[i] = getXOR(mystery[i], rightMystery[i]);
            //System.out.println("L" + i + ": " + new String(leftMystery[i]));
        }
    }

    /**
     * @param input
     * @param safe
     * @return
     * @throws Exception
     */
    public static ArrayList<byte[][]> BitCommitment(byte[][] input, byte[] safe)
            throws Exception {
        byte[][] temp = new byte[LENGTH][100];
        byte[][] tempOut = new byte[LENGTH][100];
        for (int i = 0; i < 100; i++) {
            byte[] out;
            out = generateRandomBytes(LENGTH, BYTE);
            byte[] hash = ArrayUtils.addAll(out, ArrayUtils.addAll(safe, input[i]));
            temp[i] = getHash(hash);
            tempOut[i] = out;
        }
        ArrayList<byte[][]> ret = new ArrayList<>();
        ret.add(temp); //hash pod indeksem 0
        ret.add(tempOut); //losowy ciąg wysyłany pod indeksem 1
        return ret;
    }
}
