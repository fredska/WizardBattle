package com.wb.level;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
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
	
	public void getTiles(int startX, int startY, int endX, int endY,
			Array<Rectangle> tiles) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
		rectPool.freeAll(tiles);
		tiles.clear();
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					Rectangle rect = rectPool.obtain();
					rect.set(x*32, y*32, 33,33);
					tiles.add(rect);
				}
			}
		}
	}
}
