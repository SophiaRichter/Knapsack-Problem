package de.hcmlab.rl.abschlussprojekt;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.hcmlab.rl.abschlussprojekt.components.GridCellComponent;
import de.hcmlab.rl.abschlussprojekt.gridworld.ColorType;
import de.hcmlab.rl.abschlussprojekt.gridworld.Simulation;
import de.hcmlab.rl.abschlussprojekt.solution.Brick;
import de.hcmlab.rl.abschlussprojekt.solution.Multiset;
import de.hcmlab.rl.abschlussprojekt.solution.Multiset.Element;
import de.hcmlab.rl.abschlussprojekt.solution.Solution;
import de.hcmlab.rl.abschlussprojekt.systems.GridCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Main class for initializing and game loop
 * 
 * @author Patrizia Schalk
 */
public class KnapsackGame extends ApplicationAdapter {

	private static Logger LOGGER = LoggerFactory.getLogger(KnapsackGame.class);
	public static double average_reward = 0;
	public static double max_value = 0;
	public static final int WIDTH = 1;
	public static final int HEIGHT = 15;

	private final Engine engine = new Engine();

	private SpriteBatch spriteBatch;
	private AssetManager assets;
	private de.hcmlab.rl.abschlussprojekt.learning.LearningAlgorithm algorithm;
	private Simulation sim;
	private Multiset sack;

	private boolean assetsLoaded;
	private Texture normalTex, blockedTex_b, blockedTex_db, blockedTex_lb, blockedTex_r, blockedTex_dr, blockedTex_lr, blockedTex_y, blockedTex_dy, blockedTex_ly, blockedTex_g, blockedTex_dg, blockedTex_lg, blockedTex_p, blockedTex_dp, blockedTex_o;

	private float elapsed, stepDuration = 0.01f;

