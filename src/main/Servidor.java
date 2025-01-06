package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.Peticio;

public class Servidor {

	public static void main(String[] args) throws IOException {
		System.err.println("SERVIDOR >>> Arranca el servidor, espera peticio");
		ServerSocket socketEscolta = null;
		try {
			socketEscolta = new ServerSocket(1234);
		} catch (IOException e) {
			System.err.println("SERVIDOR >>> Error");
			return;
		}
		while (true) {
			Socket connexio = socketEscolta.accept();
			System.err.println("SERVIDOR >>> Connexio rebuda --> Llança fil classe Peticio");
			
			Peticio p = new Peticio(connexio);
			Thread fil = new Thread(p);
			fil.start();
		}
	}

}
