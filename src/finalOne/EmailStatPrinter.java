package finalOne;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EmailStatPrinter extends EmailStatObserver {
	
	private File file;
	
	public EmailStatPrinter(File file) {
		this.file = file;
	}
	
	public void update(String notification) {
		try {
			writeOnFile(notification);
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void writeOnFile(String text) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
		writer.write(text);
		writer.newLine();
		writer.close();	
	}
	
}
