package com.gbstudios.entities;

import java.awt.image.BufferedImage;

public class Weapon extends Entity{

	/**
	 * Método com informações sobre a arma
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param sprite
	 */
	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
	}

}
