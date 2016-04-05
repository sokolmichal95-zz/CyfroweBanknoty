package Bank;

import static Utils.Utils.SysOut;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import Utils.RSA;

public class BankShop {

	public static void shop() throws InterruptedException {
		ServerSocket serverSocket = null;
		// 5 Bank obsługuje sklep
		Thread.sleep(5000);
		try (Socket socket = serverSocket.accept()) {
			ObjectOutputStream oos;
			ObjectInputStream ois = null;
			RSA rsa = null;
			try {
				rsa = new RSA();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(rsa.getPublicKey());
			System.out.println("Public key was sent to Shop!");

		} catch (IOException e) {
			SysOut("ERROR CONNECTING WITH SHOP\n" + e.getMessage());
		}
	}
}
