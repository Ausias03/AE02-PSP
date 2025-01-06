package client;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Listener implements Runnable {
	private BufferedReader br;

	public Listener(BufferedReader br) {
		this.br = br;
	}

	@Override
	public void run() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd-hh:mm:ss");
		try {
			String message;
			while ((message = br.readLine()) != null) {
				System.out.println(dateFormat.format(new Date()) + ": " + message);
			}
		} catch (Exception e) {
			System.out.println("Connection closed.");
		}
	}
}
