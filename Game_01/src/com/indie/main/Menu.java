 package com.indie.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.gbstudios.world.World;

public class Menu {

	public String[] options = {"Novo Jogo", "Carregar Jogo", "Sair"};
	
	public int curOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up;

	public boolean down;
	
	public boolean enter;
	
	public static boolean pause = false;
	
	public static boolean saveExists = false;
	
	public static boolean saveGame = false;
	
	/**
	 * Método que atualiza informações a cada frame
	 */
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		if(up) {
			up = false;
			curOption--;
			if(curOption < 0) {
				curOption = maxOption;
			}
		}
		
		if(down) {
			down = false;
			curOption++;
			if(curOption > maxOption) {
				curOption = 0;
			}
		}
		
		if(enter) {
			enter = false;
			if(options[curOption] == "Novo Jogo" || options[curOption] == "Continuar") {
				Game.gameState = "NORMAL";
				pause = false;
				file = new File("save.txt");
				file.delete();
			}else if(options[curOption] == "Carregar Jogo") {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}else if(options[curOption] == "Sair") {
				System.exit(1);
			}
		}
	}
	
	/**
	 * Método que aplica o save
	 * @param str
	 */
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
				case "level" :
					World.restartGame("level" + spl2[1] + ".png");
					Game.gameState = "NORMAL";
					pause = false;
					break;
				case "lvl":
					Game.player.lvl = Integer.parseInt(spl2[1]);
					break;
				case "xp":
					Game.player.xp = Integer.parseInt(spl2[1]);
					break;
				case "ammo":
					Game.player.ammo = Integer.parseInt(spl2[1]);
					break;
			}
		}
	}
	
	/**
	 * Método que carrega o jogo
	 * @param encode
	 * @return
	 */
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];
						}
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
					}
				}catch (IOException e) {
					
				}
			}catch(FileNotFoundException e) {
				
			}
		}
		return line;
	}
	
	/**
	 * Método que salva o jogo
	 * @param val1
	 * @param val2
	 * @param encode
	 */
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ":";
			
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n] += encode;
				current += value[n];
			}
			try {
				write.write(current);
				if(i < val1.length - 1) 
					write.newLine();
			}catch(IOException e) {
			
			}
			
			try {
				write.flush();
				write.close();
			}catch(IOException e) {
				
			}
			}
		}
	
	/**
	 * Método que renderiza a parte gráfica
	 * @param g
	 */
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString(">Ghost Slayer<", (Game.WIDTH*Game.SCALE) / 2 - 120, (Game.HEIGHT*Game.SCALE) / 2 - 100);
		
		//Opções de menu
		g.setFont(new Font("arial", Font.BOLD, 24));
		if(pause == false) {
			g.drawString("Novo Jogo", (Game.WIDTH*Game.SCALE) / 2 - 75, (Game.HEIGHT*Game.SCALE) / 2 - 70);
		}else {
			g.drawString("Continuar", (Game.WIDTH*Game.SCALE) / 2 - 75, (Game.HEIGHT*Game.SCALE) / 2 - 70);
		}
		g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE) / 2 - 75, (Game.HEIGHT*Game.SCALE) / 2 - 40);
		g.drawString("Sair", (Game.WIDTH*Game.SCALE) / 2 - 75, (Game.HEIGHT*Game.SCALE) / 2 - 10);
		
		if(options[curOption] == "Novo Jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 90, (Game.HEIGHT*Game.SCALE) / 2 - 70);
		}else if(options[curOption] == "Carregar Jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 90, (Game.HEIGHT*Game.SCALE) / 2 - 40);
		}else if(options[curOption] == "Sair") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 90, (Game.HEIGHT*Game.SCALE) / 2 - 10);
		}
	}
}
