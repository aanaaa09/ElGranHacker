package com.ldm.elGranHacker.juego;

import com.ldm.elGranHacker.Pantalla;
import com.ldm.elGranHacker.androidimpl.AndroidJuego;

public class JuegoGranHacker extends AndroidJuego {
    @Override
    public Pantalla getStartScreen() {
        return new LoadingScreen(this);
    }
}
