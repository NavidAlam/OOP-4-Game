package uk.ac.cam.cl.dtg.sac92.oop.word_game.game_tree;

import java.util.ArrayList;
/**
 * Represents a node with an arbitrary number of children. 
 * 
 * @author Navid
 *
 * @param <T> - Parameter of Node. 
 */
public class Node<T> {
	
	private T value;
	private ArrayList<Node<T>> children;
	
	/**
	 * Constructor - Sets the value and initialises child nodes. 
	 * @param value
	 */
	public Node(T value)
	{
		this.value = value;
		children = new ArrayList<Node<T>>();
	}
	
	/**
	 * Gets the value of the node.
	 * @return - Value.
	 */
	public T getValue() {return value;}
	
	/**
	 * Sets the value of the node.
	 * @param value - Value.
	 */
	public void SetValue(T value) {this.value = value;}
	
	/**
	 * Gets the child nodes.
	 * @return - Child nodes.
	 */
	public ArrayList<Node<T>> getChildren()
	{
		return children;
	}
	
	/**
	 * Sets the child nodes.
	 * @param children - Child nodes.
	 */
	public void setChildren(ArrayList<Node<T>> children)
	{
		this.children = children;
	}
	
	/**
	 * Adds a child node to the end of the current child nodes.
	 * @param child - Child node to add. 
	 */
	public void addChild(Node<T> child)
	{
		children.add(child);
	}
	
	/**
	 * Adds multiple child nodes to the end of the current child nodes.
	 * 
	 * @param children - Child nodes to add.
	 */
	public void addChildren(ArrayList<Node<T>> children)
	{
		this.children.addAll(children);
	}
	
	/**
	 * Counts the nodes in the tree.
	 * 
	 * @return - Total nodes in the tree.
	 */
	public int count()
	{
		int count = 1;
		
		if(children.size() != 0) {
			for(Node<T> n : children)
				count+= n.count();
		}
		
		return count;
	}
	
}
