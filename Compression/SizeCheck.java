/**
 * 
 */

import java.io.File;

/**
 * @author Stephen Shepherd
 *
 */
public class SizeCheck {

	public static void main(String args[]) {
		//String[] filePres = new String[10];
		
		System.out.println("File, Original, Compressed, Zipped");
		
		for (int i = 0; i < 10; i++) {
			String base = "test-files/results-compare/input" + (i + 1);
			String orig = base + ".txt";
			String comp = base + "_comp.txt";
			String zip = base + ".zip";
			
			File origFile = new File(orig);
			File compFile = new File(comp);
			File zipFile = new File(zip);
			
			System.out.println(orig + "," + (int) origFile.length() + ","
					+ (int) compFile.length() + "," + (int) zipFile.length());
		}
	}
}
