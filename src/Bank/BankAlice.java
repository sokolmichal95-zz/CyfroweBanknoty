package Bank;

import Banknote.BlindNote;
import Banknote.Note;
import Banknote.SignedNote;
import Utils.BlindRSA;
import Utils.RSA;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static Utils.Utils.SysOut;
import static Utils.Utils.generateRandomInteger;

public class BankAlice {

    public static void alice() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Socket socket = serverSocket.accept()) {
            ObjectOutputStream oos;
            ObjectInputStream ois = null;
            RSA rsa = null;

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Tablica zaciemnionych banknotów
            BlindNote[] bn = new BlindNote[100];
            try {
                rsa = new RSA();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(rsa.getPublicKey());
            System.out.println("Public key was sent to Alice!");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // 1. Bank odbiera zaciemnione banknoty od Alice.
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                bn = (BlindNote[]) ois.readObject();
                SysOut("Received blinded notes from Alice");
            } catch (ClassNotFoundException e) {
                SysOut("NO SUCH CLASS EXISTS\n" + e.getMessage());
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // 2. Bank wybiera losowo liczbę naturalną [1,100] i odsyła ją do
            //////////////////////////////////////////////////////////////////////////////////////////////////////////// Alice.
            int bankChoice = generateRandomInteger();
            try {
                oos.writeObject(bankChoice);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Choice : " + bankChoice);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // 3. Bank odbiera od Alice wszystkie banknoty z wyjątkiem
            //////////////////////////////////////////////////////////////////////////////////////////////////////////// wybranego.
            ArrayList<Note> noteList = new ArrayList<>();
            noteList = (ArrayList<Note>) ois.readObject();
            SysOut("Received original notes from Alice except the chosen one");
            // 3.1 Bank odbiera od Alice lewy i prawy sekret
            ArrayList<byte[]> rSafe;
            ArrayList<byte[]> lSafe;

            rSafe = (ArrayList<byte[]>) ois.readObject();
            lSafe = (ArrayList<byte[]>) ois.readObject();

            // 3.2 Bank odbiera od Alice czynnik zaciemniający
            BlindRSA blindRSA = (BlindRSA) ois.readObject();
            SysOut("Received blinding factor\nUnblinding notes...");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // 4. Bank odkrywa zakryte banknoty
            ArrayList<Note> unblindedNotes = new ArrayList<>();

            for (int j = 0; j < bankChoice; j++) {
                byte[] amnt = blindRSA.unblind(bn[j].getAmount());
                byte[] id = blindRSA.unblind(bn[j].getId());
                byte[][] lH = new byte[100][];
                byte[][] rH = new byte[100][];
                byte[][] lO = new byte[100][];
                byte[][] rO = new byte[100][];
                byte[][] lM = new byte[100][];
                byte[][] rM = new byte[100][];

                BigInteger[] lh = bn[j].getLeftHash();
                BigInteger[] rh = bn[j].getRightHash();
                BigInteger[] lo = bn[j].getLeftOut();
                BigInteger[] ro = bn[j].getRightOut();
                BigInteger[] lm = bn[j].getLeftMystery();
                BigInteger[] rm = bn[j].getRightMystery();


                for (int i = 0; i < 100; i++) {
                    lH[i] = blindRSA.unblind(lh[i]);
                    rH[i] = blindRSA.unblind(rh[i]);
                    lO[i] = blindRSA.unblind(lo[i]);
                    rO[i] = blindRSA.unblind(ro[i]);
                    lM[i] = blindRSA.unblind(lm[i]);
                    rM[i] = blindRSA.unblind(rm[i]);
                }

                unblindedNotes.add(new Note(amnt, id, lH, rH, lO, rO, lM, rM));
            }

            for (int j = bankChoice + 1; j < bn.length; j++) {
                byte[] amnt = blindRSA.unblind(bn[j].getAmount());
                byte[] id = blindRSA.unblind(bn[j].getId());
                byte[][] lH = new byte[100][];
                byte[][] rH = new byte[100][];
                byte[][] lO = new byte[100][];
                byte[][] rO = new byte[100][];
                byte[][] lM = new byte[100][];
                byte[][] rM = new byte[100][];

                BigInteger[] lh = bn[j].getLeftHash();
                BigInteger[] rh = bn[j].getRightHash();
                BigInteger[] lo = bn[j].getLeftOut();
                BigInteger[] ro = bn[j].getRightOut();
                BigInteger[] lm = bn[j].getLeftMystery();
                BigInteger[] rm = bn[j].getRightMystery();


                for (int i = 0; i < 100; i++) {
                    lH[i] = blindRSA.unblind(lh[i]);
                    rH[i] = blindRSA.unblind(rh[i]);
                    lO[i] = blindRSA.unblind(lo[i]);
                    rO[i] = blindRSA.unblind(ro[i]);
                    lM[i] = blindRSA.unblind(lm[i]);
                    rM[i] = blindRSA.unblind(rm[i]);
                }

                unblindedNotes.add(new Note(amnt, id, lH, rH, lO, rO, lM, rM));
            }
            /*
             * SysOut("Unblinded notes : \n"); for (Note n : unblindedNotes ) {
			 * SysOut("Amount : " + new String(n.getAmount(),
			 * StandardCharsets.UTF_8)); }
			 */

            // 4.1 Bank sprawdza zgodność banknotów
            SysOut("\n\n");
            SysOut(lSafe.size() + "                   " + rSafe.size());
            if (NoteComparator.noteCompare(unblindedNotes, noteList, lSafe, rSafe)) {
                SysOut("\nI trust Alice so I'm gonna sign her note!");
                ArrayList<BigInteger> Y = new ArrayList<BigInteger>();
                for (int i = 0; i < bn.length; i++) {
                    Y.add(bn[i].getAmount().add(bn[i].getId()));
                }
                BlindRSA blindRSA2 = new BlindRSA(rsa.getPrivateKey());
                ArrayList<SignedNote> sigNotes = new ArrayList<SignedNote>();
                for (int i = 0; i < Y.size(); i++) {
                    //SysOut(Y.get(i));
                    sigNotes.add(new SignedNote(bn[i], blindRSA2.sign(Y.get(i))));
                    SysOut("Signed Note : " + sigNotes.get(i).getSignature());
                }
                oos.writeObject(sigNotes);
            } else {
                SysOut("\nAlice is trying to cheat on me!");
            }

        } catch (IOException e) {
            SysOut("ERROR CONNECTING WITH ALICE\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            SysOut("NO SUCH CLASS EXISTS\n" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
