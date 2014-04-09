package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileIO {
	public static String getFileContents(File file){
		String contents = "";
		try {
			Scanner scan = new Scanner(file);
			while(scan.hasNext()){
				contents += scan.nextLine();
				if(scan.hasNext())
					contents += "\n";
			}
		} catch (FileNotFoundException e) {
		}
		return contents;
	}
	
	public static void writeToFile(String file, String contents){
		try{
			FileWriter fw = new FileWriter(new File(file));
			fw.write(contents);
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
