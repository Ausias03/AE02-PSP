package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import server.UserNameStatus;

/**
 * The Client class connects to a server, allows the user to select a channel, 
 * choose a username, and facilitates the sending and receiving of messages.
 */
public class Client {

    /**
     * Prompts the user to select a channel from the available ones.
     * 
     * @param channels A comma-separated string of available channels.
     * @return The selected channel number.
     */
	private static int obtainChannel(String channels) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd-hh:mm:ss");
		int amountChannels = channels.split(",").length;

		Scanner sc = new Scanner(System.in);

		int channel;
		System.out.println(String.format("%s: Canales disponibles: %s", dateFormat.format(new Date()), channels));
		do {
			try {
				System.out.print("Selecciona canal: ");
				channel = sc.nextInt();
			} catch (Exception e) {
				channel = -1;
				System.out.println("Non numeric value introduced");
			}
		} while (channel < 1 || channel > amountChannels);

		return channel;
	}

    /**
     * Prompts the user to enter a username and ensures it is valid and unique.
     * 
     * @param pw The PrintWriter to send the username to the server.
     * @param br The BufferedReader to receive the server's response.
     * @return The chosen username.
     * @throws Exception If an error occurs while communicating with the server.
     */
	private static String obtainUsername(PrintWriter pw, BufferedReader br) throws Exception {
		Scanner sc = new Scanner(System.in);

		String username;
		do {
			System.out.print("Indica nombre de usuario: ");
			username = sc.nextLine();
			if (username.contains(" ")) {
				System.out.println("EL NOMBRE DE USUARIO NO PUEDE TENER ESPACIOS!!!");
			}
		} while (username.contains(" "));

		pw.println(username);
		String serverResponse = br.readLine();

		while (username.contains(" ") || serverResponse.equals(UserNameStatus.CHOSEN.toString())) {
			System.out.print("El usuario ya existe, indica otro: ");
			username = sc.nextLine();
			if(username.contains(" ")) {
				System.out.println("EL NOMBRE DE USUARIO NO PUEDE TENER ESPACIOS!!!");
			}
			else {
				pw.println(username);
				serverResponse = br.readLine();				
			}
		}

		return username;
	}

    /**
     * Main method to establish connection with the server, allow the user to choose a channel 
     * and username, and start threads for sending and receiving messages.
     * 
     * @param args Command-line arguments.
     */
	public static void main(String[] args) {
		try (Socket socket = new Socket("127.0.0.1", 1234)) {
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			String channels = br.readLine();

			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println(String.valueOf(obtainChannel(channels)));
			obtainUsername(pw, br);
			
			Thread listenerThread = new Thread(new Listener(br));
            Thread senderThread = new Thread(new Sender(pw));
            listenerThread.start();
            senderThread.start();
            listenerThread.join();
            senderThread.join();
            
			br.close();
			isr.close();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
