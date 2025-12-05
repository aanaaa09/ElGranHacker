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
        Assets.fondo2 = g.newPixmap("fondo2.png", PixmapFormat.RGB565);
        Assets.pizarra = g.newPixmap("pizarra.png", PixmapFormat.ARGB4444);
        Assets.menu = g.newPixmap("menu.png", PixmapFormat.ARGB4444);
        Assets.botones = g.newPixmap("botones.png", PixmapFormat.ARGB4444);
        Assets.ayuda1 = g.newPixmap("ayuda1.png", PixmapFormat.ARGB4444);
        Assets.ayuda2 = g.newPixmap("ayuda2.png", PixmapFormat.ARGB4444);
        Assets.ayuda3 = g.newPixmap("ayuda3.png", PixmapFormat.ARGB4444);
        Assets.numeros = g.newPixmap("numeros.png", PixmapFormat.ARGB4444);
        Assets.preparadoN = g.newPixmap("preparadoN.png", PixmapFormat.ARGB4444);
        Assets.preparadoE = g.newPixmap("preparadoE.png", PixmapFormat.ARGB4444);
        Assets.menupausa = g.newPixmap("menupausa.png", PixmapFormat.ARGB4444);
        Assets.finjuego = g.newPixmap("finjuego.png", PixmapFormat.ARGB4444);
        Assets.hackerarriba = g.newPixmap("hackerarriba.png", PixmapFormat.ARGB4444);
        Assets.chefizquierda = g.newPixmap("chefizquierda.png", PixmapFormat.ARGB4444);
        Assets.chefabajo = g.newPixmap("chefabajo.png", PixmapFormat.ARGB4444);
        Assets.hackerderecha = g.newPixmap("hackerderecha.png", PixmapFormat.ARGB4444);
        Assets.campana = g.newPixmap("campana.png", PixmapFormat.ARGB4444);
        Assets.lechuga = g.newPixmap("lechuga.png", PixmapFormat.ARGB4444);
        Assets.tomate = g.newPixmap("tomate.png", PixmapFormat.ARGB4444);
        Assets.queso = g.newPixmap("queso.png", PixmapFormat.ARGB4444);
        Assets.campanaDorada = g.newPixmap("campanaDorada.png", PixmapFormat.ARGB4444);
        Assets.gusano = g.newPixmap("gusano.png", PixmapFormat.ARGB4444);
        Assets.cuchillo = g.newPixmap("cuchillo.png", PixmapFormat.ARGB4444);
        Assets.tenedor = g.newPixmap("tenedor.png", PixmapFormat.ARGB4444);
        Assets.clic = juego.getAudio().nuevoSonido("clic.ogg");
        Assets.comer = juego.getAudio().nuevoSonido("comer.ogg");
        Assets.platoroto = juego.getAudio().nuevoSonido("platoroto.ogg");
        Assets.asco = juego.getAudio().nuevoSonido("asco.ogg");


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