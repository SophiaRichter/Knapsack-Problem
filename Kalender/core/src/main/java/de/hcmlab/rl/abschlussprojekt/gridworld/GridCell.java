package de.hcmlab.rl.abschlussprojekt.gridworld;

/**
 * A class representing a position in the game-grid.
 * 
 * @author Manuel Richter
 */
public class GridCell {

    public int x, y;		// position of the GridCell
    public CellType type;	// type of the GridCell
    public ColorType col;	// color of the GridCell

    /**
     * Constructor of the GridCell class.
     * 
     * @param x : x-position of the GridCell
     * @param y : y-position of the GridCell
     * @param type : type of the GridCell - either CellType.NORMAL or CellType.BLOCKED
     * @param col : color of the GridCell
     */
    public GridCell(int x, int y, CellType type, ColorType col) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GridCell)) return false;

        GridCell gridCell = (GridCell) o;

        if (x != gridCell.x) return false;
        return y == gridCell.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
    
    @Override
    public String toString() {
    	return "("+x+","+y+"),"+type+","+col;
    }

}
