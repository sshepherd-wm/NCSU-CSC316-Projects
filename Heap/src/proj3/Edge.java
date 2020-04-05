/**
 * 
 */
package proj3;

/**
 * @author Stephen Shepherd
 *
 */
public class Edge {
	
	public int vertex1;
	public int vertex2;
	public double weight;
	public Edge next;

	/**
	 * 
	 */
	public Edge(int vertex1, int vertex2, double weight, Edge next) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.weight = weight;
		this.next = next;
	}

}
