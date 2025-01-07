package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The Servidor class represents the server that listens for incoming client connections,
 * assigns each client to a thread, and manages a list of connected clients.
 */
public class Servidor {
    /**
     * List of connected clients.
     */
	public static final ArrayList<Peticio> clients = new ArrayList<Peticio>();
	
    /**
     * Checks if a given username is already taken in a specific channel.
     * 
     * @param userName The username to check.
     * @param channel The channel to check in.
     * @return true if the username is already taken in the specified channel, false otherwise.
     */
	public static synchronized boolean isUserNameTaken(String userName, int channel) {
        for (Peticio p : clients) {
            if (p.getUserName() != null && p.getUserName().equals(userName) && p.getChannel() == channel) {
                return true;
            }
        }
        return false;
    }

    /**
     * The main method that starts the server, listens for incoming connections,
     * and assigns each connection to a new thread.
     * 
     * @param args Command-line arguments.
     * @throws IOException If an I/O error occurs when creating the server socket.
     */
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
			System.err.println("SERVIDOR >>> Connection received --> Creating new Thread");
			
			Peticio p = new Peticio(connexio);
			clients.add(p);
			Thread fil = new Thread(p);
			fil.start();
		}
	}

}
