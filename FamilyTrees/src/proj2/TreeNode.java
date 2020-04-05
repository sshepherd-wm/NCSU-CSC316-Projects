package proj2;

import java.util.ArrayList;

public class TreeNode {
	public String element;
	public ArrayList<TreeNode> children;
	public TreeNode parent;
	public boolean mark;

	public TreeNode(String element, ArrayList<TreeNode> children, TreeNode parent, boolean mark) {
		this.element = element;
		this.children = children;
		this.parent = parent;
		this.mark = mark;
	}
}
