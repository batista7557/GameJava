package com.gbstudios.entities;

import java.awt.image.BufferedImage;

public class Bullet extends Entity{

	/**
	 * Método que contém as informações básicas sobre as munições
	 * @param x - posição da bala no eixo x
	 * @param y - posição da bala no eixo y
	 * @param width - largura da bala
	 * @param height - altura da bala
	 * @param sprite - imagem da bala
	 */
	public Bullet(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
	}

}
