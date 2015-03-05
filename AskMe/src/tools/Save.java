package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Save {
	private FileWriter output;

	/**
	 * create a log file to write in it
	 * 
	 * @param name
	 *            : the name of your file, not the path
	 * @throws IOException
	 */
	public Save(String name) throws IOException {
		File temp = new File("logs/" + name + ".simplouf");
		output = new FileWriter(temp);
	}

	public void print(String string) throws IOException {
		output.write(string);
	}

	public void println(String string) throws IOException {
		output.write(string + System.getProperty("line.separator"));
	}

	public void close() throws IOException {
		output.close();
	}

	public boolean equals(Object o) {
		return output.equals(o);
	}
}
