package uk.ac.cam.cl.dtg.sac92.oop.word_game;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import static java.nio.file.StandardOpenOption.*;

/**
 * Holds a few file I/0 algorithms to demonstrate I understand it.
 * 
 * @author Navid
 *
 */
public abstract class FileIOUtilities {
	
	/**
	 * Gets the contents of a text file.
	 * @param fileName - Path of the text file.
	 * @return - Contents of the text file.
	 */
	static String getFile(String filePath) 
	{		
		// Better as we are doing a lot of concatenation.
		StringBuilder sb = new StringBuilder();
		
		// Turn it into a path.
		Path path = Paths.get(filePath);
	
		// Try with resources statement - we do not have to close these.
		try(InputStream in = Files.newInputStream(path);
			BufferedReader bfr = new BufferedReader(new InputStreamReader(in)))
		{
			String line = bfr.readLine();
			
			while(line != null)
			{
				sb.append(line + "\n");
				line = bfr.readLine();
			}
		} catch (IOException e) {
			System.out.println("IO Exception :(");
		}
		
		return sb.toString();
	}
	
	/**
	 * Writes the string data to a specified file, optionally appending it.
	 * 
	 * @param filePath - Path of the text file.
	 * @param shouldAppend - If true then it will append else it write over.
	 * @param data - The data to write.
	 * @return - True if successful, false otherwise. 
	 */
	static boolean writeToFile(
			String filePath, boolean shouldAppend, String data)
	{
		// Turn it into a path.
		Path path = Paths.get(filePath);
		// Convert the string into bytes for writing.
		byte[] dataBytes = data.getBytes();
		
		// Use the parameter shouldAppend to decide what options to us for the 
		// newOutputStream.
		try(OutputStream out = new BufferedOutputStream(
				Files.newOutputStream(path, shouldAppend ? APPEND : CREATE))) 
		{
			// Write the data.
			out.write(dataBytes, 0, data.length());
		} catch (IOException e) {
			System.out.println("IO Exception :(");
			return false;
		}
		
		return true;
	}
	
}
