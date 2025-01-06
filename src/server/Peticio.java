package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.codec.digest.DigestUtils;

public class Peticio implements Runnable {
	private Socket socket;
	private int channel = -1;
	private String userName = "";

	public Socket getSocket() {
		return socket;
	}
	
	public int getChannel() {
		return channel;
	}

	public String getUserName() {
		return userName;
	}

	public Peticio(Socket socket) {
		this.socket = socket;
	}

	public void ChannelSelection(BufferedReader bf, PrintWriter pw) throws Exception {
		System.err.println("SERVER >>> Waiting for channel selection");
		String channels = Channels.getChannels();
		pw.println(channels);

		String channelSelected = bf.readLine();
		channel = Integer.parseInt(channelSelected);
	}

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
	
	public void readMessages(BufferedReader bf) throws Exception{
		String message;
		
		do {
			message = bf.readLine();
			System.err.println("SERVER >>> " + userName + " (canal " + channel + ") >>> " + message);
			Commands.execute(message, this);
		} while (!message.equals("exit"));
	}

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
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("SERVIDOR >>> Error.");
		}
	}
}
