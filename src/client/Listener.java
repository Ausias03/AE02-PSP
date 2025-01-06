package client;

import java.io.BufferedReader;

public class Listener implements Runnable{
	private BufferedReader br;

    public Listener(BufferedReader br) {
        this.br = br;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = br.readLine()) != null) {
                System.out.println("Servidor: " + message);
            }
        } catch (Exception e) {
            System.out.println("Conexión cerrada por el servidor.");
        }
    }
}
