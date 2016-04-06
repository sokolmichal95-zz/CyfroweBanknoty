package Bob;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.interfaces.RSAPublicKey;

import static Utils.Utils.IP;
import static Utils.Utils.SysOut;

public class Shop {

    public static void main(String[] args) {
        Socket socket = new Socket();
        // 1 Sklep odbiera z banku klucz publiczny
        while (!socket.isConnected()) {
            try {
                Thread.sleep(4);
                try {
                    socket = new Socket(IP, 6666);
                } catch (IOException e) {

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            // 1. Sklep odbiera od Banku klucz publiczny RSA
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            RSAPublicKey publicKey;
            publicKey = (RSAPublicKey) ois.readObject();
            SysOut("Received public key from bank\n");

			/*// 2. Sklep tworzy nowy Server Socket i odbiera od Alice podpisany banknot
            ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(8888);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SignedNote sigNote = (SignedNote) ois.readObject();*/
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
