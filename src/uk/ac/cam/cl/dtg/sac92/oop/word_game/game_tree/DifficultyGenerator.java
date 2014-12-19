package uk.ac.cam.cl.dtg.sac92.oop.word_game.game_tree;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;

/**
 * This class generates numbers for the player to try and make based on the 
 * chosen difficulty, currently it generates a number of sufficient rarity 
 * based on the position it is placed in the frequency sorted list 
 * generated in GameTree by applying BFS to a IntTree (from a GameTree).
 * However, it is easily extensible to allow for other types of games e.g. 
 * generate the most positive number possible in x moves as the GameTree 
 * provides a convenient way to search the space of GameStates.
 *  
 * @author Navid
 *
 */
public abstract class DifficultyGenerator {
	
	private static int requiredNumber;
	
	private DifficultyGenerator(){}
	
	public static void generateRequiredNumber(Difficulty dif, GameState gs)
	{
		// Get difficulty as defined in the enumeration type.
		int depth = dif.depth();
		float fraction = dif.fraction();
		
		// Generate the IntTree by generating the GameTree.
		Tree<Integer> intTree =
				GameTree.extractIntTree(new GameTree(gs, depth));
		
		// Get the numbers in the IntTree sorted by frequency
		// so less frequent first.
		ArrayList<Integer> sortedByFreq = GameTree.sortByFrequency(
					intTree.BreadthFirstSearch(depth, intTree));
		
		// Remove duplicates.
		ArrayList<Integer> noDuplicates = new ArrayList<Integer>(
					new LinkedHashSet<Integer>(sortedByFreq));
		
		// Gets you the most difficult required value if on max difficulty.
		if(dif.depth() == 10) {
			requiredNumber = noDuplicates.get(0);
			return;
		}
		
		// Use random numbers to allow consecutive games to have different 
		// required values.
		Random random = new Random();
			
		// Pick a index with an average (Expectation) of 
		// fraction * noDuplicates.size().
		int index = (int)(fraction * (float) noDuplicates.size());
		index = random.nextInt(index) + index/2;
		requiredNumber = noDuplicates.get(index);
	}
	
	/**
	 * Gets the required value.
	 * @return - The required value.
	 */
	public static int getRequiredNumber(){return requiredNumber;}
}
