package com.gbstudios.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gbstudios.entities.Player;
import com.indie.main.Game;

public class UI {
	 /**
	  * M�todo que renderiza a parte gr�fica / tela de jogo
	  * @param g
	  */
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8, 4, 50, 8);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int) ((Game.player.life / Game.player.maxLife) * 50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int) Game.player.life + "/" + (int) Game.player.maxLife, 20, 11);
	}
} 
