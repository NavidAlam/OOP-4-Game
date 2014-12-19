package uk.ac.cam.cl.dtg.sac92.oop.word_game;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import uk.ac.cam.cl.dtg.sac92.oop.word_game.game_tree.Difficulty;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.game_tree.DifficultyGenerator;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.game_tree.GameState;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Grid;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.GridGUI;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Tile;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.TileCollection;

import java.awt.GridLayout;
import java.awt.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This represents a number game. It approximates a singleton pattern.
 * 
 * The player is provided with a grid of numbers and operators that they can
 * click. The idea is that the player chooses a path composed of adjacent  
 * tiles and so construct an expression with a certain value. The aim is to 
 * create a required value. The difficulty level determines the rarity of the
 * required value. 
 * 
 * A significant (internal) part of the program is the generation of GameTrees 
 * which are trees that contain the game at a particular moment (represented as
 * a GameState) as nodes and the children of each node are all the potential 
 * GameStates available by making a move at the node. This is used to determine
 * which values are reachable (within a depth limit) and breadth first search 
 * is used to find the reachable values. 
 * 
 * Graphically we have a region that displays the current game info i.e. the 
 * current value, required value, current expression, score. In addition we 
 * have three self explanatory buttons: SubmitCurrentValue, ShuffleGrid and
 * ClearExpression. On the grid we have inactive tiles represented by blue and
 * active tiles by differing shades of red with the more recently clicked tiles
 * being a brighter shade of red to help the player with the order of their 
 * path. If the player clicks an active tile on the path it deactivates and 
 * also deactivates all the tiles added after it (and recomputes the value).
 * 
 * 
 * @author Stephen Cummins & Navid
 * @version 1.0 Released 11/10/2005
 */
