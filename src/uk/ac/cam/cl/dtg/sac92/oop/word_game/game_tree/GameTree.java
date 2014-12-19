package uk.ac.cam.cl.dtg.sac92.oop.word_game.game_tree;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Grid;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Tile;

/**
 * Represents potential moves from the GameState stored in the root node as a
 * tree and provides various methods to construct and process this tree.
 * 
 * @author navid
 *
 */
public class GameTree extends Tree<GameState> {

	/**
	 * Constructor for GameTree, builds a tree of a specified depth from the 
	 * given GameState.
	 * 
	 * @param gs - Initial GameState.
	 * @param depth - Depth to build to.
	 */
	public GameTree(GameState gs, int depth) {
		super(gs);
		Node<GameState> root = this.getRoot();
		ArrayList<Node<GameState>> children = new ArrayList<Node<GameState>>();
		
		// Special case for initial states. 
		children = initialStates(root.getValue());
		root.addChildren(children);
		
		// Construct the tree, note depth-2 is due to us already having the top
		// two levels of the tree built, i.e. root-children. 
		for(Node<GameState> n : children)
			buildTree(n, depth-2);
	}
	
	/**
	 * Builds a tree of a specified depth from the GameState supplied.
	 * 
	 * @param node - GameState to build from.
	 * @param depth - Depth required. 
	 */
	public static void buildTree(Node<GameState> node, int depth)
	{
		if(depth == 0)
			return;
		
		// Calculate the next GameStates from this node and add as children.
		node.addChildren(wrapInNodes(
				GameState.nextGameStates(node.getValue())));
		// Build a tree of one less depth for each of the children.
		for(Node<GameState> n : node.getChildren())
			buildTree(n, depth-1);
	}
	
	/**
	 * Generates all the possible GameStates at the start of a game, i.e. all 
	 * the tiles that do not hold operators are potential first moves.
	 *  
	 * @param gs - The initial GameState to work from (zero items on the path).
	 * @return - The array of initial GameStates.
	 */
	private static ArrayList<Node<GameState>> initialStates(GameState gs)
	{
		ArrayList<Node<GameState>> initialStates = 
				new ArrayList<Node<GameState>>();
		Grid grid = gs.getGrid();
	
		// For each tile in the grid.
		for(int y = 0; y < grid.height(); y++)
		{
			for(int x = 0; x < grid.width(); x++)
			{
				Point p = new Point(x,y);
				Tile t = (Tile)grid.tileAt(p).clone();
				// If it is not an operator it is a viable first move.
				if(!t.isOperator())
				{
					Grid newGrid = (Grid)gs.getGrid().clone();
					ArrayList<Tile> newPath = new ArrayList<Tile>();
					
					newPath.add(t);
					t.active(true);
					
					// Required as Grid.positionOf requires reference equality.
					newGrid.setTileAt(p, t);
					
					// Add the new GameState.
					initialStates.add(new Node<GameState>(
							new GameState(newGrid, newPath)));
				}
			}
		}
		
		return initialStates;
	}
	/**
	 * Converts an ArrayList of GameStates into a ArrayList of Nodes of
	 * GameStates. Just puts each GameState into a Node<GameState>. 
	 * 
	 * @param gsList - ArrayList to convert.
	 * @return - Converted ArrayList.
	 */
	private static ArrayList<Node<GameState>> wrapInNodes(
			ArrayList<GameState> gsList)
	{
		ArrayList<Node<GameState>> wrappedGs =
				new ArrayList<Node<GameState>>();
		for(GameState gs: gsList)
			wrappedGs.add(new Node<GameState>(gs));
		
		return wrappedGs;
	}
	
	/**
	 * Turns a gameTree into a tree of integers based on the values of the path
	 * at each node of the gameTree.
	 * 
	 * @param gt - GameTree to convert.
	 * @return - Converted Integer Tree.
	 */
	public static Tree<Integer> extractIntTree(GameTree gt)
	{
		Tree<Integer> intTree = new Tree<Integer>(0);
		// This function traverses the entire GameTree and constructs intTree.
		gameNodesToIntNodes(gt.getRoot(), intTree.getRoot());
		return intTree;
	}
	
