package com.ldm.elGranHacker.juego;

import com.ldm.elGranHacker.Juego;
import com.ldm.elGranHacker.Graficos;
import com.ldm.elGranHacker.Pantalla;
import com.ldm.elGranHacker.Graficos.PixmapFormat;

public class LoadingScreen extends Pantalla{
    public LoadingScreen(Juego juego) {
        super(juego);
    }

    @Override
    public void update(float deltaTime) {
        Graficos g = juego.getGraphics();
        Assets.fondo = g.newPixmap("fondo.png", PixmapFormat.RGB565);
        Assets.logo = g.newPixmap("pizarra.png", PixmapFormat.ARGB4444);
        Assets.modos = g.newPixmap("modos.png", PixmapFormat.ARGB4444);
        Assets.buttons = g.newPixmap("buttons.png", PixmapFormat.ARGB4444);
        Assets.ayuda_1 = g.newPixmap("ayuda_1.png", PixmapFormat.ARGB4444);
        Assets.ayuda_2 = g.newPixmap("ayuda_2.png", PixmapFormat.ARGB4444);
        Assets.ayuda_3 = g.newPixmap("ayuda_3.png", PixmapFormat.ARGB4444);
        Assets.numeros = g.newPixmap("numeros.png", PixmapFormat.ARGB4444);
        Assets.inicioN = g.newPixmap("inicioN.png", PixmapFormat.ARGB4444);
        Assets.inicioD = g.newPixmap("inicioD.png", PixmapFormat.ARGB4444);
        Assets.menupausa1 = g.newPixmap("menupausa.png", PixmapFormat.ARGB4444);
        Assets.finjuego = g.newPixmap("finjuego.png", PixmapFormat.ARGB4444);
        Assets.hackerarriba = g.newPixmap("hackerarriba.png", PixmapFormat.ARGB4444);
        Assets.hackerizquierda = g.newPixmap("hackerizquierda.png", PixmapFormat.ARGB4444);
        Assets.hackerabajo = g.newPixmap("hackerabajo.png", PixmapFormat.ARGB4444);
        Assets.hackerderecha = g.newPixmap("hackerderecha.png", PixmapFormat.ARGB4444);
        Assets.escudo = g.newPixmap("escudo.png", PixmapFormat.ARGB4444);
        Assets.virus_5ptos = g.newPixmap("virus_5ptos.png", PixmapFormat.ARGB4444);
        Assets.virus_7ptos = g.newPixmap("virus_7ptos.png", PixmapFormat.ARGB4444);
        Assets.virus_10ptos = g.newPixmap("virus_10ptos.png", PixmapFormat.ARGB4444);
        Assets.escudoDorado = g.newPixmap("escudoDorado.png", PixmapFormat.ARGB4444);
        Assets.malware = g.newPixmap("malware.png", PixmapFormat.ARGB4444);
        Assets.calavera1 = g.newPixmap("calavera1.png", PixmapFormat.ARGB4444);
        Assets.calavera2 = g.newPixmap("calavera2.png", PixmapFormat.ARGB4444);
        Assets.clic = juego.getAudio().nuevoSonido("clic.ogg");
        Assets.beep = juego.getAudio().nuevoSonido("beep.ogg");
        Assets.error = juego.getAudio().nuevoSonido("error.ogg");
        Assets.virus = juego.getAudio().nuevoSonido("virus.ogg");


        Configuraciones.cargar(juego.getFileIO());
        juego.setScreen(new MainMenuScreen(juego));
    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void pause() {

    }


    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}