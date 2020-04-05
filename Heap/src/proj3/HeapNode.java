/**
 * 
 */
package proj3;

/**
 * @author Stephen Shepherd
 *
 */
public class HeapNode {
	
	public Edge element;
	double key;

	/**
	 * 
	 */
	public HeapNode(Edge element, int key) {
		this.element = element;
		// Setting keys to be the weight of the edge
		this.key = this.element.weight;
	}

}
