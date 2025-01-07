package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * The Peticio class represents a client connection and handles user input,
 * channel selection, and message processing for each connected user.
 */
public class Peticio implements Runnable {
    /**
     * The socket used to communicate with the client.
     */
	private Socket socket;
    /**
     * The channel to which the user is assigned.
     */
	private int channel = -1;
    /**
     * The username chosen by the user.
     */
	private String userName = "";

    /**
     * Gets the socket associated with this client.
     * 
     * @return The client's socket.
     */
	public Socket getSocket() {
		return socket;
	}
	
    /**
     * Gets the channel assigned to the user.
     * 
     * @return The user's channel.
     */
	public int getChannel() {
		return channel;
	}

    /**
     * Gets the username of the user.
     * 
     * @return The user's username.
     */
	public String getUserName() {
		return userName;
	}

    /**
     * Constructor to initialize the Peticio with a client socket.
     * 
     * @param socket The client socket.
     */
	public Peticio(Socket socket) {
		this.socket = socket;
	}

    /**
     * Handles the process of selecting a channel for the user.
     * 
     * @param bf The BufferedReader to read client input.
     * @param pw The PrintWriter to send output to the client.
     * @throws Exception If an error occurs during channel selection.
     */
	public void ChannelSelection(BufferedReader bf, PrintWriter pw) throws Exception {
		System.err.println("SERVER >>> Waiting for channel selection");
		String channels = Channels.getChannels();
		pw.println(channels);

		String channelSelected = bf.readLine();
		channel = Integer.parseInt(channelSelected);
	}

    /**
     * Handles the process of selecting a username for the user.
     * 
     * @param bf The BufferedReader to read client input.
     * @param pw The PrintWriter to send output to the client.
     * @throws Exception If an error occurs during username selection.
     */
	public void UserNameSelection(BufferedReader bf, PrintWriter pw) throws Exception {
		while (userName.length() == 0) {
			String userNameInput = bf.readLine();
			boolean userNameTaken = Servidor.isUserNameTaken(userNameInput, channel);
			pw.println(userNameTaken ? UserNameStatus.CHOSEN.toString() : UserNameStatus.EMPTY.toString());
			if (!userNameTaken) {
				userName = userNameInput;
			}
		}
	}
	
    /**
     * Reads and processes messages sent by the client.
     * 
     * @param bf The BufferedReader to read client input.
     * @throws Exception If an error occurs while reading messages.
     */
	public void readMessages(BufferedReader bf) throws Exception{
		String message;
		
		do {
			message = bf.readLine();
			System.err.println("SERVER >>> " + userName + " (canal " + channel + ") >>> " + message);
			Commands.execute(message, this);
		} while (!message.equals("exit"));
	}

    /**
     * The main run method to handle the entire client connection process.
     * It manages the channel selection, username selection, and message reading.
     */
	public void run() {
		try {
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			OutputStream os = socket.getOutputStream();
			BufferedReader bf = new BufferedReader(isr);
			PrintWriter pw = new PrintWriter(os, true);

			ChannelSelection(bf, pw);
			UserNameSelection(bf, pw);
			System.err.println("SERVER >>> User " + userName + " has selected channel " + channel);
			readMessages(bf);

			pw.close();
			bf.close();
			os.close();
			isr.close();
			is.close();
		} catch (SocketException ex) {
			System.err.println("CLIENT " + userName + " has closed the connection");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("SERVIDOR >>> Error.");
		} 
	}
}
