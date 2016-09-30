package com.revenant.flappy.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils 
{
	// We create a private constructor to prevent anybody from creating an instance of this class
	private FileUtils() throws IOException
	{
	}
	
	// Loads the file at "path" in a Java String and returns it.
	public static String loadAsString(String path)
	{
		// We could have done this:
		// String result = "";
		// But, using the StringBuilded is much much faster when the String is to constantly appended as being done is the loop below
		StringBuilder result = new StringBuilder();
		
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			
			// Buffer is simply a string that will hold our line... I could have called this variable a line, but meh
			String buffer = "";
			
			while( (buffer = reader.readLine()) != null )
			{
				result.append(buffer + '\n');
				// result += buffer + "\n";
			}
			
			reader.close();			
		} catch(IOException e) {  // Technically, two exceptions, FileReadException and IOException will be thrown, but the IOException includes the
								  // FileReadException, so we use that
			e.printStackTrace();
		}
		
		return result.toString();
	}
}
