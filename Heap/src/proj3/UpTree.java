/**
 * 
 */
package proj3;

/**
 * @author Stephen Shepherd
 *
 */
public class UpTree {
	private int[] array;
	private int size;
	
	public UpTree(int maxSize) {
		array = new int[maxSize];
		size = maxSize;
		// Initialize all positions to contain -1
		// Each will be it's own singleton set/tree to start, so it will be the root
		for (int i = 0; i < maxSize; i++) {
			array[i] = -1;
		}
	}
	
	/**
	 * Returns root of tree of node at passed position
	 * @param position
	 * @return
	 */
	public int find(int position) {
		int val = array[position];
		if (val < 0) {
			// This means that the position is a root node
			return position;
		} else {
			// Otherwise, find starting at position of parent
			return find(val);
		}
	}
	
	/**
	 * Returns position of root of new tree
	 * @param a
	 * @param b
	 * @return
	 */
	public int union(int a, int b) {
		int aTree = find(a);
		int bTree = find(b);
		int newRoot = 0;
		
		if (array[aTree] * -1 >= array[bTree] * -1) {
			// In balanced union, B needs to become child of A
			// Adjusting size of A
			array[aTree] = array[aTree] + array[bTree];
			// Replacing value at B with new parent (A)
			array[bTree] = aTree;
			newRoot = aTree;
		} else {
			// In balanced union, B needs to become child of A
			// Adjusting size of B
			array[bTree] = array[aTree] + array[bTree];
			// Replacing value at B with new parent (A)
			array[aTree] = bTree;
			newRoot = bTree;
		}
		
		return newRoot;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void printArray() {
		for (int i = 0; i < this.size; i++) {
			System.out.printf("%3d", array[i]);
		}
		System.out.println();
	}
	
	
}
