package de.hcmlab.rl.abschlussprojekt.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.hcmlab.rl.abschlussprojekt.Util.GraphicsUtil;
import de.hcmlab.rl.abschlussprojekt.components.GridCellComponent;
import de.hcmlab.rl.abschlussprojekt.gridworld.CellType;

/**
 * A class deduced from the Wumpus-World problem defined by 
 * the Chair of Human Centered Multimedia.
 * 
 * @author Hannes Ritschel
 */
public class GridCellRenderer extends EntitySystem {

    private final SpriteBatch sb;
    private int cellWidth, cellHeight;
    private final Texture cellTex, blockTex;

    private ImmutableArray<Entity> entities;

    private ComponentMapper<GridCellComponent> gcm = ComponentMapper.getFor(GridCellComponent.class);

    public GridCellRenderer(SpriteBatch spriteBatch, int cellWidth, int cellHeight, Texture cellTex, Texture blockTex) {
        sb = spriteBatch;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.cellTex = cellTex;
        this.blockTex = blockTex;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(GridCellComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity e : entities) {
            final GridCellComponent gc = gcm.get(e);

            GraphicsUtil.drawCentered(sb, cellTex, gc.cell.x, 2-gc.cell.y, cellWidth, cellHeight);
            
            final CellType type = gc.cell.type;
            Texture tex;
            if (type == CellType.BLOCKED) {
                tex = blockTex;
            } else {
                tex = cellTex;
            }
            GraphicsUtil.drawCentered(sb, tex, gc.cell.x, 2-gc.cell.y, cellWidth, cellHeight);
        }

    }
}
