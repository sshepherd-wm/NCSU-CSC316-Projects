/**
 * 
 */
package proj3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * @author Stephen Shepherd
 *
 */
public class proj3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Heap heap = new Heap();
		
		userInterface(heap);

	}
	
	/**
	 * User interface method
	 */
	public static void userInterface(Heap heap) {

		//  Initialize scanners
		Scanner console = new Scanner(System.in);
		Scanner fileScanner = null;
		PrintStream fileOutStream = null;
		
		//Print header
		System.out.println("**Welcome to Stephen's MST program**");
		System.out.println("Enter input filepath: ");
		
		String inFilePath = console.next();
		// ***
		//String inFilePath = "test-files/twenty-test.txt";
		//String inFilePath = "test-files/graph2-input.txt";
		// **
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
	 	//String outFileName = "test-files/graph2-output_test.txt";
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
		processFile(inFile, outFile, fileScanner, fileOutStream, heap);
		
		System.out.println();
		
		fileScanner.close();
		fileOutStream.close();
		
	}
	
	public static void processFile(File inFile, File outFile, Scanner fileScanner, PrintStream fileOutStream, Heap heap) {
		
		EdgeList edgeList = new EdgeList();
		
		// Make heap and list of edges
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			
			Scanner lineScanner = new Scanner(line);
			int firstEnd = lineScanner.nextInt();
			
			int secondEnd = 0;
			double weight = 0.0;
			if (firstEnd > -1) {
				secondEnd = lineScanner.nextInt();
				weight = lineScanner.nextDouble();
			} else {
				break;
			}
			
			Edge edge = new Edge(firstEnd, secondEnd, weight, null);
			//System.out.println(edge.vertex1);
			
			// Insert into heap
			heap.insert(edge);
			// Insert into edgeList
			edgeList.insert(edge);
		}
		
		System.out.println("Printing results to console, and also writing output file:");
		
		heap.printHeap();
		heap.outputHeap(fileOutStream);
		
		EdgeList MSTList = kruskal(heap);
		MSTList.printList();
		MSTList.outputList(fileOutStream);
		
		VertexList vertexList = makeVertexList(edgeList);
		printAdjacencyList(vertexList, edgeList);
		outputAdjacencyList(vertexList, edgeList, fileOutStream);
		
		System.out.println("Done!");
	}
	
	public static EdgeList kruskal(Heap heap) {
		// Make up tree with max position == max vertex
		UpTree tree = new UpTree(heap.getMaxVertex() + 1);
		EdgeList list = new EdgeList();
		
		// Number of connected components
		// Each vertex is a single connected component to start
		int comp = tree.getSize();
		
		while (comp > 1) {
			//System.out.println(comp);
			Edge e = heap.deleteMin();
			int u = tree.find(e.vertex1);
			int v = tree.find(e.vertex2);
			if (u != v) {
				// Union components if they are part of the same tree
				tree.union(u, v);
				list.insert(e);
				comp--;
			}
		}
		return list;
	}
	
	public static VertexList makeVertexList(EdgeList edgeList) {
		VertexList vertexList = new VertexList();
		for (int i = 0; i < edgeList.getSize(); i++) {
			Edge e = edgeList.get(i);
			//System.out.println(e.vertex1 + " " + e.vertex2);
			if (! vertexList.contains(e.vertex1)) {
				vertexList.insert(e.vertex1);
			}
			if (! vertexList.contains(e.vertex2)) {
				vertexList.insert(e.vertex2);
			}
		}
		return vertexList;
	}
	
	public static void printAdjacencyList(VertexList vertexList, EdgeList edgeList) {
		
		for (int i = 0; i < vertexList.getSize(); i++) {
			int v = vertexList.get(i);
			VertexList vEdges = new VertexList();
			for (int j = 0; j < edgeList.getSize(); j++) {
				Edge e = edgeList.get(j);
				if (e.vertex1 == v) {
					vEdges.insert(e.vertex2);
				} else if (e.vertex2 == v) {
					vEdges.insert(e.vertex1);
				}
			}
			
			for (int k = 0; k < vEdges.getSize(); k++) {
				System.out.printf("%4d", vEdges.get(k));
				if (k < vEdges.getSize() - 1) {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	
	public static void outputAdjacencyList(VertexList vertexList, EdgeList edgeList, PrintStream fileOutStream) {
		
		for (int i = 0; i < vertexList.getSize(); i++) {
			int v = vertexList.get(i);
			VertexList vEdges = new VertexList();
			for (int j = 0; j < edgeList.getSize(); j++) {
				Edge e = edgeList.get(j);
				if (e.vertex1 == v) {
					vEdges.insert(e.vertex2);
				} else if (e.vertex2 == v) {
					vEdges.insert(e.vertex1);
				}
			}
			
			for (int k = 0; k < vEdges.getSize(); k++) {
				fileOutStream.printf("%4d", vEdges.get(k));
				if (k < vEdges.getSize() - 1) {
					fileOutStream.print(" ");
				}
			}
			fileOutStream.println();
		}
	}
	
}
