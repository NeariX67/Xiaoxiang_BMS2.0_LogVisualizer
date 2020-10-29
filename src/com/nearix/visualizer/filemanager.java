package com.nearix.visualizer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class filemanager {
	
	
	public static ArrayList<String> readFile(File file) {
		LineNumberReader f;
		String line;
		ArrayList<String> lineList = new ArrayList<String>();
		
		try {
			f = new LineNumberReader(new FileReader(file));
			while((line = f.readLine()) != null) {
				lineList.add(line);
			}
			f.close();
		}
		catch(IOException e) {
			System.err.println(e.toString());
		}
		return lineList;
	}
}
