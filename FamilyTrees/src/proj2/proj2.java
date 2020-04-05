package proj2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class proj2 {
	
	private static final ArrayList<String> pretrav = new ArrayList<String>();
	private static final ArrayList<String> posttrav = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		userInterface();
		System.out.println("Done!");
	}
	
	/* User interface for the program
	 *
	*/
	public static void userInterface() {
		
		//  Initialize scanners
		Scanner console = new Scanner(System.in);
		Scanner fileScanner = null;
		PrintStream fileOutStream = null;

		//Print header
		System.out.println("**Welcome to Stephen's tree program**");
		System.out.println("Enter input filepath: ");
		
		
		String inFilePath = console.next();
		//String inFilePath = "medium-input.txt";
		//System.out.println("read: " + inFilePath);
		
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
	 	//String outFileName = "medium-output.txt";
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
		processFile(inFile, outFile, fileScanner, fileOutStream);
		
		System.out.println();
		
		fileScanner.close();
		fileOutStream.close();
		
	}
	
	/*
	 * Process File method
	 */
	public static void processFile(File inFile, File outFile, Scanner fileScanner, PrintStream fileOutStream) {
		
		fileScanner.useDelimiter("(\\p{javaWhitespace}|\\.|,)+");
		String next = "";
		
		// Build pretrav from file
		if (fileScanner.hasNext()) {
			next = fileScanner.next();
			//System.out.println("first char: " + next);
			if (next.equals("<")) {
				next = fileScanner.next();
				while (fileScanner.hasNext() && ! next.equals(">")) {
					//System.out.println("adding: " + next);
					pretrav.add(next);
					next = fileScanner.next();
				}
			} else {
				System.out.println("invalid input: does not start with '<'");
				System.exit(1);
			}
		} else {
			System.out.println("invalid input: empty");
			System.exit(1);
		}
		
		// Build posttrav from file
		if (next.equals(">")) {
			next = fileScanner.next();
			while (fileScanner.hasNext() && next != null && ! next.equals("?")) {
				//System.out.println("adding: " + next);
				posttrav.add(next);
				next = fileScanner.next();
			}
		} else {
			System.out.println("invalid input: posttrav does not start with '>'");
			System.exit(1);
		}
		
		// Build the tree and fill the parents
		TreeNode top = buildTree(pretrav.size(), 0, 0);
		fillParents(top);
		
		// Answer queries
		while (fileScanner.hasNext() && next != null && next.equals("?")) {
			String nextA = fileScanner.next();
			String nextB = fileScanner.next();
			
			String relation = getRelationship(nextA, nextB, top) + ".";
			
			// Output
			System.out.println(relation);
			fileOutStream.println(relation);
			
			if (fileScanner.hasNext()) {
				next = fileScanner.next();
			}
		}
		
		LinkedList<TreeNode> queue = levelOrder(top);
		String levelOrder = getLevelOrder(queue);
		
		// Output
		System.out.println(levelOrder);
		fileOutStream.println(levelOrder);
		
	}
	
	public static String getRelationship(String a, String b, TreeNode head) {
		TreeNode A = find(a, head);
		TreeNode B = find(b, head);
		
		// Mark A and ancestors of A
		A.mark = true;
		TreeNode aTemp = A;
		aTemp.mark = true;
		while (aTemp.parent != null) {
			aTemp.parent.mark = true;
			aTemp = aTemp.parent;
		}
		
		// Find least common ancestor from B
		TreeNode bTemp = B;
		int bCount = 0;
		while (bTemp.mark != true) {
			bCount++;
			bTemp = bTemp.parent;
		}
		
		TreeNode leastCommon = bTemp;
		
		aTemp = A;
		int aCount = 0;
		while (aTemp != null && aTemp != leastCommon) {
			aCount++;
			aTemp = aTemp.parent;
		}
		
		// Erase marks
		eraseMarks(head);
		
		return relationship(a, b, aCount, bCount);
		
	}
	
	public static String relationship(String A, String B, int a, int b) {
		if (a == 0) {
			if (b == 0) {
				return A + " is " + B + "";
			} else if (b == 1) {
				return A + " is " + B + "'s parent";
			} else if (b == 2) {
				return A + " is " + B + "'s grandparent";
			} else if (b == 3) {
				return A + " is " + B + "'s great-grandparent";
			} else if (b > 3) {
				int times = b - 2;
				String grtStr = "";
				for (int i = 0; i < times; i++) {
					grtStr = grtStr + "great-";
				}
				return A + " is " + B + "'s " + grtStr + "grandparent";
			}
		}
		
		if (b == 0) {
			if (a == 1) {
				return A + " is " + B + "'s child";
			} else if (a == 2) {
				return A + " is " + B + "'s grandchild";
			} else if (a >= 3) {
				int times = a - 2;
				String grtStr = "";
				for (int i = 0; i < times; i++) {
					grtStr = grtStr + "great-";
				}
				return A + " is " + B + "'s " + grtStr + "grandchild";
			}
		}
		
		if (a == 1) {
			if (b == 1) {
				return A + " is " + B + "'s sibling";
			} else if (b == 2) {
				return A + " is " + B + "'s aunt/uncle";
			} else if (b >= 3) {
				int times = b - 2;
				String grtStr = "";
				for (int i = 0; i < times; i++) {
					grtStr = grtStr + "great-";
				}
				return A + " is " + B + "'s " + grtStr + "aunt/uncle";
			}
		}
		
		if (a == 2 && b == 1) {
			return A + " is " + B + "'s niece/nephew";
		}
		
		if (a >= 3 && b == 1) {
			int times = a - 2;
			String grtStr = "";
			for (int i = 0; i < times; i++) {
				grtStr = grtStr + "great-";
			}
			return A + " is " + B + "'s " + grtStr + "niece/nephew";
		}
		
		if (a >= 2 && b >= 2) {
			int firstNum = Math.min(a, b) - 1;
			int secondNum = Math.abs(a - b);
			return A + " is " + B + "'s " + firstNum + "th cousin " + secondNum + " times removed";
		}
		
		return "";
	}
	
	// Size of the node in pre is the # of elements through the node in post
	// For each posttrav, find earliest pretrav
	public static TreeNode buildTree(int size, int prestart, int poststart) {
		//System.out.println("Building Tree: " + pretrav.get(prestart) + "," + size + "," + prestart + "," + poststart);
		
		// Base case: IF LEAF
		if (size == 1) {
			return new TreeNode(pretrav.get(prestart), null, null, false);
		}
		
		ArrayList<String> preSubset = new ArrayList<String>();
		ArrayList<String> postSubset = new ArrayList<String>();
		
		for (int i = prestart; i < prestart + size; i++) {
			preSubset.add(pretrav.get(i));
		}
		
		for (int i = poststart; i < poststart + size; i++) {
			postSubset.add(posttrav.get(i));
		}
		
		//System.out.println(preSubset.toString());
		//System.out.println(postSubset.toString());
		
		// Setup for identifying direct children
		ArrayList<String> children = new ArrayList<String>();
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		int subsetSize = 0;
		int accumulation = 0;
		
		// identify children
		for (int i = 1; i < preSubset.size(); i += subsetSize) {
			String element = preSubset.get(i);
			subsetSize = postSubset.indexOf(element) + 1 - accumulation;
			children.add(element);
			sizes.add(subsetSize);
			accumulation += subsetSize;
		}
		
		//System.out.println(preSubset.get(0));
		//System.out.println(children.toString());
		//System.out.println(sizes.toString());
		
		ArrayList<TreeNode> childNodes = new ArrayList<TreeNode>();
		
		if (children.size() == 0) {
			return new TreeNode(preSubset.get(0), null, null, false);
		} else {
			for (int i = 0; i < children.size(); i++) {
				//System.out.println("Parent: " + preSubset.get(0));
				TreeNode child = buildTree(sizes.get(i), pretrav.indexOf(children.get(i)), poststart);
				childNodes.add(child);
				poststart += sizes.get(i);
			}
		}
		
		return new TreeNode(pretrav.get(prestart), childNodes, null, false);
	}
	
	// Level order traversal
	// Assumes list passed contains 1 node: the tree head
	public static LinkedList<TreeNode> levelOrder(TreeNode head) {
		
		LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(head);
		
		// queue grows to contain more children as the loop continues
		// each iteration handles the next child added
		// children added in level-order
		for (int i = 0; i < queue.size(); i++) {
			TreeNode node = queue.get(i);
			if (node.children != null) {
				for (int j = 0; j < node.children.size(); j++) {
					TreeNode child = node.children.get(j);
					if (! queue.contains(child)) {
						//System.out.println("adding: " + child.element);
						queue.add(child);
					}
				}
			}
		}
		return queue;
	}
	
	public static String getLevelOrder(LinkedList<TreeNode> queue) {
		String ret = "";
		
		for (int i = 0; i < queue.size(); i++) {
			if (i < queue.size() - 1) {
				ret = ret + queue.get(i).element + ", ";
			} else {
				ret = ret + queue.get(i).element + ".";
			}
		}
		
		return ret;
	}
	
	public static void fillParents(TreeNode node) {
		if (node.children == null) {
			return;
		}
		for (int i = 0; i < node.children.size(); i++) {
			//System.out.println("Node " + node.children.get(i).element + " parent: " + node.element);
			node.children.get(i).parent = node;
			fillParents(node.children.get(i));
		}
	}
	
	public static TreeNode find(String element, TreeNode head) {
		LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(head);
		if (element.equals(head.element)) {
			return head;
		}
		
		// queue grows to contain more children as the loop continues
		// each iteration handles the next child added
		// children added in level-order
		for (int i = 0; i < queue.size(); i++) {
			TreeNode node = queue.get(i);
			if (node.children != null) {
				for (int j = 0; j < node.children.size(); j++) {
					TreeNode child = node.children.get(j);
					if (! queue.contains(child)) {
						//System.out.println("adding: " + child.element);
						queue.add(child);
						if (element.equals(child.element)) {
							return child;
						}
					}
				}
			}
		}
		return null;
	}
	
	public static void eraseMarks(TreeNode node) {
		node.mark = false;
		if (node.children == null) {
			return;
		}
		for (int i = 0; i < node.children.size(); i++) {
			//System.out.println("Node " + node.children.get(i).element + " parent: " + node.element);
			node.children.get(i).mark = false;
			eraseMarks(node.children.get(i));
		}
	}

}