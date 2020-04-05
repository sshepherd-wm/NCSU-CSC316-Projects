/**
 * 
 */
package proj3;

import java.io.PrintStream;
import java.util.LinkedList;

/**
 * @author Stephen Shepherd
 *
 */
public class Heap {
	
	public static final int MAX_SIZE = 5000;
	
	private HeapNode[] array;
	private int size;

	/**
	 * Heap constructor
	 */
	public Heap() {
		this.array = new HeapNode[MAX_SIZE];
		this.size = 0;
	}
	
	public void insert(Edge element) {
		// not in pseudocode, in slides it's a parameter passed
		int key = size + 1;
		
		if (this.size >= MAX_SIZE) {
			throw new IllegalArgumentException("Heap is full");
		}
		
		HeapNode node = new HeapNode(element, key);
		
		array[this.size] = node;
		this.size++;
		
		upHeap(this.size - 1);
	}
	
	public Edge deleteMin() {
		if (this.size == 0) {
			return null;
		}
		Edge element = array[0].element;
		this.size--;
		// Bring bottom rightmost element to top (now at position size)
		array[0] = array[this.size];
		
		// Move parent replacement down if needed
		downHeap(0);
		
		return element;
	}
	
	public void upHeap(int newPos) {
		// i is position of new element in heap
		if (newPos > 0) {
			if (array[(newPos - 1) / 2].key > array[newPos].key) {
				// Swap
				HeapNode child = array[newPos];
				HeapNode parent = array[(newPos - 1) / 2];
				// Switch keys too
				//child.key = ((newPos - 1) / 2) + 1;
				//parent.key = newPos - 1;
				// Replace elements
				array[newPos] = parent;
				array[(newPos - 1) / 2] = child;
				
				upHeap((newPos - 1) / 2);
			}
		}
	}
	
	public void downHeap(int m) {
		
		// i is m's smallest child, if exists
		int i = 0;
		
		// both children exist
		if (2 * m + 2 < this.size) {
			if (array[2 * m + 2].key <= array[2 * m + 1].key) {
				i = 2 * m + 2;
			} else {
				i = 2 * m + 1;
			}
		} else if (2 * m + 1 < this.size) {
			i = 2 * m + 1;
		}
		// At this stage, if i = 0, then the node has no children
		if (i > 0 & array[m].key > array[i].key) {
			HeapNode mNode = array[m];
			HeapNode iNode = array[i];
			array[m] = iNode;
			array[i] = mNode;
			downHeap(i);
		}
	}
	
	public int getMaxVertex() {
		int max = 0;
		for (int i = 0; i < this.size; i++) {
			Edge edge = array[i].element;
			int vertexMax = Math.max(edge.vertex1, edge.vertex2);
			max = Math.max(max, vertexMax);
		}
		return max;
	}
	
	public LinkedList getVertices() {
		LinkedList<Integer> vertexList = new LinkedList<Integer>();
		
		for (int i = 0; i < this.size; i++) {
			Edge edge = array[i].element;
			if (!vertexList.contains(edge.vertex1)) {
				vertexList.add(edge.vertex1);
			}
			if (!vertexList.contains(edge.vertex2)) {
				vertexList.add(edge.vertex2);
			}
		}
		return vertexList;
	}
	
	public void printHeap() {
		for (int i = 0; i < this.size; i++) {
			Edge edge = array[i].element;
			if (edge.vertex1 < edge.vertex2) {
				System.out.printf("%4d", edge.vertex1);
				System.out.print(" ");
				System.out.printf("%4d", edge.vertex2);
			} else {
				System.out.printf("%4d", edge.vertex2);
				System.out.print(" ");
				System.out.printf("%4d", edge.vertex1);
			}
			System.out.println();
		}
	}
	
	public void outputHeap(PrintStream fileOutStream) {
		String sep = System.getProperty("line.separator");
		
		for (int i = 0; i < this.size; i++) {
			Edge edge = array[i].element;
			if (edge.vertex1 < edge.vertex2) {
				fileOutStream.printf("%4d", edge.vertex1);
				fileOutStream.print(" ");
				fileOutStream.printf("%4d", edge.vertex2);
			} else {
				fileOutStream.printf("%4d", edge.vertex2);
				fileOutStream.print(" ");
				fileOutStream.printf("%4d", edge.vertex1);
			}
			fileOutStream.print(sep);
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public void printHeapTest() {
		int count = 1;
		int log = 0;
		for (int i = 0; i < this.size; i++) {
			int newLog = (int) (Math.log((double) count) / Math.log(2));
			// Add a level in printing
			if (newLog > log) {
				log = newLog;
				System.out.println();
			}
			System.out.print(array[i].key + ",");
			count++;
		}
	}
	
	public void printDeleteMins() {
		while (this.size > 0) {
			System.out.println(this.deleteMin().weight);
		}
	}

}
