package Bank;

import Banknote.BlindNote;
import Banknote.Note;
import Utils.BlindRSA;
import Utils.RSA;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static Utils.Utils.SysOut;
import static Utils.Utils.generateRandomInteger;

public class Bank {

    public static void main(String[] args) {

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
            //Tablica zaciemnionych banknotów
            BlindNote[] bn = new BlindNote[100];
            try {
                rsa = new RSA();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(rsa.getPublicKey());
            System.out.println("Wysłałem klucz publiczny do Alice");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //1. Bank odbiera zaciemnione banknoty od Alice.
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                bn = (BlindNote[]) ois.readObject();
            } catch (ClassNotFoundException e) {
                SysOut("NO SUCH CLASS EXISTS\n" + e.getMessage());
            }


            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //2. Bank wybiera losowo liczbę naturalną [1,100] i odsyła ją do Alice.
            int bankChoice = generateRandomInteger();
            try {
                oos.writeObject(bankChoice);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Wybór: " + bankChoice);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //3. Bank odbiera od Alice wszystkie banknoty z wyjątkiem wybranego.
            ArrayList<Note> noteList = new ArrayList<>();
            noteList = (ArrayList<Note>) ois.readObject();

            //3.1 Bank odbiera od Alice lewy i prawy sekret
            ArrayList<byte[]> rSafe;
            ArrayList<byte[]> lSafe;

            rSafe = (ArrayList<byte[]>) ois.readObject();
            lSafe = (ArrayList<byte[]>) ois.readObject();

            //3.2 Bank odbiera od Alice czynnik zaciemniający
            BlindRSA blindRSA = (BlindRSA) ois.readObject();
            SysOut("Odebrałem R od Alice i odkrywam banknoty");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // 4. Bank odkrywa zakryte banknoty
            ArrayList<Note> unblindedNotes = new ArrayList<>();
            for (int j = 0; j < bankChoice; j++) {
                Note note = new Note();
                ArrayList<byte[]> LH = new ArrayList<>();
                ArrayList<byte[]> LO = new ArrayList<>();
                ArrayList<byte[]> RH = new ArrayList<>();
                ArrayList<byte[]> RO = new ArrayList<>();

                for (int i = 0; i < 100; i++) {
                    LH.add(blindRSA.unblind(bn[j].leftHash.get(i)));
                    LO.add(blindRSA.unblind(bn[j].leftOut.get(i)));
                    RH.add(blindRSA.unblind(bn[j].rightHash.get(i)));
                    RO.add(blindRSA.unblind(bn[j].rightOut.get(i)));
                }

                note.setAmount(blindRSA.unblind(bn[j].amount));
                note.setId(blindRSA.unblind(bn[j].id));
                note.setLeftHash(LH);
                note.setLeftOut(LO);
                note.setRightHash(RH);
                note.setRightOut(RO);

                unblindedNotes.add(note);
            }

            for (int j = bankChoice + 1; j < bn.length; j++) {
                Note note = new Note();
                ArrayList<byte[]> LH = new ArrayList<>();
                ArrayList<byte[]> LO = new ArrayList<>();
                ArrayList<byte[]> RH = new ArrayList<>();
                ArrayList<byte[]> RO = new ArrayList<>();

                for (int i = 0; i < 100; i++) {
                    LH.add(blindRSA.unblind(bn[j].leftHash.get(i)));
                    LO.add(blindRSA.unblind(bn[j].leftOut.get(i)));
                    RH.add(blindRSA.unblind(bn[j].rightHash.get(i)));
                    RO.add(blindRSA.unblind(bn[j].rightOut.get(i)));
                }

                note.setAmount(blindRSA.unblind(bn[j].amount));
                note.setId(blindRSA.unblind(bn[j].id));
                note.setLeftHash(LH);
                note.setLeftOut(LO);
                note.setRightHash(RH);
                note.setRightOut(RO);

                unblindedNotes.add(note);
            }
            SysOut("Unblinded notes: \n");
            for (Note n : unblindedNotes
                    ) {
                SysOut("Amount : " + new String(n.getAmount(), StandardCharsets.UTF_8));
            }


            //4.1 Bank sprawdza zgodność banknotów
            SysOut("\n\n");
            if (NoteComparator.noteCompare(unblindedNotes, lSafe, rSafe, noteList)) {
                SysOut("\nUfam Alice i podpiszę jej banknot");
            } else {
                SysOut("\nAlice próbuje mnie oszukać");
            }

        } catch (IOException e) {
            SysOut("ERROR CONNECTING WITH BANK\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            SysOut("NO SUCH CLASS EXISTS\n" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
