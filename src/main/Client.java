package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {

	private static int obtainChannel(String channels) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd-hh:mm:ss");
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
	
	private static String obtainUsername(PrintWriter pw, BufferedReader br) throws Exception {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Indica nombre de usuario: ");
		String username = sc.nextLine();
		pw.println(username);
		String serverResponse = br.readLine();
		
		while(serverResponse.equals(UserNameStatus.CHOSEN.toString())) {
			System.out.print("El usuario ya existe, indica otro: ");
			username = sc.nextLine();
			pw.println(username);
			serverResponse = br.readLine();
		}
		
		return username;
	}
	
	public static void main(String[] args) {
		try (Socket socket = new Socket("127.0.0.1", 1234)) {
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			
			String channels = br.readLine();
			
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println(String.valueOf(obtainChannel(channels)));
			obtainUsername(pw, br);
			System.out.println("acaba");
			br.close();
			isr.close();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
