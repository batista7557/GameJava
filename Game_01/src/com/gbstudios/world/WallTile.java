package com.gbstudios.world;

import java.awt.image.BufferedImage;

import com.gbstudios.entities.BulletShoot;
import com.gbstudios.entities.Entity;
import com.indie.main.Game;

public class WallTile extends Tile{

	/**
	 * Método com informações sobre as paredes do jogo
	 * @param x
	 * @param y
	 * @param sprite
	 */
	public WallTile(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
	}
		
}