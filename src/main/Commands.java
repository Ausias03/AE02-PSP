package main;

import java.io.IOException;
import java.io.PrintWriter;

public class Commands {
	public static void execute(String command, Peticio user) {
		String channelCommand = "";
		int channel = -1;
		if(command.length() >= 7 && command.substring(0, 6).equals("@canal")) {
			try {
				channel = Integer.parseInt(command.substring(7, 8));
				channelCommand = command.substring(command.indexOf(' ') + 1);
				command = "channel";
			} catch (Exception e) {}
		}
		
		switch(command) {
		case "whois":
			whoIs(user);
			break;
		case "channel":
			channel(channelCommand, channel, user);
			break;
		case "exit":
			exit(user);
			break;
		case "channels":
			channels(user);
			break;
		default:
			message(command, user);
			break;
		}
	}
	
	private static void whoIs(Peticio user) {
		String names = "";
		for(Peticio p : Servidor.clients) {
			if(p.getChannel() == user.getChannel()) {
				names += String.format("%s | ", p.getUserName());
			}
		}
		try {
			PrintWriter pw = new PrintWriter(user.getSocket().getOutputStream(), true);
			pw.println(names);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void message(String message, Peticio user) {
		for(Peticio p : Servidor.clients) {
			if(p == user)
				continue;
			
			if(p.getChannel() == user.getChannel()) {
				try {
					PrintWriter pw = new PrintWriter(p.getSocket().getOutputStream(), true);
					pw.println(message);
					pw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void channel(String message, int channel, Peticio user) {
		for(Peticio p : Servidor.clients) {
			if(p == user)
				continue;
			
			if(p.getChannel() == channel) {
				try {
					PrintWriter pw = new PrintWriter(p.getSocket().getOutputStream(), true);
					pw.println(message);
					pw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void exit(Peticio user) {
		try {
			Servidor.clients.remove(user);
			user.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void channels(Peticio user) {
		try {
			PrintWriter pw = new PrintWriter(user.getSocket().getOutputStream(), true);
			pw.println(Channels.getChannels());
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