	/**
	 * Traverses a tree of GameStates and calculates the value of the path at  
	 * each node and uses a tree of integers to hold this.
	 * 
	 * @param ngs - The GameStates
	 * @param intNode - Holds the path values.
	 */
	private static void gameNodesToIntNodes(
			Node<GameState> ngs, Node<Integer> intNode)
	{
		// Compute value of path at the current node.
		int result = pathToValue(ngs.getValue().getPath());
		intNode.SetValue(result);
		int childrenSize = ngs.getChildren().size();
		
		// Apply to each child node.
		for(int i = 0; i < childrenSize; i++)
		{
			// Initialise the children of intNode with 0's.
			intNode.addChild(new Node<Integer>(0));
			gameNodesToIntNodes(ngs.getChildren().get(i),
					intNode.getChildren().get(i));
		}
	}
	
	/**
	 * Turns an ArrayList of tiles into an value.
	 * @param tilePath - The tiles to convert. 
	 * @return - The value.
	 */
	private static int pathToValue(ArrayList<Tile> tilePath)
	{
		int length = tilePath.size();
		if(length == 0)
			return 0;
		
		// Set the accumulator to the first integer in the path.
		int accumulator = Integer.parseInt(
				String.valueOf(tilePath.get(0).letter()));
		char currentChar = 0;
		char currentOp = 0;
		
		// Visit each element in the path and
		// compute it's effect on the accumulator.
		for(int i = 1; i < length; i++) 
		{
			currentChar = tilePath.get(i).letter();
			switch(currentChar)
			{
			// If it is an operator.
			case '*':
			case '%':
			case '+':
			case '-':
				currentOp = currentChar;
				break;
			default:
				int currentNumber = Integer.parseInt(
						String.valueOf(currentChar));
				// If a number is found apply the last operator
				// to it and the accumulator.
				switch (currentOp)
				{
				case '*':
					accumulator *= currentNumber;
					break;
				case '%':
					if(currentNumber == 0) {
						accumulator = 0;
					} else {
						accumulator %= currentNumber;
					}
					break;
				case '+':
					accumulator += currentNumber;
					break;
				case '-':
					accumulator -= currentNumber;
					break;
				}
			}
		}
		
		return accumulator;
	}
	
	/**
	 * Takes a list of numbers (obtained from BFS on the extracted IntTree) and
	 * returns a list of the same length but with the numbers sorted by the 
	 * frequency of their appearance in ascending order.  
	 * @param intList
	 * @return
	 */
	 static ArrayList<Integer> sortByFrequency(ArrayList<Integer> intList)
	{
		// Use point to store (number, frequency) pair. 
		ArrayList<Point> numberCountPairs = new ArrayList<Point>();
		
		ArrayList<Integer> sortedByFreq = new ArrayList<Integer>();
		int counter = 0;
		
		Collections.sort(intList);
		
		numberCountPairs.add(new Point(intList.get(0).intValue(), 1));
		
		for(int i = 1; i < intList.size(); i++)
		{
			if(intList.get(i).equals(intList.get(i-1))) {
				// If the next number == previous number
				// then increment the frequency.
				numberCountPairs.get(counter).y++;
			} else {
				// Otherwise create a new entry for the next number. 
				counter++;
				numberCountPairs.add(new Point(intList.get(i).intValue(), 1));
			}
		}
		
		// Sort pairs by frequency.
		Collections.sort(numberCountPairs, new Comparator<Point>() {
			@Override
			public int compare(Point p1, Point p2)
			{
				return p1.y - p2.y;
			}
		});
		
		
		// Use the (number, frequency) pairs to populate return array. 
		for(Point p : numberCountPairs) 
			for(int i = 0; i < p.y; i++)
				sortedByFreq.add(new Integer(p.x));
		
		return sortedByFreq;
	}
}
