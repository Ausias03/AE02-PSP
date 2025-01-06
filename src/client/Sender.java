package client;

import java.io.PrintWriter;
import java.util.Scanner;

public class Sender implements Runnable {
	private PrintWriter pw;

    public Sender(PrintWriter pw) {
        this.pw = pw;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            String message;
            System.out.println("Write");

            while (true) {
                message = scanner.nextLine();
                if ("salir".equalsIgnoreCase(message)) {
                    System.out.println("Disconnecting");
                    break;
                }
                pw.println(message);
            }
        } catch (Exception e) {
            System.out.println("ERROR.");
        }
    }
}
