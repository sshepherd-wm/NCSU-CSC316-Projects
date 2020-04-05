/**
 * 
 */
package proj4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Stephen Shepherd
 *
 */
public class proj4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		userInterface();

	}
	
	/**
	 * User interface method
	 */
	public static void userInterface() {

		//  Initialize scanners
		Scanner console = new Scanner(System.in);
		Scanner dictScanner = null;
		Scanner dictCountScanner = null;
		Scanner fileScanner = null;
		PrintStream fileOutStream = null;
		
		//Print header
		System.out.println("**Welcome to Stephen's SpellCheck program**");
		
		// Get dictionary
		System.out.println("Enter DICTIONARY filepath: ");
		
		String dictFilePath = console.next();
		//String dictFilePath = "dictionary.txt";
		
		System.out.println("read: " + dictFilePath);
		
		File dictFile = null;
		
		// Test if input file exists and handle exception
		//File inFile = new File(inFileName);
		do {
			try {
				dictFile = new File(dictFilePath);
				dictScanner = new Scanner(dictFile, "UTF-8");
				dictCountScanner = new Scanner(dictFile, "UTF-8");
			} catch (FileNotFoundException e) {
				System.out.println("Dictionary file not found.  Enter new filename:");
				dictFilePath = console.next();
			}
		} while(! dictFile.exists());
		
		// Get file to be checked
		System.out.println("Enter path of file you want to spellcheck: ");
		
		String inFilePath = console.next();
		//String inFilePath = "input10.txt";

		System.out.println("read: " + inFilePath);
		
		File inFile = null;
		
		// Test if input file exists and handle exception
		//File inFile = new File(inFileName);
		do {
			try {
				inFile = new File(inFilePath);
				fileScanner = new Scanner(inFile, "UTF-8");
			} catch (FileNotFoundException e) {
				System.out.println("Input file not found.  Enter new filename:");
				inFilePath = console.next();
			}
		} while(! inFile.exists());

		// Get output printstream
	 	File outFile = null;
	 	String writeChoice = "";
	 	
	 	System.out.println("Enter output filepath: ");
	 	
	 	String outFileName = console.next();
	 	//String outFileName = "test.txt";
	 	outFile = new File(outFileName);
	 	
	 	while (outFile.exists()) {
	 		System.out.println("Output file already exists. OK to overwrite file? (y/n):");
	 		writeChoice = console.next();
	 		
	 		if (! writeChoice.equals("y")) {
	 			System.out.println("Enter new OUTPUT file name:");
	 			outFileName = console.next();
	 			outFile = new File(outFileName);
	 		} else if (writeChoice.equals("y")) {
	 			break;
	 		}
	 	}

	 	try {
			fileOutStream = new PrintStream(outFile, "UTF-8");
		} catch (FileNotFoundException e) {
			System.out.println("Error: cannot write file as file is not initialized/found.");
			System.exit(1);
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error: unsupported encoding.");
			System.exit(1);
		}
	 	
	 	System.out.println();

		// Process the file
		processFile(dictCountScanner, dictScanner, fileScanner, fileOutStream);
		
		System.out.println();
		
		fileScanner.close();
		fileOutStream.close();
		
	}
	
	public static void processFile(Scanner dictCountScanner, Scanner dictScanner, Scanner fileScanner, PrintStream fileOutStream) {
		
		// Count words in dictionary
		int dictSize = 0;
		while (dictCountScanner.hasNextLine()) {
			dictSize++;
			dictCountScanner.nextLine();
		}
		dictCountScanner.close();
		
		ArrayList<String> wordList = new ArrayList<String>();
		
		// Build hash table
		HashTable table = new HashTable(dictSize);
		
		while (dictScanner.hasNext()) {
			//String next = dictScanner.next();
			table.insert(dictScanner.next());
			//wordList.add(next);
		}
		
		// Words to be spell checked
		int inputWords = 0;
		// Total comparisons
		int comparisons = 0;
		// Words not found in table
		int notFound = 0;
		
		// List of suspect spelling words
		ArrayList<String> suspect = new ArrayList<String>();
		
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			int idx = 0;
			while (idx < line.length()) {
				Character nextChar = line.charAt(idx);
				if (Character.isLetter(nextChar)) {
					Scanner lineScanner = new Scanner(line.substring(idx));
					//lineScanner.useDelimiter("[^a-zA-Z0-9']+");
					lineScanner.useDelimiter("[^\\p{L}0-9']+");
					String nextWord = lineScanner.next();
					inputWords++;
					int wordComps = table.checkWord(nextWord);
					//System.out.println("---****" + nextWord + ": " + wordComps);
					if (wordComps < 1) {
						fileOutStream.println(nextWord);
						notFound++;
					}
					comparisons += Math.abs(wordComps); 
					
					// Increase the idx
					idx = idx + nextWord.length();
				} else {
					//fileOutStream.print(nextChar);
					idx++;
				}
			}
		}
		
		System.out.println("Done! Misspelled words that were flagged were output to file.");
		System.out.println();
		
		System.out.println("*** SPELL CHECK REPORT ***");
		System.out.println("Words in dictionary   : " + table.getItemCount());
		System.out.println("Words in input file   : " + inputWords);
		System.out.println("Words misspelled      : " + notFound);
		System.out.println("Probes during check   : " + comparisons);
		System.out.println("Avg Probes per word   : " + (double) comparisons / (double) inputWords);
		System.out.println("Avg Probes per lookUp : " + (double) comparisons / (double) table.getCountLookUps());
		
		//System.out.println();
		//System.out.println("*** HASH TABLE DISTRIBUTION REPORT ***");
		//table.printDistributionReport();
		
	}

}
