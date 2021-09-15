package com.indie.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.gbstudios.entities.BulletShoot;
import com.gbstudios.entities.Enemy;
import com.gbstudios.entities.Entity;
import com.gbstudios.entities.Player;
import com.gbstudios.graficos.Spritesheet;
import com.gbstudios.graficos.UI;
import com.gbstudios.world.Camera;
import com.gbstudios.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 7;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	
	public static List<Enemy> enemies;
	
	public static List<BulletShoot> bullets;
	
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "MENU";
	
	private boolean showMessageGameOver = true;
	
	private int framesGameOver = 0;
	
	private boolean restartGame = false;
	
	public Menu menu;
	
	public boolean saveGame = false;
		
	/**
	 * Método para inicialização de mundo, objetos e personagens 
	 */
	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//inicializando objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16)); 
		entities.add(player);
		world = new World("/level1.png");
		menu = new Menu();
	}
	
	/**
	 * Método para criação da tela de jogo
	 */
	public void initFrame() {
		frame = new JFrame("Ghost Slayer");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * Método para criação da thread que possibilitará o sistema rodar diversos pontos ao mesmo tempo
	 */
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	/**
	 * Método que para a thread
	 */
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que inicializa o jogo
	 */
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}
	
	/**
	 * Método tick que atualiza informações a cada tick (frame) 
	 */
	public void tick() {
		if(gameState == "NORMAL") {
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level", "lvl", "xp", "ammo"};
				int[] opt2 = {this.CUR_LEVEL, player.lvl, player.xp, player.ammo};
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("Jogo salvo!");
			}
			this.restartGame = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
		
		if(enemies.size() == 0) {
			//Avançar para o próximo level!
			CUR_LEVEL++;
			if(CUR_LEVEL > MAX_LEVEL) {
				CUR_LEVEL = 1;
			}
			String newWorld = "level" + CUR_LEVEL + ".png";
			World.restartGame(newWorld);
		}
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver)
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
			}
			if(restartGame) {
				this.restartGame = false;
				this.gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		}else if(gameState == "MENU") {
				menu.tick();
		}
		
	}
	
	
	/**
	 * Método que renderiza a parte gráfica
	 */
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		//renderizando o jogo
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		ui.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 17));
		g.setColor(Color.white);
		g.drawString("Munição: " + Player.ammo, 610, 30);
		g.drawString("Level: " + Player.lvl, 610, 60);
		g.drawString("Fase: " + CUR_LEVEL, 350, 30);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 150));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setFont(new Font("arial", Font.BOLD, 70));
			g.setColor(Color.white);
			g.drawString("GAME OVER!", 155, 255);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.setColor(Color.white);
			if(showMessageGameOver)
				g.drawString(">PRESSIONE ENTER PARA REINICIAR<", 100, 300);
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		bs.show();
	}
	
	/**
	 * Sistema de fps (frames por segundo)
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer = System.currentTimeMillis();
			}
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {

		
	}

	/**
	 * Método para capturar eventos do teclado
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			
			if(gameState == "MENU") {
				menu.up = true;
			}
		}
		
			else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				player.down = true;
				
				if(gameState == "MENU") {
					menu.down = true;
				}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			 player.shoot = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			Menu.pause = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_B) {
			if(gameState == "NORMAL")
				this.saveGame = true;
			
		}
	}

	/** 
	 * Método que capta o soltamento de teclas do teclado
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}
		
			else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				player.down = false;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	/**
	 * Método que capta eventos do mouse
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() / SCALE);
		player.my = (e.getY() / SCALE);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}
}