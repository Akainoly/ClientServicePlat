package test;

import java.io.File;
import java.io.IOException;

public class Test {
	public static void main(String[] args) {
		File file=new File("file://d:/teller_screen2.flv");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(file.exists());
	}
}
