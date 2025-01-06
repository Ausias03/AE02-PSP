package server;

import java.io.BufferedReader;
import java.io.FileReader;

public class Channels {
	private static final String fileLocation = "resources/channels.txt";
	
	public static String getChannels() {
		try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
			String channels = "[";
			String line;
			int i = 1;
			while((line = br.readLine()) != null) {
				if(i != 1)
					channels += ", ";
				channels += String.format("%d-%s", i, line);
				i++;
			}
			channels += "]";
			return channels;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
