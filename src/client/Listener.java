package client;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Listener class implements the Runnable interface to continuously listen for
 * and print incoming messages from the server.
 */
public class Listener implements Runnable {
    /**
     * BufferedReader used to read messages from the server.
     */
	private BufferedReader br;

    /**
     * Constructor to initialize the Listener with a BufferedReader.
     * 
     * @param br The BufferedReader to read messages from the server.
     */
	public Listener(BufferedReader br) {
		this.br = br;
	}

    /**
     * Continuously reads messages from the server and prints them with a timestamp.
     * Exits the program if the connection is closed.
     */
	@Override
	public void run() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd-hh:mm:ss");
		try {
			String message;
			while ((message = br.readLine()) != null) {
				System.out.println(System.lineSeparator() + dateFormat.format(new Date()) + ": " + message);
			}
		} catch (Exception e) {
			System.out.println("Connection closed.");
			System.exit(0);
		}
	}
}
