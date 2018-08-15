package de.hcmlab.rl.abschlussprojekt.components;

import com.badlogic.ashley.core.Component;

import de.hcmlab.rl.abschlussprojekt.gridworld.GridCell;

public class GridCellComponent implements Component {

    public GridCell cell;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GridCellComponent)) return false;

        GridCellComponent that = (GridCellComponent) o;

        return cell != null ? cell.equals(that.cell) : that.cell == null;
    }

    @Override
    public int hashCode() {
        return cell != null ? cell.hashCode() : 0;
    }
}
