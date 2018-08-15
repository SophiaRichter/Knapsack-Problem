package de.hcmlab.rl.abschlussprojekt.Util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A class deduced from the Wumpus-World problem defined by 
 * the Chair of Human Centered Multimedia
 *  
 * @author Hannes Ritschel
 */
public class GraphicsUtil {

    public static void drawCentered(SpriteBatch sb, Texture tex, float x, float y, int cellWidth, int cellHeight) {
        drawCentered(sb, tex, x, y, cellWidth, cellHeight, 0);
    }

    public static void drawCentered(SpriteBatch sb, Texture tex, float x, float y, int cellWidth, int cellHeight, float rotation) {
        final float offsX = (cellWidth - tex.getWidth()) / 2;
        final float offsY = (cellHeight - tex.getHeight()) / 2;
        sb.draw(tex, x * cellWidth + offsX, y * cellHeight + offsY, tex.getWidth() / 2, tex.getHeight() / 2, tex.getWidth(), tex.getHeight(), 1, 1, rotation, 0, 0, tex.getWidth(), tex.getHeight(), false, false);
    }

}
