package uk.ac.cam.cl.dtg.sac92.oop.word_game.grid;

import java.awt.Point;
import java.util.NoSuchElementException;

/**
 * Grid Class This class represents a grid of tiles.
 * 
 * @author Stephen Cummins
 * @version 1.0 Released 11/10/2005
 */
public class Grid implements Cloneable{
	private Tile[][] gridRep;

	/**
	 * Constructor will generate a grid of a specified width and height and
	 * populate it with a collection of tiles.
	 * 
	 * @param width
	 * @param height
	 * @param tiles
	 */
	public Grid(int width, int height, TileCollection tiles) {
		if (width < 1 || height < 1) {
			throw new IllegalArgumentException("Grid:constructor(" + width + ", " + height + ", " + tiles
					+ "): grid must be at least 1x1 in size");
		} else if (width * height > tiles.size()) {
			throw new IllegalArgumentException("Grid:constructor(" + width + ", " + height + ", " + tiles
					+ "): not enough tiles to cover grid");
		} else {
			gridRep = new Tile[width][height];
			for (int y = 0; y < height; y++) {

				for (int x = 0; x < width; x++) {

					gridRep[x][y] = tiles.removeOne();
				}
			}
		}
	}

	/**
	 * TileAt This method will return the tile at a given position within the
	 * grid.
	 * 
	 * @param position
	 * @return a Tile
	 */
	public Tile tileAt(Point position) {
		if (position.x > this.width() || position.x < 0 || position.y > this.height() || position.y < 0) {
			throw new IllegalArgumentException("Grid:at(" + position + "): point out of bounds");
		} else {
			return gridRep[position.x][position.y];
		}

	}
	
	/**
	 * Sets a tile at a particular position.
	 * @param position - the position
	 * @param tile - the new tile
	 */
	public void setTileAt(Point position, Tile tile)
	{
		gridRep[position.x][position.y] = tile;
	}

	/**
	 * PositionOf This method will search for a given tile and tell you what the
	 * position of it is within the grid
	 * 
	 * @param tile
	 * @return a Point
	 */
	public Point positionOf(Tile tile) {
		for (int y = 0; y < this.height(); y++) {
			for (int x = 0; x < this.width(); x++) {
				if (gridRep[x][y] == tile) {
					return new Point(x, y);
				}
			}
		}
		throw new NoSuchElementException("Grid:of(" + tile + "): tile not contained in this grid");
	}

	/**
	 * Width This method will get the current width of the grid
	 * 
	 * @return int width
	 */
	public int width() {
		return gridRep.length;
	}

	/**
	 * Height This method will get the current height of the grid
	 * 
	 * @return int height
	 */
	public int height() {
		return gridRep[0].length;
	}
	
	/**
	 * Calculates if two tiles are adjacent.
	 * 
	 * @param t1 first tile
	 * @param t2 second tile
	 * @param grid the grid from which the tiles are from.
	 * @return Returns true if adjacent, false if not.
	 */
	public static boolean isNeighbour(Tile t1, Tile t2, Grid grid) 
	{
		Point p1 = grid.positionOf(t1);
		Point p2 = grid.positionOf(t2);
		
		// Differences in coordinates.
		int dx = p1.x - p2.x;
		int dy = p1.y - p2.y;
		
		// For adjacency we require |dx| and |dy| to be less than 2
		return (dx*dx < 2) && (dy*dy < 2) ? true : false;
	}
	
	/**
	 * Performs a deep copy of the grid. Easier to construct the object from
	 * scratch rather than use uglier try-catch blocks for super.clone().
	 */
	@Override 
	public Object clone()
	{
		int height = height();
		int width = width();
		Tile[] tiles = new Tile[height*width];
		TileCollection collection;
		
		// Deep copying ensured by cloning all reference types, i.e. Tiles 
		// which performs a deep copy for its clone. 
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++) 
			{
				tiles[x+height*y] = (Tile)gridRep[x][y].clone();
			}
		}
		
		collection = new TileCollection(tiles);
		
		return new Grid(width, height, collection);
	}
	
}
