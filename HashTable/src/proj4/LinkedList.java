/**
 * 
 */
package proj4;

/**
 * @author Stephen Shepherd
 *
 */
public class LinkedList {
	
	private ListNode dummy = new ListNode(null, null);
	private int size;
	
	public LinkedList() {
		size = 0;
	}
	
	public void insert(String element) {
		//System.out.println("Inserting: " + element);
		// Do not insert if element already in list
		if (this.contains(element) > 0) {
			return;
		}
		// Insert at front
		dummy.next = new ListNode(element, dummy.next);
		size++;
	}
	
	/**
	 * Main function of the list
	 * Returns positive number of comparisons if element is found
	 * Returns negative number of comparisons if not found
	 * @param element
	 * @return
	 */
	public int contains(String element) {
		// Count of comparisons
		int count = 0;
		
		ListNode previous = dummy;
		ListNode next = dummy.next;
		
		while (next != null) {
			count++;
			if (next.element.equals(element)) {
				// Move to front
				previous.next = next.next;
				dummy.next = new ListNode(element, dummy.next);
				// Return positive count
				return count;
			}
			previous = next;
			next = next.next;
		}
		// Return negative count (not found)
		return count * -1;
	}
	
	public void printList() {
		ListNode current = dummy.next;
		while (current != null) {
			if (current.next != null) {
				System.out.print(current.element + ", ");
			} else {
				System.out.print(current.element);
			}
			current = current.next;
		}
		System.out.println();
	}
	
	private class ListNode {
		private String element;
		private ListNode next;
		
		public ListNode(String element, ListNode next) {
			this.element = element;
			this.next = next;
		}
	}
	
	public int getSize() {
		return this.size;
	}
	
}
