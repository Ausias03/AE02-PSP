package client;

import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The Sender class implements the Runnable interface to send messages to the server.
 * It allows the user to enter and send messages, including an 'exit' command to close the connection.
 */
public class Sender implements Runnable {
    /**
     * PrintWriter used to send messages to the server.
     */
	private PrintWriter pw;

    /**
     * Constructor to initialize the Sender with a PrintWriter.
     * 
     * @param pw The PrintWriter to send messages to the server.
     */
    public Sender(PrintWriter pw) {
        this.pw = pw;
    }

    /**
     * Prompts the user to enter a message and sends it to the server.
     * The loop continues until the user enters "exit".
     */
    @Override
    public void run() {
        try {
    		Scanner sc = new Scanner(System.in);

    		String input = "";
    		System.out.println("Press ENTER to send messages");
    		while (input == null || !input.equals("exit")) {    			
    			String enter = sc.nextLine();
    			if (enter.isEmpty()) {
    				JFrame parentFrame = new JFrame();
                    parentFrame.setAlwaysOnTop(true);
    				input = JOptionPane.showInputDialog(parentFrame, "Introduce 'exit' para cerrar", "Input",
    						JOptionPane.QUESTION_MESSAGE);
    				if (input != null) {
    					pw.println(input);
    				}
    			}
    		}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
