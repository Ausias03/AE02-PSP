package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.commons.codec.digest.DigestUtils;

public class Peticio implements Runnable {
	Socket socket;

	public Peticio(Socket socket) {
		this.socket = socket;
	}


	public void run() {	}
}
