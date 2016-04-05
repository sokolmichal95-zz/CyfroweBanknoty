package Test;

import Utils.Utils;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * Created by msokol on 2/21/16.
 */
public class Test {
    public static void main(String args[]){
        Scanner s = new Scanner(System.in);
        byte[] b = s.nextLine().getBytes();

        BigInteger bi = new BigInteger(b);
        Utils.SysOut(bi);
        byte[] ret = bi.toByteArray();
        System.out.println(new String(ret));
        s.close();
    }
}
