
package com.ldm.elGranHacker.juego;

public class Obstaculo {
    public int x, y;         // Coordenadas del obstáculo
    public float tiempoVida; // Tiempo de vida del obstáculo
    public int tipo;         // Tipo del obstáculo

    public Obstaculo(int x, int y, int tipo) {
        this.x = x;
        this.y = y;
        this.tiempoVida = 0; // Inicializar tiempo de vida
        this.tipo = tipo;    // Asignar el tipo del obstáculo
    }
}
