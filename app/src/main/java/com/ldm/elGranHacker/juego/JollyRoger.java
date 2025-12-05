package com.ldm.elGranHacker.juego;

import java.util.ArrayList;
import java.util.List;

public class JollyRoger {
    public static final int ARRIBA = 0;
    public static final int IZQUIERDA= 1;
    public static final int ABAJO = 2;
    public static final int DERECHA = 3;

    public List<Campanas> partes = new ArrayList<>();
    public int direccion;

    public JollyRoger() {
        direccion = ARRIBA;
        partes.add(new Campanas(5, 6));
        partes.add(new Campanas(5, 7));
        partes.add(new Campanas(5, 8));
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

    public void anadirCampana() {
        Campanas end = partes.get(partes.size()-1);
        partes.add(new Campanas(end.x, end.y));
    }

    public void avance() {
        Campanas chef = partes.get(0);

        int len = partes.size() - 1;
        for(int i = len; i > 0; i--) {
            Campanas antes = partes.get(i-1);
            Campanas parte = partes.get(i);
            parte.x = antes.x;
            parte.y = antes.y;
        }

        if(direccion == ARRIBA)
            chef.y -= 1;
        if(direccion == IZQUIERDA)
            chef.x -= 1;
        if(direccion == ABAJO)
            chef.y += 1;
        if(direccion == DERECHA)
            chef.x += 1;

        if(chef.x < 0)
            chef.x = 9;
        if(chef.x > 9)
            chef.x = 0;
        if(chef.y < 0)
            chef.y = 12;
        if(chef.y > 12)
            chef.y = 0;
    }

    public boolean comprobarChoque() {
        int len = partes.size();
        Campanas chef = partes.get(0);
        for(int i = 1; i < len; i++) {
            Campanas parte = partes.get(i);
            if(parte.x == chef.x && parte.y == chef.y)
                return true;
        }
        return false;
    }
}

