package server;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * The Channels class provides functionality to read channel names from a text file and 
 * format them as a string.
 */
public class Channels {
    /**
     * Path to the channels file.
     */
	private static final String fileLocation = "resources/channels.txt";
	
    /**
     * Reads the channel names from the file and returns them as a formatted string.
     * 
     * @return A string representing the list of channels in the format "[1-Channel1, 2-Channel2, ...]"
     *         or an empty string if an error occurs during file reading.
     */
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
