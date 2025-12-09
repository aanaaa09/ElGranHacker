package com.ldm.elGranHacker.juego;

import java.util.ArrayList;
import java.util.List;

public class Hacker {
    public static final int ARRIBA = 0;
    public static final int IZQUIERDA= 1;
    public static final int ABAJO = 2;
    public static final int DERECHA = 3;

    public List<Escudos> partes = new ArrayList<>();
    public int direccion;

    public Hacker() {
        direccion = ARRIBA;
        partes.add(new Escudos(5, 6));
        partes.add(new Escudos(5, 7));
        partes.add(new Escudos(5, 8));
    }

    public void girarIzquierda() {
        direccion += 1;
        if(direccion > DERECHA)
            direccion = ARRIBA;
    }

    public void girarDerecha() {
        direccion -= 1;
        if(direccion < ARRIBA)
            direccion = DERECHA;
    }

    public void anadirEscudo() {
        Escudos end = partes.get(partes.size()-1);
        partes.add(new Escudos(end.x, end.y));
    }

    public void avance() {
        Escudos hacker = partes.get(0);

        int len = partes.size() - 1;
        for(int i = len; i > 0; i--) {
            Escudos antes = partes.get(i-1);
            Escudos parte = partes.get(i);
            parte.x = antes.x;
            parte.y = antes.y;
        }

        if(direccion == ARRIBA)
            hacker.y -= 1;
        if(direccion == IZQUIERDA)
            hacker.x -= 1;
        if(direccion == ABAJO)
            hacker.y += 1;
        if(direccion == DERECHA)
            hacker.x += 1;

        if(hacker.x < 0)
            hacker.x = 9;
        if(hacker.x > 9)
            hacker.x = 0;
        if(hacker.y < 0)
            hacker.y = 12;
        if(hacker.y > 12)
            hacker.y = 0;
    }

    public boolean comprobarChoque() {
        int len = partes.size();
        Escudos hacker = partes.get(0);
        for(int i = 1; i < len; i++) {
            Escudos parte = partes.get(i);
            if(parte.x == hacker.x && parte.y == hacker.y)
                return true;
        }
        return false;
    }
}

