package com.gbstudios.world;

public class Camera {
	public static int x = 0;
	public static int y = 0;
	
	/**
	 * Método que tira o fundo preto da aplicação para aumentar o desempenho
	 * @param Atual
	 * @param Min
	 * @param Max
	 * @return
	 */
	public static int clamp(int Atual,int Min,int Max){
		if(Atual < Min){
			Atual = Min;
		}
		
		if(Atual > Max) {
			Atual = Max;
		}
		
		return Atual;
	}
}