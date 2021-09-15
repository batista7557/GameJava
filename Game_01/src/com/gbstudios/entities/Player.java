package com.gbstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.gbstudios.graficos.Spritesheet;
import com.gbstudios.world.Camera;
import com.gbstudios.world.World;
import com.indie.main.Game;

public class Player extends Entity{

	public boolean right, left, up, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.4;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDamage;
	
	private boolean hasGun = false;
	
	public static int ammo = 0;
	
	public static int xp = 0;
	
	public static int lvl = 1;
	
	public boolean isDamaged = false;
	private int DamageFrames = 0;
	
	public boolean shoot = false, mouseShoot = false;
	
	public double life = 100, maxLife = 100;
	
	public int mx, my;
	
	/**
	 * Método que contém as informaões básicas do personagem
	 * @param x - posição do jogador no eixo x
	 * @param y - posição do jogador no eixo y
	 * @param width - largura do jogador
	 * @param height - altura do jogador
	 * @param sprite - imagem do jogador 
	 */
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}

	}
	
	/**
	 * Método que atualiza informações a cada frame
	 */
	public void tick() {
		moved = false;
		if(right && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x = x + speed;
		}
			else if(left && World.isFree((int) (x - speed), this.getY())) {
				moved = true;
				dir = left_dir;
				x = x - speed;
			}
		if(up && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			y = y - speed;
		}
			else if(down && World.isFree(this.getX(), (int) (y + speed))) {
				moved = true;
				y = y + speed;
			}
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
			}
			if(index > maxIndex)
				index = 0;
		}
		
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionGun();
		
		if(isDamaged) {
			this.DamageFrames++;
			if(this.DamageFrames == 8) {
				this.DamageFrames = 0;
				isDamaged = false; 
			}
		}
		
		if(shoot) {
			//Criar bala e atirar
			shoot = false;
			if(hasGun && ammo > 0) {
				ammo--;
				int dx = 0;
				int px = 0;
				int py = 6;
				if( dir == right_dir) {
					px = 18;
					dx = 1;
				}else {
					px = -8;
					dx = -1;
				}
			
			BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
			Game.bullets.add(bullet);
			}
		}
		
		if(mouseShoot) {
			mouseShoot = false;
			

			if(hasGun && ammo > 0) {
				ammo--;
				int px = 8;
				int py = 8;
				double angle = 0;
				if( dir == right_dir) {
					px = 18;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));
				}else {
					px = -8;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));
				}
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				
			BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
			Game.bullets.add(bullet);
			}
		}
		
		if(life <= 0) {
			//Game Over!
			//Renderizar tudo novamente
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH*16 - Game.WIDTH );
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	/**
	 * Método que checa a colisão do jogador com a arma
	 */
	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColliding(this, atual)) {
					hasGun = true;
//					System.out.println("Pegou a arma!");
					Game.entities.remove(atual);
				}
			}
		}
	} 
	
	/**
	 * Método que checa a colisão do jogador com a munição
	 */
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColliding(this, atual)) {
					ammo+=10;
					System.out.println("Munição atual: " + ammo);
					Game.entities.remove(atual);
				}
			}
		}
	} 
	
	/**
	 * Método que checa a colisão do jogador com o kit médico
	 */
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePack) {
				if(Entity.isColliding(this, atual)) {
					if(life >= 90)
						continue;
					life+=10;
					if(life > 100) {
						life = 100;
					}
						Game.entities.remove(atual);
					
				}
			}
		}
	}
	
	/**
	 * Método que renderiza a parte gráfica
	 */
	public void render(Graphics g) {
		if(!isDamaged) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					//Desenhar arma para a direita
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 6 - Camera.x, this.getY() + 1 - Camera.y, null);
				}
			}else if(dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					//Desenhar arma para a esquerda
					g.drawImage(Entity.GUN_LEFT, this.getX() - 6 - Camera.x, this.getY() + 1 - Camera.y, null);
				}
			}
		}else {
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
