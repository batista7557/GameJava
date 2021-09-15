package com.gbstudios.entities;

import java.awt.image.BufferedImage;

public class Bullet extends Entity{

	/**
	 * M�todo que cont�m as informa��es b�sicas sobre as muni��es
	 * @param x - posi��o da bala no eixo x
	 * @param y - posi��o da bala no eixo y
	 * @param width - largura da bala
	 * @param height - altura da bala
	 * @param sprite - imagem da bala
	 */
	public Bullet(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
	}

}
