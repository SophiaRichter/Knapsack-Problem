package de.hcmlab.rl.ferienaufgabe.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.hcmlab.rl.abschlussprojekt.KnapsackGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 300;
        config.height = 300;
        config.resizable = false;
		new LwjglApplication(new KnapsackGame(), config);
	}
}
