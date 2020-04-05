/**
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * @author Stephen Shepherd
 *
 */
public class proj1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Error: arguments passed invalid");
			System.out.println("Try: First arg: Input filename.  Second arg: Output filename.");
			System.out.println("Exiting.");
			System.exit(1);
		}
		userInterface(args);
	}
	
	/**
	 * User interface for the program
	 */
	/**
	* User interface for the program
	* @param keyword args
	*/
	public static void userInterface(String[] args) {
		
		//  Initialize scanners
		Scanner console = new Scanner(System.in);
		Scanner fileScanner = null;
		PrintStream fileOutStream = null;

		// Initialize compress/decompress variable
		boolean compress = true;

		//Print header
		System.out.println("**Welcome to Stephen's compression program**");
		System.out.println("...trying input and output paths passed in cmd line.");
		System.out.println("...Running with Input: " + args[0] + ", and Output: " + args[1]);

		//String inFilePath = console.next();
		String inFilePath = args[0];
		
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
	 	
	 	//outFileName = console.next();
	 	String outFileName = args[1];
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
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("Error: cannot write file.");
			System.exit(1);
		}

		// Process the file
		processFile(inFile, outFile, fileScanner, fileOutStream);
		System.out.println("Done!");
		System.out.println();
	}
	
	/**
	 * Method that processes the file encryption/decryption
	 * @return 
	 */
	public static void processFile(File inFile, File outFile, Scanner fileScanner, PrintStream fileOutStream) {
		WordList list = new WordList();
		
		boolean encrypt = true;
		if (fileScanner.hasNextInt()) {
			int first = fileScanner.nextInt();
			if (first == 0) {encrypt = false;}
		}
		//fileScanner.reset();
		
		if (encrypt) {
			processCompress(inFile, outFile, fileScanner, fileOutStream, list);
		}
		
		if (!encrypt) {
			processDecompress(inFile, outFile, fileScanner, fileOutStream, list);
		}
	}
	
	/**
	 * Method for compressing a file
	 */
	public static void processCompress(File inFile, File outFile, Scanner fileScanner, PrintStream fileOutStream, WordList list) {
		fileOutStream.print("0 ");
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
					//System.out.println(nextWord);
					int result = list.cycle(nextWord);
					if (result >= 1) {
						//System.out.println(list.toString());
						fileOutStream.print(result);
					} else if (result <= 0) {
						fileOutStream.print(nextWord);
					}
					// Increase the idx
					idx = idx + nextWord.length();
				} else {
					fileOutStream.print(nextChar);
					idx++;
				}
			}
			if (fileScanner.hasNextLine()) {
				//fileOutStream.println();
				fileOutStream.print("\n");
			}
		}
		// Summary statistics
		//fileOutStream.println();
		fileOutStream.print("\n");
		int ucbytes = (int) inFile.length();
		int cbytes = (int) outFile.length();
		fileOutStream.println("0 Uncompressed: " + ucbytes + " bytes;  Compressed: " + cbytes + " bytes");
	}
	
	/**
	 * Method for DEcompressing a file
	 */
	public static void processDecompress(File inFile, File outFile, Scanner fileScanner, PrintStream fileOutStream, WordList list) {
		int linecount = 0;
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			int idx = 0;
			if (linecount == 0) {idx = 1;}
			
			while (idx < line.length()) {
				Character nextChar = line.charAt(idx);
				if (Character.isLetter(nextChar)) {
					Scanner lineScanner = new Scanner(line.substring(idx));
					//lineScanner.useDelimiter("[^a-zA-Z0-9']+");
					lineScanner.useDelimiter("[^\\p{L}0-9']+");
					String nextWord = lineScanner.next();
					int result = list.cycle(nextWord);
					fileOutStream.print(nextWord);
					// Increase the idx
					idx = idx + nextWord.length();
				} else if (Character.isDigit(nextChar)) {
					//int digit = Character.getNumericValue(nextChar);
					Scanner lineScanner = new Scanner(line.substring(idx));
					lineScanner.useDelimiter("[^a-zA-Z0-9']+");
					int number = lineScanner.nextInt();
					// Test for last line compression summary
					if (number == 0) {
						break;
					}
					String listWord = list.get(number);
					fileOutStream.print(listWord);
					int digitLength = ("" + number).length();
					idx = idx + digitLength;
				} else {
					fileOutStream.print(nextChar);
					idx++;
				}
			}
			if (fileScanner.hasNextLine()) {
				//fileOutStream.println();
				fileOutStream.print("\n");
			}
			linecount++;
		}
	}
	
	/**
	 * Linked List
	 */
	private static class WordList {
		// Front of list
		private Node head;
		
		/**
		 * Constructor
		 */
		public WordList() {
			head = null;
		}
		
		/**
		 * List Node
		 */
		private class Node {
			private String word;
			private Node next;
			
			public Node(String word, Node next) {
				this.word = word;
				this.next = next;
			}
		}
		
		public void add(String word) {
			if (head == null) {
				head = new Node(word, null);
			}
			if (head != null) {
				head = new Node(word, head);
			}
		}
		
		public int cycle(String word) {
			//System.out.println(this.toString());
			
			int pos = 0;
			boolean contains = false;
			Node previous = null;
			Node current = head;
			while (current != null) {
				pos++;
				if (current.word.equals(word)) {
					// Remove and move to front
					if (pos == 1) {
						// Leave head as-is
						contains = true;
					} else {
						// Add to front
						head = new Node(current.word, head);
						// Remove from current position
						previous.next = current.next;
						contains = true;
					}
					break;
				} else {
					previous = current;
					current = current.next;
				}
			}
			if (contains) {
				return pos;
			} else {
				// Insert at front
				head = new Node(word, head);
				return 0;
			}
		}
		
		public String get(int pos) {
			if (pos < 1) {
				throw new IllegalArgumentException("Invalid position: " + pos);
			}
			Node previous = null;
			Node current = head;
			int count = 1;
			if (pos == 1) {
				// Leave head as-is
				return head.word;
			}
			while (count <= pos && current != null) {
				if (pos == count) {
					// Get word, delete node, move to front
					String retWord = current.word;
					//System.out.println(retWord);
					previous.next = current.next;
					head = new Node(retWord, head);
					return retWord;
				} else {
					count++;
					previous = current;
					current = current.next;
				}
			}
			return "";
		}
		
		public String toString() {
			String ret = "[";
			Node current = head;
			while (current !=null) {
				ret = ret + current.word;
				if (current.next != null) {
					ret = ret + ", ";
				}
				current = current.next;
			}
			ret = ret + "]";
			return ret;
		}
		
	}
	
}
