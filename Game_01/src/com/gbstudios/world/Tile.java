package com.gbstudios.world;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.gbstudios.entities.Entity;
import com.indie.main.Game;

public class Tile {

	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);
	
	
	private BufferedImage sprite;
	private int x, y;
	
	/**
	 * M�todo com informa��es sobre os tiles (objetos em pixel art est�ticos)
	 * @param x
	 * @param y
	 * @param sprite
	 */
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		
		this.sprite = sprite;
	}
	
	/**
	 * M�todo que renderiza a parte gr�fica
	 * @param g
	 */
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
