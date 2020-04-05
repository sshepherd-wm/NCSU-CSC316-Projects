/**
 * 
 */
package proj3;

/**
 * @author Stephen Shepherd
 *
 */
public class VertexList {
	// Dummy header
	private VertexNode dummy;
	private int size;
	
	public VertexList() {
		this.dummy = new VertexNode(-1, null);
		this.size = 0;
	}
	
	public int compare(int a, int b) {
		if (a < b) {
			return -1;
		} else if (a == b) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public void insert(int toAdd) {
		insertTask(dummy, dummy.next, toAdd);
		//System.out.println(toAdd.vertex1 + "_" + toAdd.vertex2 + "_" + toAdd.weight);
	}
	
	public void insertTask(VertexNode previous, VertexNode current, int toAdd) {
		// Case when you're at end of list, or toAdd is less than current
		if (current == null || this.compare(toAdd, current.vertex) == -1) {
			previous.next = new VertexNode(toAdd, current);
			this.size++;
			return;
		} else {
			insertTask(current, current.next, toAdd);
		}
	}
	
	public boolean contains(int vertex) {
		boolean contains = false;
		VertexNode next = dummy.next;
		while (next != null) {
			if (next.vertex == vertex) {
				contains = true;
				return contains;
			}
			next = next.next;
		}
		return contains;
	}
	
	public int deleteMin() {
		if (this.size < 1) {
			throw new IllegalArgumentException("Vertex list empty");
		}
		int ret = dummy.next.vertex;
		dummy.next = dummy.next.next;
		size--;
		return ret;
	}
	
	public void printList() {
		VertexNode next = dummy.next;
		while (next != null) {
			System.out.println(next.vertex);
			next = next.next;
		}
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int get(int i) {
		if (i >= this.size) {
			throw new IllegalArgumentException("position exceeds list size");
		}
		int count = 0;
		VertexNode next = dummy.next;
		while (count < i) {
			next = next.next;
			count++;
		}
		return next.vertex;
	}
	
}
