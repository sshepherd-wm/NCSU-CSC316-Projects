/**
 * 
 */
package proj3;

import java.io.PrintStream;

/**
 * @author Stephen Shepherd
 *
 */
public class EdgeList {
	// Dummy header
	private ListNode dummy;
	private int size;
	
	public EdgeList() {
		this.dummy = new ListNode(null, null);
		this.size = 0;
	}
	
	public int compare(Edge a, Edge b) {
		int a1 = Math.min(a.vertex1, a.vertex2);
		int a2 = Math.max(a.vertex1, a.vertex2);
		
		int b1 = Math.min(b.vertex1, b.vertex2);
		int b2 = Math.max(b.vertex1, b.vertex2);
		
		if (a1 < b1) {
			return -1;
		} else if (a1 == b1) {
			if (a2 < b2) {
				return -1;
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}
	
	public void insert(Edge toAdd) {
		insertTask(dummy, dummy.next, toAdd);
		//System.out.println(toAdd.vertex1 + "_" + toAdd.vertex2 + "_" + toAdd.weight);
	}
	
	public void insertTask(ListNode previous, ListNode current, Edge toAdd) {
		// Case when you're at end of list, or toAdd is less than current
		if (current == null || this.compare(toAdd, current.edge) == -1) {
			previous.next = new ListNode(toAdd, current);
			this.size++;
			return;
		} else {
			insertTask(current, current.next, toAdd);
		}
	}
	
	public void printList() {
		ListNode next = this.dummy.next;
		while (next != null) {
			Edge edge = next.edge;
			if (edge.vertex1 < edge.vertex2) {
				System.out.printf("%4d", edge.vertex1);
				System.out.print(" ");
				System.out.printf("%4d", edge.vertex2);
			} else {
				System.out.printf("%4d", edge.vertex2);
				System.out.print(" ");
				System.out.printf("%4d", edge.vertex1);
			}
			next = next.next;
			System.out.println();
		}
	}
	
	public void outputList(PrintStream fileOutStream) {
		String sep = System.getProperty("line.separator");
		
		ListNode next = this.dummy.next;
		while (next != null) {
			Edge edge = next.edge;
			if (edge.vertex1 < edge.vertex2) {
				fileOutStream.printf("%4d", edge.vertex1);
				fileOutStream.print(" ");
				fileOutStream.printf("%4d", edge.vertex2);
			} else {
				fileOutStream.printf("%4d", edge.vertex2);
				fileOutStream.print(" ");
				fileOutStream.printf("%4d", edge.vertex1);
			}
			next = next.next;
			fileOutStream.print(sep);
		}
	}
	
	public int getSize() {
		return this.size;
	}
	
	public Edge get(int i) {
		if (i >= this.size) {
			throw new IllegalArgumentException("position exceeds list size");
		}
		int count = 0;
		ListNode next = dummy.next;
		while (count < i) {
			next = next.next;
			count++;
		}
		return next.edge;
	}
	
	public void printAdjacencyList(PrintStream fileOutStream) {
		
	}
	
}
