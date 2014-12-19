package uk.ac.cam.cl.dtg.sac92.oop.word_game.game_tree;

/**
 * Represents different levels of difficulty and associates them with a
 * fraction, depth and multiplier. 
 * 
 * @author Navid
 *
 */
public enum Difficulty {
	LEVEL_1 (0.5f, 4, 1),
	LEVEL_2 (0.4f, 5, 2),
	LEVEL_3 (0.3f, 6, 8),
	LEVEL_4 (0.2f, 7, 16),
	LEVEL_5 (0.1f, 8, 32),
	LEVEL_6 (0.01f, 10, 1024);
	
	// Roughly how rare the generated number should be.
	private final float fraction;
	// The depth to which the GameTree should be explored.
	private final int depth;
	// Score multiplier for a successful answer.
	private final int multiplier;
	
	/**
	 * Required constructor.
	 * @param fraction
	 * @param depth
	 */
	private Difficulty(float fraction, int depth, int multiplier)
	{
		this.fraction = fraction;
		this.depth = depth;
		this.multiplier = multiplier;
	}
	
	/**
	 * Gets the fraction.
	 * @return - The fraction.
	 */
	float fraction() {return fraction;}
	
	/**
	 * Gets the depth.
	 * @return - The depth.
	 */
	int depth() {return depth;}
	
	/**
	 * Gets the multiplier.
	 * @return - The multiplier
	 */
	public int multiplier() {return multiplier;}
	
	/**
	 * Turns a number to a difficulty choice.
	 * @param choice - The number.
	 * @return - The difficulty choice.
	 */
	public static Difficulty numToDif(int choice)
	{
		// Default difficulty is easy.
		Difficulty dif = Difficulty.LEVEL_1;
		switch(choice)
		{
		case 0:
			dif = Difficulty.LEVEL_1;
			break;
		case 1:
			dif = Difficulty.LEVEL_2;
			break;
		case 2:
			dif = Difficulty.LEVEL_3;
			break;
		case 3:
			dif = Difficulty.LEVEL_4;
			break;
		case 4:
			dif = Difficulty.LEVEL_5;
			break;
		case 5:
			dif = Difficulty.LEVEL_6;
			break;
		}
		
		return dif;
	}
	
}
