package com.wb.level;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class BaseLevel {
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	private final String level_path;

	public BaseLevel(String level_path, float scale) {
		this.level_path = level_path;

		map = new TmxMapLoader().load(this.level_path);
		renderer = new OrthogonalTiledMapRenderer(map, scale);
	}

	public void render(OrthographicCamera camera) {
		renderer.setView(camera);
		renderer.render();
	}

	public OrthogonalTiledMapRenderer getRenderer() {
		return this.renderer;
	}

	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};

	/**
	 * Loads the given TileMapTileLayer into the Box2D world
	 */
	public void loadLevelIntoWorld(int layerNum) {
		if (this.map == null)
			return;
		
//		((TiledMapTileLayer) map.getLayers().get(0)).setVisible(false);
//		((TiledMapTileLayer) map.getLayers().get(1)).setVisible(false);
		// Get the box2D world object from the 'global' instance
		World world = WorldManager.getInstance().getWorld();

		// Get the TileMapTileLayer to get ready for loading
		TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get(layerNum);

		// Generated a static body for each tile and apply to the world instance
		// TODO: Connected tiles should be combined where possible
		for (int x = 0; x < tileLayer.getWidth(); x++) {
			for (int y = 0; y < tileLayer.getHeight(); y++) {
				BodyDef wallBodyDef = new BodyDef();
				wallBodyDef.type = BodyType.StaticBody;
				wallBodyDef.position.set(x, y);
				Body wallBody = world.createBody(wallBodyDef);
				PolygonShape wallBox;

				Cell cell = tileLayer.getCell(x, y);
				if (cell != null) {
					wallBox = new PolygonShape();
					wallBox.setAsBox(500,10f);
					wallBody.createFixture(wallBox, 0.0f);
					wallBox.dispose();
				}
			}
		}
	}

	public void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
		rectPool.freeAll(tiles);
		tiles.clear();
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					Rectangle rect = rectPool.obtain();
					rect.set(x * 32, y * 32, 32, 32);
					tiles.add(rect);
				}
			}
		}
	}
}