public class Game extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static GridGUI gui;
	
	// Swing components.
	private static JFrame frame;
	
	private static JPanel rightPanel;
	private static JPanel gameInfo;
	private static JPanel controls;
	
	private static JLabel scoreLabel;
	private static JLabel requiredValueLabel;
	private static JLabel currentValueLabel;
	private static JLabel currentExpressionLabel;
	
	// Game variables.
	private static Grid grid;
	private static ArrayList<Tile> path = new ArrayList<Tile>();
	private static TileCollection collection;
	private static int score = 0;
	private static int value = 0;
	private static int requiredValue;
	private static String expression = ""; 
	private static Difficulty difficulty;
	
	// Constants.
	private static final int X_TILE_COUNT = 8;
	private static final int Y_TILE_COUNT = 8;
	private static final int SCORE_CONST = 1000;
	

	/**
	 * Private constructor to implement singleton pattern.
	 */
	private Game() {}
	
	
	/**
	 * Analogous to  getInstance in a typical singleton pattern.
	 */
	public static void startGame()
	{
		if(frame == null)
			buildGUI(false);
		frame.toFront();
	}
	
	/**
	 * Opens a dialog to get the user's choice of difficulty. Then calls for
	 * the requiredValue to be updated.
	 * 
	 */
	public static void getDifficultyChoice()
	{
		// Difficulty labels.
		ArrayList<String> choices = new ArrayList<String>(
				Arrays.asList("EZ PZ", "No Sweat", "Average", "Tryhard", "MLG",
						"Heap masher"));
		
		String response = "";
		int index = -1;
		
		// Ensure user sets a difficulty.
		while(true)
		{
			response = (String)JOptionPane.showInputDialog(frame,
						"Choose your difficulty level: ", "Difficulty chooser",
						JOptionPane.PLAIN_MESSAGE, null, choices.toArray(),
						choices.get(0));
			
			// If the response is valid.
			if((response != null) && (response.length() > 0))
				break;
		}
		
		index = choices.indexOf(response);
		
		// Adjust the game to the newly chosen difficulty.
		difficulty = Difficulty.numToDif(index);
		
		requiredValue = generateNextRequiredValue();
		
		requiredValueLabel.setText("Required value: " + requiredValue);
	}
	

	/**
	 * This method will construct each element of the game's GUI
	 * @param collectionSupplied - Specifies if we need to make new collection.
	 */
	private static void buildGUI(boolean collectionSupplied) {
		
		// Instantiate swing components. 
		frame = new JFrame("Java Word Game");
		if(!collectionSupplied) {
			collection = new TileCollection();
		}
		
		grid = new Grid(X_TILE_COUNT, Y_TILE_COUNT, collection);
		gui = new GridGUI(grid);
		
		gui.setTileForeground(Color.yellow);
		gui.setTileBackground(Color.blue);
		
		// Applying borders to the right panel components. 
		rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(3,1, 10, 10));

		gameInfo = new JPanel();
		gameInfo.setLayout(new GridLayout(3,1, 10, 10));
		
		controls = new JPanel();
		controls.setLayout(new GridLayout(1,3, 10, 10));
				
		controls.setBorder(new EmptyBorder(10,10,10,10));
		

		JButton submitButton = new JButton("Submit Current Value");
		JButton shuffleButton = new JButton("Shuffle Grid");
		JButton clearButton = new JButton("Clear expression");

		// Setting initial label values/alignment.
		scoreLabel = new JLabel("Your score is: " + score);
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		
		requiredValueLabel = new JLabel("You need to make: ");
		requiredValueLabel.setHorizontalAlignment(JLabel.CENTER);
		
		currentValueLabel = new JLabel("Current value: 0");
		currentValueLabel.setHorizontalAlignment(JLabel.CENTER);
		
		currentExpressionLabel = new JLabel("Current expression: ");
		currentExpressionLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// Adding components to the JPanels. 
		gameInfo.add(currentExpressionLabel);
		gameInfo.add(currentValueLabel);
		gameInfo.add(requiredValueLabel);
		
		controls.add(submitButton);
		controls.add(shuffleButton);
		controls.add(clearButton);
		controls.setVisible(true);
		
		// Adding ActionListeners. 
		gui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				Tile source = (Tile) actionEvent.getSource();
				tileClick(source);
			}
		});
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				submitValue();
			}
		});
		
		shuffleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				shuffleTiles();
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				clearExpression();
			}
		});
		
		frame.setTitle("Java Number Game");
		
		// Sets layout of the game as a 1*2 grid.
		frame.getContentPane().setLayout(new GridLayout(1,2));
		
		// Finish the rightPanel.
		rightPanel.setVisible(true);
		rightPanel.add(scoreLabel);
		rightPanel.add(gameInfo);
		rightPanel.add(controls);
		
		// Add board and rightPanel to the main JFrame.
		frame.getContentPane().add(gui);
		frame.getContentPane().add(rightPanel);
		
		frame.pack();
		frame.setResizable(false);
		frame.toFront();

		frame.setBackground(Color.lightGray);
		frame.setVisible(true);
		
		// Get the initial choice of difficulty from the player.
		getDifficultyChoice();
	}
	
	/**
	 * Processes player clicks on the board. If an active (previously clicked)
	 * tile is clicked again then it removes it from the path and removes all
	 * the tiles following it in the path. It also recomputes the game info for
	 * this reduced path. If an inactive tile is clicked then it adds it to the
	 * end of the path and recomputes the game info.
	 * 
	 * @param source - Tile clicked.
	 */
	private static void tileClick(Tile source)
	{
		// Catch the case where the player has not picked any tiles yet and
		// tries to pick an operator.
		if((path.size() == 0) && source.isOperator()) {
			JOptionPane.showMessageDialog(frame, "You cannot start an " +
					"expression with an operator.", "Try again!", 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(source.checkActive()) {
			// We have to remove the tile from the path and also any tiles that
			// were added after it, then we have to recompute value and the 
			// current expression. 
			boolean found = false;
			int indexToRemoveFrom = -1;
			int pathLength = path.size();
			
			// Find tile in path, using reference equality comparison. 
			for(int i = 0; i <= pathLength; i++)
			{
				if(!found) {
					if(path.get(i) == source) {
						found = true;
						indexToRemoveFrom = i;
					}
				} else {
					Tile deselected = path.get(indexToRemoveFrom);
					
					// De-select tiles.
					gui.setTileBackground(grid.positionOf(deselected),
							Color.blue);
					gui.setTileForeground(grid.positionOf(deselected),
							Color.yellow);
					
					deselected.active(false);
					
					// .remove shifts elements to the left so we can keep
					// deleting from the same index. 
					path.remove(indexToRemoveFrom);
				}
			}
			
			// Update game info.
			expression = pathToExpression(path);
			value = expressionToValue(expression);
			currentValueLabel.setText("Current value: " + value);
			
			// Re draw tiles. 
			gui.invalidate();
			
		} else {
			if(path.size() != 0) {
				// Get last tile in expression.
				Tile previous = path.get(path.size()-1);
				// If they are adjacent and not both numbers/operators. 
				if(!Grid.isNeighbour(source, previous,grid)) {
					// Player has picked non-adjacent tiles.
					JOptionPane.showMessageDialog(frame,
							"Your tiles must be adjacent.",
							"Try again!", JOptionPane.ERROR_MESSAGE);
				} else {
					if(!(source.isOperator() != previous.isOperator())) {
						// Player has not created a valid expression.
						JOptionPane.showMessageDialog(frame,
								"There cannot be two of these in a row.",
								"Try again!", JOptionPane.ERROR_MESSAGE);
					} else {
						// Player has made a valid tile choice.
						
						// Select tile.
						gui.setTileBackground(grid.positionOf(source),
								Color.red);
						gui.setTileForeground(grid.positionOf(source),
								Color.green);
						source.active(true);
					
						// Update game info.
						updateCurrentValue(source.letter());
						path.add(source);
					}
				}
			} else {
				// Special initial cases for value and expression.
				
				// Update game info.
				value = Integer.parseInt(String.valueOf(source.letter()));
				currentValueLabel.setText("Current value: " + value);
				expression = String.valueOf(source.letter());
				path.add(source);
				
				// Select tile.
				gui.setTileBackground(grid.positionOf(source), Color.red);
				gui.setTileForeground(grid.positionOf(source), Color.green);
				source.active(true);
				
			}
		}
		
		// Update graphical output. 
		colourPath();
		currentExpressionLabel.setText("Current expression: " + expression);
		frame.repaint();
	}
	
	/**
	 * Handles clicks on the submit button. It updates the player's score if
	 * their currentValue == requiredValue and resets the game, otherwise
	 * it displays a dialog informing the user their value is not right.
	 */
	private static void submitValue()
	{
		
		if(value != requiredValue) {
			// If the user has not submitted a correct value.
			JOptionPane.showMessageDialog(frame, "Not quite right :(",
					"Keep trying.", JOptionPane.PLAIN_MESSAGE);
		} else {
			// Calculate the score of the user's input path.
			int length = path.size();
			score += difficulty.multiplier() * SCORE_CONST / (1 + length/2);
			
			scoreLabel.setText("Your score is: " + score);
			
			// Update the game info.
			value = 0;
			currentValueLabel.setText("Current value: 0");
		
			requiredValue = generateNextRequiredValue();
			requiredValueLabel.setText("Required value: " + requiredValue);
			
			currentExpressionLabel.setText("Current expression: ");
			expression = "";
			
			// Deactivate tiles on the path.
			for(Tile t : path) 
			{
				t.active(false);
				gui.setTileBackground(grid.positionOf(t), Color.blue);
				gui.setTileForeground(grid.positionOf(t), Color.yellow);
			}
			
			path.clear();
		    frame.repaint();
		}

	}
	
	/**
	 * Generates the required value using the DifficultyGenerator class.
	 * @return
	 */
	private static int generateNextRequiredValue()
	{
		DifficultyGenerator.generateRequiredNumber(difficulty,
				new GameState(grid, path));
		
		return DifficultyGenerator.getRequiredNumber();
	}
	
	/**
	 * Handles clicks on the Shuffle Tiles button.
	 * Shuffles the tiles on the current board and resets the all the game info
	 * except for score. It calls buildGUI which causes the frame to 
	 * disappear momentarily. Note the tiles are passed implicitly to the new
	 * game GUI by collection.
	 */
	private static void shuffleTiles()
	{
		int length = Y_TILE_COUNT*X_TILE_COUNT;
		Tile[] shuffledTiles = new Tile[length];
		
		Random random = new Random();
		int swapIndex = 0;
		Tile temp;
		
		// Populate tiles from grid.
		for(int x = 0; x < X_TILE_COUNT; x++)
		{
			for(int y = 0; y < Y_TILE_COUNT; y++)
			{
				shuffledTiles[x+Y_TILE_COUNT*y] = grid.tileAt(new Point(x,y));
			}
		}
		
		// Fisher-Yates shuffle.
		for(int i = length - 1; i > 0; i--)
		{
			swapIndex = random.nextInt(i+1);
			temp = shuffledTiles[i];
			shuffledTiles[i] = shuffledTiles[swapIndex];
			shuffledTiles[swapIndex] = temp;
		}
		
		// Deactivate all the tiles.
		for(Tile t : shuffledTiles)
			t.active(false);
		
		// Reset game info.
		value = 0;
		expression = "";
		requiredValue = 0;
		path.clear();
		
		// Assign the shuffled grid to collection and start the game again.
		collection = new TileCollection(shuffledTiles);
		frame.dispatchEvent(new WindowEvent(frame,
				WindowEvent.WINDOW_CLOSING));
		buildGUI(true);
	}
	
	/**
	 * Handles clicks on the Clear Expression button. 
	 * Clears the current expression and resets game info and UI.
	 */
	private static void clearExpression()
	{
	
		// Deactivate tiles on the path.
		for(Tile t : path) 
		{
			gui.setTileBackground(grid.positionOf(t), Color.blue);
			gui.setTileBackground(grid.positionOf(t), Color.yellow);
			t.active(false);
		}
		
		// Reset game info and UI.
		path.clear();
		value = 0;
		currentValueLabel.setText("Current value: 0");
		expression = "";
		currentExpressionLabel.setText("Current expression: ");
	}
	
	
	/**
	 * Colours the path, with the newest one being reddest. This allows the
	 * user to see the order in which they clicked the tiles. 
	 */
	private static void colourPath()
	{
		int length = path.size();
		
		// Colour each tile based on it's position in path.
		for(int i = 0; i < length; i++) 
		{
			gui.setTileBackground(grid.positionOf(path.get(i)),
					new Color((float)i/length, 0.0f, 0.0f));
		}
		
		frame.repaint();
	}
	
	
	/**
	 * Updates value according to the letter on the newly picked tile.
	 * 
	 * @param c - The letter on the newly picked tile.
	 */
	private static void updateCurrentValue(char c) 
	{
		boolean isOperator = true;
		// If c is not an operator. 
		if (!((c=='*') || (c=='+') ||(c=='%') || (c=='-'))) {
			isOperator = false;
			// Get the value of the last thing in the path.
			char previousOperator = path.get(path.size()-1).letter();
			// Convert c into an integer.
			int currentNumber = Integer.parseInt(String.valueOf(c));
			
			switch(previousOperator) {
			case '*':
				value *= currentNumber;
				break;
			case '+':
				value += currentNumber;
				break;
			case '%':
				// Catching the mod 0 to avoid ArithmeticException.
				if(currentNumber == 0)
				{
					value = 0;
				} else {
					value %= currentNumber;
				}
				break;
			case '-':
				value -= currentNumber;
				break;
			}
		}
		
		currentValueLabel.setText("Current value: " + value);
		
		updateExpressionLabel(c, isOperator);
	}
	
	/**
	 * Updates the expression and expression label after the user adds a tile.
	 * 
	 * @param c
	 * @param isOperator
	 */
	private static void updateExpressionLabel(char c, boolean isOperator)
	{
		// First update the expression, then the expression label. 
		if(isOperator) {
			// Special cases when applying * or % to first value as there is
			// no ambiguity and so we omit the brackets. 
			switch(c) {
			case '*':
				if(expression.length() == 1) {
					expression = expression + "*";
				} else {
					expression = "(" + expression + ")*";
				}
				break;
			case '+':
				expression = expression + "+";
				break;
			case '%':
				if(expression.length() == 1) {
					expression = expression + "%";
				} else {
					expression = "(" + expression + ")%";
				}
				break;
			case '-':
				expression = expression + "-";
				break;
			}
		} else {
			expression = expression + String.valueOf(c);
		}
		
		currentExpressionLabel.setText("Current expression: " + expression);
	}
	
	/**
	 * Turns an ArrayList of tiles into an expression.
	 * @param tilePath - The tiles to convert. 
	 * @return - The expression.
	 */
	private static String pathToExpression(ArrayList<Tile> tilePath)
	{
		int length = tilePath.size();
		if(length == 0)
			return "";
		StringBuilder expressionBuilder = new StringBuilder();
		char currentChar = 0;
		
		for(int i = 0; i < length; i++) 
		{
			currentChar = tilePath.get(i).letter();
			switch(currentChar)
			{
			case '*':
			case '%':
				if(expressionBuilder.length() == 1) {
					expressionBuilder.append(currentChar);
				} else {
					expressionBuilder.insert(0, '(');
					expressionBuilder.append(')');
					expressionBuilder.append(currentChar);
				}
				break;
			case '+':
			case '-':
				expressionBuilder.append(currentChar);
				break;
			default:
				// If a number is found.
				expressionBuilder.append(currentChar);
			}
		}
		
		return expressionBuilder.toString();
	}
	
	
	/**
	 * Returns a value when given an expression of the form produced by the game.
	 * That is, (((...(val1 op1 val2) op2 val3) op3 val4)...).
	 * 
	 * @param s - The expression to be parsed.
	 * @return - The value s evaluates to.
	 */
	private static int expressionToValue(String s) 
	{
		char currentChar = 0;
		char currentOperation = 0;
		int accumulator = 0;
		boolean firstNumberSeen = false;
		
		// Parses the supplied expression from left to right.
		// The partial value is stored in accumulator.
		for(int i = 0; i < s.length(); i++)
		{
			currentChar = s.charAt(i);
			switch(currentChar)
			{
			case '(':
			case ')':
				continue;
			case '*':
				currentOperation = '*';
				break;
			case '+':
				currentOperation = '+';
				break;
			case '-':
				currentOperation = '-';
				break;
			case '%':
				currentOperation = '%';
				break;
			default:
				// A number has been found.
				int currentNumber = Integer.parseInt(String.valueOf(currentChar));
				if(!firstNumberSeen) {
					accumulator = currentNumber;
					firstNumberSeen = true;
				} else {
					// Applies the operators.
					switch(currentOperation) 
					{
					case '*':
						accumulator *= currentNumber;
						break;
					case '+':
						accumulator += currentNumber;
						break;
					case '%':
						// Catching mod 0 to avoid ArithmeticException.
						if(currentNumber == 0) {
							accumulator = 0;
						} else {
							accumulator %= currentNumber;
						}
						break;
					case '-':
						accumulator -= currentNumber;
						break;
					}
				}
			}
		}
		
		return accumulator;
	}
}