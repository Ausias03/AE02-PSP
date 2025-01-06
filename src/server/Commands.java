package server;

import java.io.IOException;
import java.io.PrintWriter;

public class Commands {
	public static void execute(String command, Peticio user) {
		String channelCommand = "";
		int channel = -1;
		if(command.length() >= 7 && command.substring(0, 6).equals("@canal")) {
			try {
				channel = Integer.parseInt(command.substring(6, 7));
				channelCommand = command.substring(command.indexOf(' ') + 1);
				command = "channel";
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			names = names.substring(0, names.length() - 2);
			PrintWriter pw = new PrintWriter(user.getSocket().getOutputStream(), true);
			pw.println(names);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void message(String message, Peticio user) {
		for(Peticio p : Servidor.clients) {
			if(p.getChannel() == user.getChannel()) {
				try {
					PrintWriter pw = new PrintWriter(p.getSocket().getOutputStream(), true);
					if(p == user)
						pw.println(message);
					else
						pw.println(String.format("%s >>> %s", user.getUserName(), message));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void channel(String message, int channel, Peticio user) {
		for(Peticio p : Servidor.clients) {
			if(p.getChannel() == channel || p == user) {
				try {
					PrintWriter pw = new PrintWriter(p.getSocket().getOutputStream(), true);
					if(p == user)
						pw.println(String.format("@canal%d %s", channel, message));
					else
						pw.println(String.format("(canal%d, %s) >>> %s", user.getChannel(), user.getUserName(), message));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static synchronized void exit(Peticio user) {
		try {
			Servidor.clients.remove(user);
			user.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static synchronized void channels(Peticio user) {
		try {
			PrintWriter pw = new PrintWriter(user.getSocket().getOutputStream(), true);
			pw.println(Channels.getChannels());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