	@Override
	public void create() {
		spriteBatch = new SpriteBatch();
		assets = new AssetManager();
		algorithm = new Solution();
		sim = new Simulation();
		final Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("/simulation.properties"));
			stepDuration = Float.parseFloat(properties.getProperty("step_duration", "0"));
		} catch (IOException e) {
			LOGGER.warn("Cannot read properties file");
		}

		setupSimulation();
		
		assets.load("CellNormal.png", Texture.class);
		assets.load("CellBlocked_blue.png", Texture.class);
		assets.load("CellBlocked_darkblue.png", Texture.class);
		assets.load("CellBlocked_lightblue.png", Texture.class);
		assets.load("CellBlocked_red.png", Texture.class);
		assets.load("CellBlocked_darkred.png", Texture.class);
		assets.load("CellBlocked_pink.png", Texture.class);
		assets.load("CellBlocked_brown.png", Texture.class);
		assets.load("CellBlocked_yellow.png", Texture.class);
		assets.load("CellBlocked_lightyellow.png", Texture.class);
		assets.load("CellBlocked_green.png", Texture.class);
		assets.load("CellBlocked_darkgreen.png", Texture.class);
		assets.load("CellBlocked_lightgreen.png", Texture.class);
		assets.load("CellBlocked_purple.png", Texture.class);
		assets.load("CellBlocked_violet.png", Texture.class);
		assets.load("CellBlocked_orange.png", Texture.class);
		
		assets.load("clock7.png", Texture.class);
		assets.load("clock8.png", Texture.class);
		assets.load("clock9.png", Texture.class);
		assets.load("clock10.png", Texture.class);
		assets.load("clock11.png", Texture.class);
		assets.load("clock12.png", Texture.class);
		assets.load("clock13.png", Texture.class);
		assets.load("clock14.png", Texture.class);
		assets.load("clock15.png", Texture.class);
		assets.load("clock16.png", Texture.class);
		assets.load("clock17.png", Texture.class);
		assets.load("clock18.png", Texture.class);
		assets.load("clock19.png", Texture.class);
		assets.load("clock20.png", Texture.class);
		assets.load("clock21.png", Texture.class);
	}

	private void setupSimulation() {
		engine.removeAllEntities();
		
		/* This is where new appointments are defined! */
		sack = new Multiset();
        sack.add(new Brick("Auf die Klausur lernen",292,5,ColorType.BLUE));
        sack.add(new Brick("Geschirr spülen",1,1,ColorType.PURPLE));
        sack.add(new Brick("Mama anrufen",10,3,ColorType.RED));
        sack.add(new Brick("Übungsblätter bearbeiten",292,4,ColorType.ORANGE));
        sack.add(new Brick("Aufräumen",10,1,ColorType.GREEN));
        sack.add(new Brick("Projekt beenden",292,6,ColorType.PINK));
        sack.add(new Brick("Geschenk für Monika besorgen",34952,4,ColorType.DARKRED));
        /* ------------------------------------- */
        
        /* Calculation of the average reward used as initial Q-value and of the maximum value */
		double total_coverage = 0;	
		double total_value = 0;
		double total_elements = 0;
		for(Element e : sack)
		{	// for each Element in the sack
			total_coverage += e.getQuantity()*e.getBrick().numBitsSet();	// add the number of squares that the element covers (multiplied by its quantity in the multiset)
			total_elements += e.getQuantity();	// add the quantity of the Element to the total number of Elements in the multiset
			total_value += e.getQuantity()*e.getBrick().value;
			if(e.getBrick().value > max_value) max_value = e.getBrick().value;
		}
		average_reward = (total_coverage/(total_elements*WIDTH*HEIGHT)*(total_value/(total_elements*max_value)));
		/* ---------------------------------------------------------------------------------- */
		
		sim.setup(algorithm, WIDTH, HEIGHT, sack);

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				final Entity e = new Entity();
				final GridCellComponent comp = new GridCellComponent();
				comp.cell = sim.getBestCell(x, y);
				e.add(comp);
				engine.addEntity(e);
			}
		}
	}

	@Override
	public void render() {
		if (!assets.update()) 
		{
			return;
		} 
		else 
		{
			if (!assetsLoaded) 
			{
				normalTex = assets.get("CellNormal.png");
				blockedTex_b = assets.get("CellBlocked_blue.png");
				blockedTex_lb = assets.get("CellBlocked_lightblue.png");
				blockedTex_db = assets.get("CellBlocked_darkblue.png");
				blockedTex_r = assets.get("CellBlocked_red.png");
				blockedTex_dr = assets.get("CellBlocked_darkred.png");
				blockedTex_lr = assets.get("CellBlocked_pink.png");
				blockedTex_y = assets.get("CellBlocked_yellow.png");
				blockedTex_dy = assets.get("CellBlocked_brown.png");
				blockedTex_ly = assets.get("CellBlocked_lightyellow.png");
				blockedTex_g = assets.get("CellBlocked_green.png");
				blockedTex_dg = assets.get("CellBlocked_darkgreen.png");
				blockedTex_lg = assets.get("CellBlocked_lightgreen.png");
				blockedTex_p = assets.get("CellBlocked_purple.png");
				blockedTex_dp = assets.get("CellBlocked_violet.png");
				blockedTex_o = assets.get("CellBlocked_orange.png");
				engine.addSystem(new GridCellRenderer(spriteBatch, normalTex.getWidth(), normalTex.getHeight(), normalTex, blockedTex_b, blockedTex_db, blockedTex_lb, blockedTex_r, blockedTex_dr, blockedTex_lr, blockedTex_y, blockedTex_dy, blockedTex_ly, blockedTex_g, blockedTex_dg, blockedTex_lg, blockedTex_p, blockedTex_dp, blockedTex_o, assets));
				assetsLoaded = true;
			}
		}

		if (!sim.isOver()) 
		{
			elapsed += Gdx.graphics.getDeltaTime();
			if (elapsed >= stepDuration) 
			{
				elapsed -= stepDuration;
				sim.place();
			}
		} 
		else 
		{
			setupSimulation();
		}
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		
		engine.update(Gdx.graphics.getDeltaTime());
	
		spriteBatch.end();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}
}
