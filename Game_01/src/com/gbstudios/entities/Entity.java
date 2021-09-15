package com.gbstudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.gbstudios.world.Camera;
import com.indie.main.Game;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6 * 16, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7 * 16, 0, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6 * 16, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7 * 16, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(144, 16, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 0, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(144, 0, 16, 16);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	private int  maskx, masky, mwidth, mheight;
	
	/**
	 * M�todo com informa��es b�sicas sobre as entities (objetos que interagem com o jogador)
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param sprite
	 */
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	/**
	 * M�todo que seta uma m�scara em cada entitie
	 * @param maskx
	 * @param masky
	 * @param mwidth
	 * @param mheight
	 */
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	 public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	/**
	 * M�todo que checa colis�o entre entities
	 * @param e1
	 * @param e2
	 * @return
	 */
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		
		return e1mask.intersects(e2mask);
	}
	
	/**
	 * M�todo que renderiza a parte gr�fica
	 * @param g
	 */
	public void render(Graphics g) {
		g.drawImage(sprite,this.getX() - Camera.x,this.getY() - Camera.y,null);
//		g.setColor(Color.red);
//		g.fillRect(this.getX() + maskx - Camera.x,this.getY() + masky - Camera.y, mwidth, mheight);
	}
}
