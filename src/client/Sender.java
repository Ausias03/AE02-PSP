package client;

import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Sender implements Runnable {
	private PrintWriter pw;

    public Sender(PrintWriter pw) {
        this.pw = pw;
    }

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
