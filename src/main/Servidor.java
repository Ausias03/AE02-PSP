package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
	private static ArrayList<Peticio> clients = new ArrayList<Peticio>();
	
	public static synchronized boolean isUserNameTaken(String userName) {
        for (Peticio p : clients) {
            if (p.getUserName() != null && p.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }
	
	public static synchronized void removeClient(int channel, String userName) {
		for (Peticio p : clients) {
            if (p.getChannel() == channel && p.getUserName().equals(userName)) {
                clients.remove(p);
            }
        }
	}

	public static void main(String[] args) throws IOException {
		System.err.println("SERVIDOR >>> Server launched, waiting for petition");
		ServerSocket socketEscolta = null;
		try {
			socketEscolta = new ServerSocket(1234);
		} catch (IOException e) {
			System.err.println("SERVIDOR >>> Error");
			return;
		}
		while (true) {
			Socket connexio = socketEscolta.accept();
			System.err.println("SERVIDOR >>> Connection receivedd --> Creating new Thread");
			
			Peticio p = new Peticio(connexio);
			clients.add(p);
			Thread fil = new Thread(p);
			fil.start();
		}
	}

}
