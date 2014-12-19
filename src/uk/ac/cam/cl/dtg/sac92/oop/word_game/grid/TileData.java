package uk.ac.cam.cl.dtg.sac92.oop.word_game.grid;

/**
 * Tile Data Interface
 *
 * This interface provides the information needed to generate the letters on the
 * face of the tiles and their associated scores.
 * 
 * You should not change this interface.
 * 
 * @author Stephen Cummins
 * @version 1.0 Released 11/10/2005
 */
public interface TileData {

	// These hold the contents of tiles.
	final static char[] CONTENT = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+', '-', '*', '%'};
	
	// Hold the relative frequencies of the tile contents, note we require 
	// grid.height()*grid.width() <= FREQUENCY.length() to populate the entire grid.
	final static int[] FREQUENCY = { 8, 4, 4, 4, 4, 4, 4, 4, 4, 4, 8, 8, 8, 8 };
	
}
