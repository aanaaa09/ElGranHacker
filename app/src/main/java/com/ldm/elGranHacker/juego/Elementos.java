package com.ldm.elGranHacker.juego;

public class Elementos {
    public static final int TIPO_1 = 0;
    public static final int TIPO_2 = 1;
    public static final int TIPO_3 = 2;
    public static final int TIPO_4 = 3;
    public static final int TIPO_GUSANO = 4;
    public int x, y;
    public int tipo;
    public float tiempoVida = 0;

    public Elementos(int x, int y, int tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }
}
