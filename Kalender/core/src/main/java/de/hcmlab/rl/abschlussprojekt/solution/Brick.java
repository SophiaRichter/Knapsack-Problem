package de.hcmlab.rl.abschlussprojekt.solution;

import de.hcmlab.rl.abschlussprojekt.gridworld.ColorType;

/**
 * A class representing a Brick that should be placed on the game-field.
 * By convention a brick is defined by the sequence of bits of an Integer, 
 * where a blocked part is represented by a 1 and an open part by a 0.
 * To identify equal Bricks the length of the bit-sequence is defined by 
 * the maximum of width and height squared. <br>
 * Example: <br>
 * x x x <br>
 * x <br>
 * has a width of 3 and a height of 2. Therefore it will be masked into 
 * a 3x3 shape <br>
 * x x x <br>
 * x o o <br>
 * o o o <br>
 * We get the Integer representing this Brick by writing 1 on every position 
 * that is blocked and 0 otherwise. We get: <br>
 * 1 1 1 <br>
 * 1 0 0 <br>
 * 0 0 0 <br>
 * Therefore our Integer representation of this brick is (111100000) = 480.
 * 
 * @author Patrizia Schalk and Manuel Richter
 */
public class Brick 
{
	public String name;		// a name representing the shape - for debug purposes
	public int bShape;		// the shape of the Brick represented by an Integer
	public double value;	// the value of a Brick
	public ColorType color;	// the color the Brick should have
	
	/**
	 * Constructor of the Brick class.
	 * 
	 * @param name : a name describing the shape of the Brick
	 * @param bShape : an integer defining the shape of the Brick
	 * @param value : the value of the specified Brick
	 * @param color : the color of the Brick
	 */
	public Brick(String name, int bShape, double value, ColorType color)
	{
		this.name = name;
		this.bShape = bShape;
		this.value = value;
		this.color = color;
	}
	
	/**
	 * Constructor of the Brick class.
	 * 
	 * @param bShape : an integer defining the shape of the Brick
	 */
	public Brick(int bShape)
	{
		this.name = "";
		this.bShape = bShape;
		this.value = 1;
		this.color = ColorType.BLUE;
	}
	
	/**
	 * Returns the maximum of the width and the height of a Brick by 
	 * counting the bits needed by the representing Integer
	 * 
	 * @return the maximum of the height and width of the Brick
	 */
	public int bricklength()
	{
		int shape = bShape;
		int numBits = 0;
		do // count how many bits represent the shape
		{
			shape >>= 1;
			++numBits;
		} while(shape != 0);
		return (int)Math.ceil(Math.sqrt(numBits)); // take square root of counted bits.
		// If the shape starts with a zero the result is not a natural number -> ceil the result!
	}
	
	/**
	 * Returns whether the Bit on Position position is set or not.
	 * 
	 * @param position : the position of the bit that should be examined, beginning with 1.
	 * @return whether the bit on the given position is set to 1
	 */
	public boolean getBitonPos(int position)
	{
		return (bShape & (int)Math.pow(2, position-1)) != 0;
	}
	
	/**
	 * Returns how many bits in the Integer representing the shape of the Brick are set.
	 * This method is needed to calculate the reward for setting a specified Brick.
	 * 
	 * @return : number of bits set to 1
	 */
	public int numBitsSet() {
		int shape = bShape;
		int result = 0;
		do
		{
			if((shape & 1) != 0) ++result;
			shape >>= 1;
		} while(shape != 0);
		return result;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brick)) return false;

        Brick brick = (Brick) o;
        
        if (!this.name.equals(brick.name)) return false;
        if (!(this.bShape == brick.bShape)) return false;
        if (!(this.value == brick.value)) return false;
        return true;
    }
    
    @Override
    public String toString() {
    	return name+" : "+bShape+" : "+value;
    }
}
