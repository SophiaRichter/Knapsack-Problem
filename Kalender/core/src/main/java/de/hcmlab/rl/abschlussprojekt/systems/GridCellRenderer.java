package de.hcmlab.rl.abschlussprojekt.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.hcmlab.rl.abschlussprojekt.KnapsackGame;
import de.hcmlab.rl.abschlussprojekt.Util.GraphicsUtil;
import de.hcmlab.rl.abschlussprojekt.components.GridCellComponent;
import de.hcmlab.rl.abschlussprojekt.gridworld.CellType;
import de.hcmlab.rl.abschlussprojekt.gridworld.ColorType;

/**
 * A class deduced from the Wumpus-World problem defined by 
 * the Chair of Human Centered Multimedia
 * 
 * @author Patrizia Schalk
 */
public class GridCellRenderer extends EntitySystem {

    private final SpriteBatch sb;
    private int cellWidth, cellHeight;
    private final Texture cellTex, blockTex_b, blockTex_db, blockTex_lb, blockTex_r, blockTex_dr, blockTex_lr, blockTex_y, blockTex_dy, blockTex_ly, blockTex_g, blockTex_dg, blockTex_lg, blockTex_p, blockTex_dp, blockTex_o;
    private AssetManager assets;
    
    private ImmutableArray<Entity> entities;

    private ComponentMapper<GridCellComponent> gcm = ComponentMapper.getFor(GridCellComponent.class);

    public GridCellRenderer(SpriteBatch spriteBatch, int cellWidth, int cellHeight, Texture cellTex, Texture blockTex_b, Texture blockTex_db, Texture blockTex_lb, Texture blockTex_r, Texture blockTex_dr, Texture blockTex_lr, Texture blockTex_y, Texture blockTex_dy, Texture blockTex_ly, Texture blockTex_g, Texture blockTex_dg, Texture blockTex_lg, Texture blockTex_p, Texture blockTex_dp, Texture blockTex_o, AssetManager assets) {
        sb = spriteBatch;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.cellTex = cellTex;
        this.blockTex_b = blockTex_b;
        this.blockTex_db = blockTex_db;
        this.blockTex_lb = blockTex_lb;
        this.blockTex_r = blockTex_r;
        this.blockTex_dr = blockTex_dr;
        this.blockTex_lr = blockTex_lr;
        this.blockTex_y = blockTex_y;
        this.blockTex_dy = blockTex_dy;
        this.blockTex_ly = blockTex_ly;
        this.blockTex_g = blockTex_g;
        this.blockTex_dg = blockTex_dg;
        this.blockTex_lg = blockTex_lg;
        this.blockTex_p = blockTex_p;
        this.blockTex_dp = blockTex_dp;
        this.blockTex_o = blockTex_o;
        this.assets = assets;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(GridCellComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
    	int clock = 7;
        for (Entity e : entities) {
            final GridCellComponent gc = gcm.get(e);
            
            GraphicsUtil.drawCentered(sb, (Texture)assets.get("clock"+clock+".png"), 0, (KnapsackGame.HEIGHT-1)-gc.cell.y, 100, 50);
            ++clock;
            
            final CellType type = gc.cell.type;
            Texture tex;
            if (type == CellType.BLOCKED) {
            	if(gc.cell.col == ColorType.BLUE) tex = blockTex_b;
            	else if(gc.cell.col == ColorType.DARKBLUE) tex = blockTex_db;
            	else if(gc.cell.col == ColorType.LIGHTBLUE) tex = blockTex_lb;
            	else if(gc.cell.col == ColorType.RED) tex = blockTex_r;
            	else if(gc.cell.col == ColorType.DARKRED) tex = blockTex_dr;
            	else if(gc.cell.col == ColorType.PINK) tex = blockTex_lr;
            	else if(gc.cell.col == ColorType.YELLOW) tex = blockTex_y;
            	else if(gc.cell.col == ColorType.BROWN) tex = blockTex_dy;
            	else if(gc.cell.col == ColorType.LIGHTYELLOW) tex = blockTex_ly;
            	else if(gc.cell.col == ColorType.GREEN) tex = blockTex_g;
            	else if(gc.cell.col == ColorType.DARKGREEN) tex = blockTex_dg;
            	else if(gc.cell.col == ColorType.LIGHTGREEN) tex = blockTex_lg;
            	else if(gc.cell.col == ColorType.PURPLE) tex = blockTex_p;
            	else if(gc.cell.col == ColorType.VIOLET) tex = blockTex_dp;
            	else if(gc.cell.col == ColorType.ORANGE) tex = blockTex_o;
            	else tex = blockTex_b;
            } else {
                tex = cellTex;
            }
            GraphicsUtil.drawCentered(sb, tex, gc.cell.x+0.333f, (KnapsackGame.HEIGHT-1)-gc.cell.y, cellWidth, cellHeight);
        }
    }
}
