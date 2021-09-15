package com.gbstudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gbstudios.world.Camera;
import com.gbstudios.world.World;
import com.indie.main.Game;

public class BulletShoot extends Entity{

	private double dx;
	private double dy;
	private double speed = 4;
	
	private int life = 100, curLife = 0;
	
	/**
	 * M�todo com informa��es b�sicas sobre os tiros de muni��o
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param sprite
	 * @param dx
	 * @param dy
	 */
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	/**
	 * M�todo que atualiza informa��es a cada frame
	 */
	public void tick() {
		if(World.isFreeDynamic((int) (x + (dx * speed)),(int)(y + (dy * speed)), 3, 3)) {
			x+=dx * speed;
			y+=dy * speed;
		}else {
			Game.bullets.remove(this);
			return;
		}
		curLife++;
		if(curLife == life) {
			Game.bullets.remove(this);
			return;
		}
	}
	
	/**
	 * M�todo que renderiza a parte gr�fica
	 */
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
}
