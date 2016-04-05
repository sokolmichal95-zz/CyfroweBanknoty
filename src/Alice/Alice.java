package Alice;

import static Utils.Utils.SysOut;
import static Utils.Utils.generateRandomBytes;
import static Utils.Utils.Mode.BYTE;
import static Utils.Utils.IP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Banknote.BlindNote;
import Banknote.Note;
import Banknote.SignedNote;
import Utils.BlindRSA;

public class Alice {

	public static void main(String[] args) throws Exception {

		
		Socket shopSocket = new Socket(IP, 8888);
		Socket bankSocket = new Socket(IP, 4444);
		
		ObjectOutputStream oos;
		ObjectInputStream ois;
		BlindNote[] bn;
		List<byte[]> rSafe = new ArrayList<>(); // = new byte[256][100];
		List<byte[]> lSafe = new ArrayList<>(); // = new byte[256][100];
		Note[] notes;
		notes = new Note[100];
		int bankChoice;
		SignedNote sigNote;

		// Pobranie od użytkownika wartości banknotu
		SysOut("Enter note amount : ");
		Scanner scanner = new Scanner(System.in);
		byte[] amount;
		amount = scanner.nextLine().getBytes();
		SysOut("amount : " + new String(amount));

		// Tablica zaciemnionych banknotów
		bn = new BlindNote[100];

		// Ciągi wysyłane do banku w zobowiązaniu bitowym
		// losuj 100*b i 100*c, odwołuj się do kolejnych i=0,1,2,...,99 tworząc
		// Note
		for (int i = 0; i < 100; i++) {
			rSafe.add(generateRandomBytes(256, BYTE));
			lSafe.add(generateRandomBytes(256, BYTE));
		}

		sigNote = new SignedNote(new Note(amount, lSafe.get(0), rSafe.get(0)),
				new BigInteger("19191919191919191919191919191919191919"));

		// TEST
		// Note n = new Note(amount, lSafe[0], rSafe[0]);
		// n.getNote();

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Alice tworzy 100 banknotów
		for (int i = 0; i < 100; i++) {
			notes[i] = new Note(amount, lSafe.get(i), rSafe.get(i));

		}

		for (Note n : notes) {
			SysOut(new String(n.getAmount()) + "             " + new String(n.getId()));
		}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 1. Alice wykonuje żądanie banku
		try {

			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 1. Alice odbiera od Banku klucz publiczny RSA
			ois = new ObjectInputStream(bankSocket.getInputStream());
			RSAPublicKey publicKey;
			publicKey = (RSAPublicKey) ois.readObject();
			SysOut("Received public key from bank\n" + "Blinding notes...\n");

			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 2. Alice zaciemnia banknoty
			BlindRSA blindRSA = new BlindRSA(publicKey);
			for (int i = 0; i < 100; i++) {
				BlindNote b = new BlindNote();
				b.amount = blindRSA.blind(notes[i].getAmount());
				b.id = blindRSA.blind(notes[i].getId());
				for (int j = 0; j < 100; j++) {
					b.leftHash.add(blindRSA.blind(notes[i].getLeftHash(j)));
					b.leftOut.add(blindRSA.blind(notes[i].getLeftOut(j)));
					b.rightHash.add(blindRSA.blind(notes[i].getRightHash(j)));
					b.rightOut.add(blindRSA.blind(notes[i].getRightOut(j)));
				}
				bn[i] = b;
			}

			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 3. Alice wysyła zaciemnione banknoty do banku
			oos = new ObjectOutputStream(bankSocket.getOutputStream());
			oos.writeObject(bn);

			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 4. Alice odbiera polecenie od Banku i je wykonuje

			try {
				bankChoice = (int) ois.readObject();
			} catch (IOException e) {
				SysOut("Exception : " + e.getMessage());
				bankChoice = -1;
			}
			SysOut("Choice of the Bank : " + bankChoice + "\n");
			// Thread.sleep(4000);
			SysOut("Sending original notes to the bank without the chosen one...\n");
			// 4.1 Alice wysyła do banku 99 odkrytych banknotów zachowując jeden
			// wybrany przez bank;
			List<Note> notesList = new ArrayList<>();
			try {
				for (int i = 0; i < bankChoice; i++) {
					notesList.add(notes[i]);
				}
				for (int i = bankChoice + 1; i < 100; i++) {
					notesList.add(notes[i]);
				}
				oos.writeObject(notesList);

				// 4.2 Alice wysyła do banku lewy i prawy sekret
				oos.writeObject(rSafe);
				oos.writeObject(lSafe);

				// 4.3 Alice wysyła do banku czynnik zaciemniający
				oos.writeObject(blindRSA);

			} catch (IOException e) {
				SysOut("Exception : " + e.getMessage());
			}
			// 5. Alice odbiera od banku podpisany banknot
			//sigNote = (SignedNote) ois.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// PŁATNOŚĆ
		// 1. Alice wysyła do sklepu banknot
		try {
			oos = new ObjectOutputStream(shopSocket.getOutputStream());
			oos.writeObject(sigNote);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
