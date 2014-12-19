package uk.ac.cam.cl.dtg.sac92.oop.word_game.grid;

/**
 * Tile Class
 *
 * This class represents a tile in the game grid.
 * 
 * @author Stephen Cummins
 * @version 1.0 Released 11/10/2005
 */
public class Tile implements Cloneable{
	private char letter;
	private boolean active;

	/**
	 * Creates a Tile with a given letter and value.
	 * 
	 * @param letter
	 *            - label for letter tile e.g. 'A'
	 * @param value
	 *            - value e.g. points.
	 */
	public Tile(char letter) {
		this.letter = letter;
		active = false;
	}

	/**
	 * Get a letter
	 * 
	 * @return the letter
	 */
	public char letter() {
		return letter;
	}


	/**
	 * Changes the active state for the tile.
	 * 
	 * @param value
	 *            - new state.
	 */
	public void active(boolean value) {
		active = value;
	}

	/**
	 * Is the current tile active.
	 * 
	 * @return true if active, false if not.
	 */
	public boolean checkActive() {
		return active;
	}
	
	/**
	 * Does the current tile have an operator as it's letter. 
	 * 
	 * @return true if it is an operator, false if it is a number.
	 */
	public boolean isOperator() {
		return (letter == '+') || (letter == '*') ||
			   (letter == '%') || (letter == '-') ? true : false;
	}

	@Override
	public String toString() {
		return "(" + letter + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + letter;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Tile))
			return false;
		Tile other = (Tile) obj;
		if (active != other.active)
			return false;
		if (letter != other.letter)
			return false;
		return true;
	}
	
	/**
	 * Performs a deep copy of a tile. Decided to use super.clone() just to
	 * test it out. 
	 */
	@Override
	public Object clone() 
	{
		try{
		// Note all state is primitive so no need to deep copy reference types.
		return (Tile) super.clone();
		} catch (CloneNotSupportedException e) 
		{
			// We should not ever be here as we implement Cloneable.
			// It is better to deal with the exception here rather than letting
			// clone throw it as we would have to catch it in more places then.
			return null;
		}
	}
}
