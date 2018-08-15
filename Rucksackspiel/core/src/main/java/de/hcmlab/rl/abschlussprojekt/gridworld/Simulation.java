package de.hcmlab.rl.abschlussprojekt.gridworld;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.hcmlab.rl.abschlussprojekt.KnapsackGame;
import de.hcmlab.rl.abschlussprojekt.learning.LearningAlgorithm;
import de.hcmlab.rl.abschlussprojekt.solution.Brick;
import de.hcmlab.rl.abschlussprojekt.solution.Multiset;
import de.hcmlab.rl.abschlussprojekt.solution.State;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class responsible for setting a Brick on the game-grid and generating a reward.
 * 
 * @author Patrizia Schalk
 */
public class Simulation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simulation.class);		// A logger for the general output

    private LearningAlgorithm algorithm;	// The actor that tries to place Bricks in an intelligent manner

    private Table<Integer, Integer, GridCell> grid = HashBasedTable.create();			// A representation of the game-grid 
    private Multiset sack = new Multiset();	// A multiset containing all Bricks the actor is able to place
    
    private int cols, rows;					

    private boolean isOver;					// A boolean representing if the episode ends

    /**
     * Returns the GridCell at the position (x,y) with its respective type
     * 
     * @param x : the x-position of the desired GridCell
     * @param y : the y-position of the desired GridCell
     * @return the GridCell-object at position (x,y)
     */
    public GridCell getCell(int x, int y) {
        return grid.get(x, y);
    }
    
    /**
     * Returns whether the current episode is over
     * 
     * @return a boolean representing if the episode is over
     */
    public boolean isOver() {
        return isOver;
    }

    /**
     * Sets up the internal representation of the game-grid before any action is taken.
     * 
     * @param algorithm : the LearningAlgorithm that is able to choose an action and receive a reward
     * @param width : the width of the game-grid
     * @param height : the height of the game-grid
     * @param sack : the set of Bricks that can be placed when starting the game
     */
    public void setup(LearningAlgorithm algorithm, int width, int height, Multiset sack) {
        cols = width;
        rows = height;
        
        grid.clear();				// remove all GridCells of the game-grid
        this.sack = sack;			// save the set of Bricks in a global variable
        
        /* Error Cases */
        if (algorithm == null) 
            throw new IllegalArgumentException("Algorithm must not be null");
        if (cols < 1) 
            throw new IllegalArgumentException("Columns must be >= 1");
        if (cols < 1) 
            throw new IllegalArgumentException("Columns must be >= 1");
        /* ----------- */
        
        this.algorithm = algorithm;	// save the LearningAlgorithm to receive an action and give feedback
        
        /* Fill the internal representation of the game-grid with GridCells */
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                final GridCell cell = new GridCell(x, y, CellType.NORMAL);
                grid.put(x, y, cell);
            }
        }
        /* ---------------------------------------------------------------- */
        
        isOver = false;				// mark that the episode begins and therefore isn't over yet
    }

    /**
     * An internal method that returns the current state of the game-grid, represented as an Integer.
     * The game-grid is viewed as a sequence of bits. If a GridCell is blocked, the bit representing 
     * it is set to one, otherwise to 0. Example: <br>
     * 1 1 0 <br>
     * 1 1 0 <br>
     * 0 0 0 <br>
     * Represents the game-grid where the two upper left GridCells are blocked.
     * This grid is represented by the binary number 110110000 which is converted to the Integer 432.
     * 
     * @return an integer representing the current state of the field
     */
    private int getFieldState() {
    	int result = 0;
    	for(int j=0; j<rows; j++) 
    	{
    		for(int i=0; i<cols; i++) 
    		{
    			result <<= 1;	// shift all bits that have been placed so they are saved
    			if(getCell(i,j).type.equals(CellType.BLOCKED)) result |= 1; // if the currently viewed cell is blocked, the respective bit is set to 1
    		}
    	}
    	return result;
    }
    
    /**
     * Lets the LearningAlgorithm place a Brick at a specified position, handles the 
     * placement by calculating the new field and generates a reward for the LearningAlgorithm.
     */
    public void place() {
    	State currentState = new State(getFieldState());	// the state before any action is taken
    	State newState;
        final Action action = algorithm.selectAction(sack, currentState);	// let the LearningAlgorithm choose an action 
        
        /* Error handling */
        if (action == null) 
        {
            LOGGER.error("Selected action is null, this is not allowed!");
            return;
        }
        /* -------------- */
        
        double reward;	// declaration of the reward for the chosen Action
        double totalbits = cols * rows;	// The total number of bits needed to represent the game-grid
        
        
        if(handlePlacement(action))
        {
        	LOGGER.info("Placed Brick");
        	reward = action.getBrick().numBitsSet()/totalbits;			// rate of how many cells of the game-grid were covered with the placed Brick
        	reward *= action.getBrick().value / KnapsackGame.max_value;	// multiply by the relative value of the placed brick
        	isOver = sack.isEmpty() ? true : false;						// if the sack is empty the algorithm placed every Brick and the game is won
        	newState = new State(getFieldState());						// the next State can be calculated after successful placement
        }
        else
        {
        	LOGGER.info("Brick not placed");
        	reward = 0;		// if it wasn't possible to place the brick in bounds, the algorithm gets no reward
        	isOver = true;	// and the episode ends
        	newState = new State(Integer.MAX_VALUE);	// if the episode ended due to an error the next state is an error state
        }
        algorithm.feedback(currentState, newState, action, reward, isOver);	// give the calculated reward to the LearningAlgorithm
    }

    /**
     * Handles the placement of a Brick onto the current game-grid.
     * If any position on the grid that should be blocked by the Action is already blocked, the placement fails.
     * It also fails if any part of the placed Brick overlaps the game-grid and therefore doesn't fit into the grid.
     * 
     * @param action : The chosen Action representing the Brick that should be placed and its respective position
     * @return whether the placement was successful
     */
    private boolean handlePlacement(Action action) {
    	int xPos = action.getPosition().x, yPos = action.getPosition().y;	// get the position where the Brick should be placed
    	Brick b = action.getBrick();	// get the Brick that should be placed
    	int bitlen = b.bricklength();	// the maximum of height and width of the Brick
    	int totalbits = b.bricklength()*b.bricklength(); 	// the total number of bits needed to represent the Brick 
    	for(int i=0; i<bitlen; i++) {		//for this algorithm it doesn't matter if we start with the columns
    		for(int j=0; j<bitlen; j++) {	// or the rows, since most of the work is done in the class Brick
    			boolean setbit = b.getBitonPos(totalbits-(i+bitlen*j));	// should the current GridCell be blocked when placing the Brick?
    			if(i+xPos >= cols && setbit)	
    			{ // if the Brick would overlap on the x-boundary
    				return false;	// the placement was not successful
    			}
    			else if(j+yPos >= rows && setbit)	
    			{ // if the Brick would overlap on the y-boundary
    				return false;	// the placement was not successful
    			}
    			else if(i+xPos < cols && j+yPos < rows && getCell(i+xPos,j+yPos).type.equals(CellType.BLOCKED) && setbit)
    			{ // if the Brick would  not overlap on any boundary but would try to set a GridCell to BLOCKED that is already BLOCKED
    				return false;	// the placement was not successful
    			}
    			else if(i+xPos < cols && j+yPos < rows && getCell(i+xPos,j+yPos).type.equals(CellType.NORMAL) && setbit)
    			{ // if none of the above problems occur the CellType of the type GridCell can be set to BLOCKED and the loop can be continued
    				getCell(i+xPos,j+yPos).type = CellType.BLOCKED;
    			}
    		}
    	}
    	return true;	// if no problems occurred the placement was successful
    }

}
