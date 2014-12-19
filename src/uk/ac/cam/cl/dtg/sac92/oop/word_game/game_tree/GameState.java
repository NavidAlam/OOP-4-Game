package uk.ac.cam.cl.dtg.sac92.oop.word_game.game_tree;

import java.awt.Point;
import java.util.ArrayList;

import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Grid;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Tile;

/**
 * Represents the state of a game board. The main method it provides is used to
 * generate GameTrees. 
 * 
 * @author navid
 *
 */
public class GameState {
	private Grid grid;
	private ArrayList<Tile> path;
	
	/**
	 * 	Constructor of GameState. Works how you would expect.
	 *  
	 * @param grid - Current grid.
	 * @param path - Current path.
	 */
	public GameState(Grid grid, ArrayList<Tile> path)
	{
		this.grid = grid;
		this.path = path;
	}
	
	/**
	 * Generate an ArrayList of all the possible GameStates reachable in one 
	 * move from a specified GameState.
	 * 
	 * @param gs - Given GameState
	 * @return - All possible GameStates reachable in one move from gs.
	 */
	static ArrayList<GameState> nextGameStates(GameState gs)
	{
		ArrayList<GameState> nextGameStates = new ArrayList<GameState>();
		ArrayList<Grid> clonedGrids = new ArrayList<Grid>();
		ArrayList<Tile> potentialMoves = new ArrayList<Tile>();
		
		int pathLength = gs.getPath().size();
		// This is the tile we have to move from.
		Tile lastTile = gs.getPath().get(pathLength-1);
		
		int movesLength = 0;
		
		Point currentPoint = new Point();
		currentPoint = gs.getGrid().positionOf(lastTile);
	
		// For each tile in the grid in gs. 
		for(int x = 0; x < gs.getGrid().width(); x++)
		{
			for(int y = 0; y < gs.getGrid().height(); y++)
			{
				// Get the tile at that position.
				Point p = new Point(x,y);
				Tile gridTile = gs.getGrid().tileAt(p);
				
				/* We need to satisfy four conditions for it to be a valid move
				 * -The tiles have to be adjacent (isNeighbour)
				 * -The gridTile needs to be inactive (checkActive)
				 * -Both the gridTile and the lastTile must not both have an 
				 *  operator or number (isOperator !=...)
				 * -The gridTile cannot be the same as the lastTile (p.x == ..)
				 */ 
				if((Grid.isNeighbour(lastTile, gridTile, gs.getGrid())) &&
				   (!gridTile.checkActive()) && 
				   (gridTile.isOperator() != lastTile.isOperator())) {
						if((p.x == currentPoint.x) && (p.y == currentPoint.y))
							continue;
						potentialMoves.add(gridTile);
					}	
			}
		}
		
		movesLength = potentialMoves.size();
		
		// For each potential move. 
		for(int i = 0; i < movesLength; i++) 
		{
			// Clone the potential tile/move.
			Tile potentialTile = potentialMoves.get(i);
			Tile clone = (Tile)potentialTile.clone();
			clone.active(true);
			Point p = new Point();
			
			p = gs.getGrid().positionOf(potentialTile);
			
			// Clone the old grids.
			clonedGrids.add((Grid)gs.getGrid().clone());
			
			ArrayList<Tile> newPath = new ArrayList<Tile>();
			
			// Clone the old path to the new path.
			for(Tile t : gs.getPath())
				newPath.add((Tile)t.clone());
			
			// Add on the new move.
			newPath.add(clone);
			
			
			// Ensure the last item in the path has the same reference as the
			// corresponding item on the grid, this ensures positionOf works.
			clonedGrids.get(i).setTileAt(p, clone);
			
			nextGameStates.add(new GameState(clonedGrids.get(i), newPath));
		}
		
		return nextGameStates;
	}

	/**
	 * Gets the path.
	 * 
	 * @return - The path.
	 */
	ArrayList<Tile> getPath()
	{
		return path;
	}
	
	
	/**
	 * Gets the grid.
	 * 
	 * @return - The grid.
	 */
	Grid getGrid()
	{
		return grid;
	}

}
