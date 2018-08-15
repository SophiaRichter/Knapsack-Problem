package de.hcmlab.rl.abschlussprojekt.gridworld;

import de.hcmlab.rl.abschlussprojekt.solution.Brick;

/**
 * A class representing the action the actor can take
 * 
 * @author Patrizia Schalk and Manuel Richter
 */

public class Action 
{	
	private GridCell position; 	//position of brick placement
	private Brick brick; 		//brick to place
	
	/**
	 * Constructor of the Action class.
	 * The position specified by x and y is implicitly converted into a GridCell of type NORMAL.
	 * 
	 * @param x : x-position in the game-grid where the Brick should be placed
	 * @param y : y-position in the game-grid where the Brick should be placed 
	 * @param b : the Brick that should be placed when the action is executed
	 */
	public Action(int x, int y, Brick b)
	{
		setPosition(x,y);
		setBrick(b);
	}
	
	/**
	 * An alternative constructor of the Action class.
	 * 
	 * @param pos : position in the game-grid where the Brick should be placed
	 * @param b : the Brick that should be placed when the action is executed
	 */
	public Action(GridCell pos, Brick b)
	{
		setPosition(pos);
		setBrick(b);
	}
	
	/**
	 * Resets the position where the Brick specified by the Action should be placed
	 * 
	 * @param position : position in the game-grid represented by a GridCell
	 */
	private void setPosition(GridCell position)
	{
		this.position = position;
	}
	
	/**
	 * Resets the position where the Brick specified by the Action should be placed.
	 * This method converts its parameters x and y into a GridCell of type NORMAL.
	 * 
	 * @param x : new x-position in the game-grid where the Brick should be placed
	 * @param y : new y-position in the game-grid where the Brick should be placed
	 */
	private void setPosition(int x, int y)
	{
		this.position = new GridCell(x,y,CellType.NORMAL,null);
	}
	
	/**
	 * Gives the Brick at the position specified by the Action another shape
	 * 
	 * @param brick : specification of the new Brick
	 */
	private void setBrick(Brick brick)
	{
		this.brick = brick;
	}
	
	/**
	 * Returns the position where a Brick should be placed
	 * 
	 * @return the GridCell representing the position
	 */
	public GridCell getPosition() 
	{
		return position;
	}
	
	/**
	 * Returns the Brick that should be placed at the position specified by the Action
	 * 
	 * @return the Brick saved in the Action class
	 */
	public Brick getBrick()
	{
		return brick;
	}
	
	@Override
	public String toString() 
	{
		return "(("+position.x+","+position.y+"),"+brick.toString();
	}
	
    @Override
    public boolean equals(Object o) 
    {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;

        Action action = (Action) o;

        if (!position.equals(action.position)) return false;
        if (!brick.equals(action.brick)) return false;
        return true;
    }
}
