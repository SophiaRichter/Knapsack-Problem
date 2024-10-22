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
 */
public class KnapsackGame extends ApplicationAdapter {

	private static Logger LOGGER = LoggerFactory.getLogger(KnapsackGame.class);
	public static double average_reward = 0;
	public static double max_value = 0;
	public static final int WIDTH = 3;
	public static final int HEIGHT = 3;

	private final Engine engine = new Engine();

	private SpriteBatch spriteBatch;
	private AssetManager assets;
	private de.hcmlab.rl.abschlussprojekt.learning.LearningAlgorithm algorithm;
	private Simulation sim;
	private Multiset sack;

	private boolean assetsLoaded;
	private Texture normalTex, blockedTex;

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
		assets.load("CellBlocked.png", Texture.class);
	}

	private void setupSimulation() {
		engine.removeAllEntities();
		
		/* This is where new Bricks are defined! */
		sack = new Multiset();
        sack.add(new Brick("O",15,5));
        sack.add(new Brick("-",12,1));
        sack.add(new Brick("-",12,1));
        sack.add(new Brick("L",7,4));
        sack.add(new Brick(".",1,1));
        sack.add(new Brick("I",292,1));
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
				comp.cell = sim.getCell(x, y);
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
				blockedTex = assets.get("CellBlocked.png");
				engine.addSystem(new GridCellRenderer(spriteBatch, normalTex.getWidth(), normalTex.getHeight(), normalTex, blockedTex));
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
