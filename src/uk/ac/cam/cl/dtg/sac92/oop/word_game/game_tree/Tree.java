package uk.ac.cam.cl.dtg.sac92.oop.word_game.game_tree;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Represents a tree.
 * Wraps the root node and provides some tree based algorithms.
 * @author Navid
 *
 * @param <T>
 */
public class Tree<T> 
{
	private Node<T> root;
	
	/**
	 * Constructor - Initialises the root with the value T.
	 * @param value - Initial value of root node.
	 */
	public Tree(T value)
	{
		root = new Node<T>(value);
	}
	 
	/**
	 * Gets the child nodes of the root.
	 * @return - The child nodes. 
	 */
	public ArrayList<Node<T>> getChildren()
	{
		return root.getChildren();
	}
	
	/**
	 * Gets the root node.
	 * @return - The root node.
	 */
	public Node<T> getRoot()
	{
		return root;
	}
	
	/**
	 * Sets the root node to a specified value.
	 * @param value - The value.
	 */
	public void setRoot(T value)
	{
		root.SetValue(value);
	}
	
	/**
	 * Sets the child nodes.
	 * @param children - The child nodes.
	 */
	public void setChildren(ArrayList<Node<T>> children)
	{
		root.setChildren(children);
	}
	
	/**
	 * Counts the number of nodes in the tree.
	 * @return - The number of nodes. 
	 */
	public int count()
	{
		return root.count();
	}
	
	/**
	 * Performs a breadth first traversal of the supplied tree to a specified
	 * depth and returns the result as an ArrayList.
	 * @param depth - The depth to traverse down to.
	 * @param tree - The tree to traverse.
	 * @return - ArrayList of T obtained by the traversal. 
	 */
	public ArrayList<T> BreadthFirstSearch(int depth, Tree<T> tree)
	{
		LinkedList<Node<T>> workQueue = new LinkedList<Node<T>>();
		workQueue.offer(tree.getRoot());
		// Use the internal implementation of BFS with a queue just containing
		// the root node to start. 
		return BreadthFirstSearch(depth, workQueue);
	}
	
	/**
	 * Actual implementation of the BreadthFirstSearch.
	 * @param depth - The depth to traverse down to.
	 * @param workQueue - The queue to process. 
	 * @return - ArrayList of T obtained by traversal. 
	 */
	private ArrayList<T> BreadthFirstSearch(
			int depth, LinkedList<Node<T>> workQueue)
	{
		ArrayList<T> result= new ArrayList<T>();
		LinkedList<Node<T>> newWorkQueue = new LinkedList<Node<T>>();
		
		// If we have reached the end of the tree or the depth limit then stop.
		if ((workQueue.isEmpty()) || (depth == 0))
			return result;
		
		// Process each node in the work queue,
		// using its children to make the new queue.
		for(Node<T> n : workQueue) {
			result.add(n.getValue());
			for(Node<T> nc : n.getChildren())
				newWorkQueue.offer(nc);
		}
		
		// Recursively create the result.
		result.addAll(BreadthFirstSearch(depth-1, newWorkQueue));
		return result;
	}
}
