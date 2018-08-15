package de.hcmlab.rl.abschlussprojekt.solution;


/**
 * A class representing the state of the game-grid.
 * Stores which GridCells are already blocked by a Brick and which not by storing 
 * this information in a binary sequence represented by an Integer. If a GridCell is 
 * blocked, the bit representing it is set to one, otherwise to 0. Example: <br>
 * 1 1 0 <br>
 * 1 1 0 <br>
 * 0 0 0 <br>
 * Represents the game-grid where the two upper left GridCells are blocked.
 * This grid is represented by the binary number 110110000 which is converted to the Integer 432.
 * 
 * @author Patrizia Schalk and Manuel Richter
 */
public class State 
{
	long bCurrentField;	// the current game-field represented as an Integer
	
	/**
	 * Constructs a new State with the given description of the game-grid.
	 * 
	 * @param bField : the Integer representing the current game-grid
	 */
	public State(long bField)
	{
		this.bCurrentField = bField;
	}
	
	/**
	 * @return the Integer representing the current game-grid.
	 */
	public long getCurrentField()
	{
		return bCurrentField;
	}
	
	@Override
	public boolean equals(Object o) 
	{
		if(o instanceof State) 
		{
			State compare = (State) o;
			if(compare.bCurrentField == this.bCurrentField)
			{
				return true;
			}
			return false;
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return "Field: "+bCurrentField;
		
	}
}
