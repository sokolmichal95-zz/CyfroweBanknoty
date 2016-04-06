package Bank;

import Utils.RSA;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import static Utils.Utils.SysOut;

public class BankShop {

	public static void shop() throws InterruptedException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(6666);
		} catch (IOException e1){
			e1.printStackTrace();
		}
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
