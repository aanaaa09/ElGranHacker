package com.ldm.elGranHacker.juego;

import java.util.List;

import com.ldm.elGranHacker.Juego;
import com.ldm.elGranHacker.Graficos;
import com.ldm.elGranHacker.Input.TouchEvent;
import com.ldm.elGranHacker.Pixmap;
import com.ldm.elGranHacker.Pantalla;

public class PantallaJuego extends Pantalla {
    enum EstadoJuego {
        Preparado,
        Ejecutandose,
        Pausado,
        FinJuego
    }

    EstadoJuego estado = EstadoJuego.Preparado;
    Mundo mundo;
    int antiguaPuntuacion = 0;
    String puntuacion = "0";
    private boolean modoExtremo;

    public PantallaJuego(Juego juego, boolean modoExtremo) {
        super(juego);
        this.modoExtremo = modoExtremo;
        mundo = new Mundo(modoExtremo);
    }

    public PantallaJuego(Juego juego) {
        this(juego, false);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = juego.getInput().getTouchEvents();
        juego.getInput().getKeyEvents();

        if(estado == EstadoJuego.Preparado)
            updateReady(touchEvents);
        if(estado == EstadoJuego.Ejecutandose)
            updateRunning(touchEvents, deltaTime);
        if(estado == EstadoJuego.Pausado)
            updatePaused(touchEvents);
        if(estado == EstadoJuego.FinJuego)
            updateGameOver(touchEvents);

    }

    private void updateReady(List<TouchEvent> touchEvents) {
        if (!touchEvents.isEmpty())
            estado = EstadoJuego.Ejecutandose;
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x < 64 && event.y < 64) {
                    if(Configuraciones.sonidoHabilitado)
                        Assets.clic.play(1);
                    estado = EstadoJuego.Pausado;
                    return;
                }
            }
            if(event.type == TouchEvent.TOUCH_DOWN) {
                if(event.x < 64 && event.y > 416) {
                    mundo.hacker.girarIzquierda();
                }
                if(event.x > 256 && event.y > 416) {
                    mundo.hacker.girarDerecha();
                }
            }
        }

        mundo.update(deltaTime);
        if(mundo.finalJuego) {
            if(Configuraciones.sonidoHabilitado)
                Assets.error.play(1);
            estado = EstadoJuego.FinJuego;
        }
        if(antiguaPuntuacion != mundo.puntuacion) {
            antiguaPuntuacion = mundo.puntuacion;
            puntuacion = "" + antiguaPuntuacion;
            if(Configuraciones.sonidoHabilitado && !mundo.malwareFueComido())
                Assets.beep.play(1);
        }
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);

            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 55 && event.x <= 260 && event.y > 115 && event.y <= 150) {
                    if (Configuraciones.sonidoHabilitado)
                        Assets.clic.play(1);
                    estado = EstadoJuego.Ejecutandose;
                    return;
                }

                if (event.x > 100 && event.x <= 235 && event.y > 190 && event.y <= 220) {
                    if (Configuraciones.sonidoHabilitado)
                        Assets.clic.play(1);
                    juego.setScreen(new MainMenuScreen(juego));
                    return;
                }
            }
        }
    }


    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x >= 128 && event.x <= 192 &&
                        event.y >= 200 && event.y <= 264) {
                    if(Configuraciones.sonidoHabilitado)
                        Assets.clic.play(1);
                    juego.setScreen(new MainMenuScreen(juego));
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graficos g = juego.getGraphics();

        if (g != null) {
            g.drawPixmap(Assets.fondo, 0, 0);
            drawWorld(mundo);
            if (estado == EstadoJuego.Preparado)
                drawReadyUI();
            if (estado == EstadoJuego.Ejecutandose)
                drawRunningUI();
            if (estado == EstadoJuego.Pausado)
                drawPausedUI();
            if (estado == EstadoJuego.FinJuego)
                drawGameOverUI();

            drawText(g, puntuacion, g.getWidth() / 2 - puntuacion.length() * 20 / 2, g.getHeight() - 42);
        }
    }

    private void drawWorld(Mundo mundo) {
        Graficos g = juego.getGraphics();
        Hacker jollyroger = mundo.hacker;
        Escudos head = jollyroger.partes.get(0);

        // Dibujar virus (antes ingredientes)
        Pixmap virusPixmap = null;
        if (mundo.virus.tipo == Elementos.TIPO_1)
            virusPixmap = Assets.virus_5ptos;
        if (mundo.virus.tipo == Elementos.TIPO_2)
            virusPixmap = Assets.virus_7ptos;
        if (mundo.virus.tipo == Elementos.TIPO_3)
            virusPixmap = Assets.virus_10ptos;
        if (mundo.virus.tipo == Elementos.TIPO_4)
            virusPixmap = Assets.escudoDorado;

        g.drawPixmap(virusPixmap, mundo.virus.x * 32, mundo.virus.y * 32);

        // Dibujar todos los malwares activos (antes gusanos)
        for (Elementos malware : mundo.getMalwares()) {
            g.drawPixmap(Assets.malware, malware.x * 32, malware.y * 32);
        }

        // Dibujar las calaveras (antes obstáculos)
        for (Obstaculo calavera : mundo.getCalaveras()) {
            Pixmap calaveraPixmap = null;

            // Asignar la imagen correspondiente según el tipo
            if (calavera.tipo == 1) {
                calaveraPixmap = Assets.calavera1;
            } else if (calavera.tipo == 2) {
                calaveraPixmap = Assets.calavera2;
            }

            if (calaveraPixmap != null) {
                g.drawPixmap(calaveraPixmap, calavera.x * 32, calavera.y * 32);
            }
        }

        // Dibujar la cola del hacker
        for (int i = 1; i < jollyroger.partes.size(); i++) {
            Escudos part = jollyroger.partes.get(i);
            g.drawPixmap(Assets.escudo, part.x * 32, part.y * 32);
        }

        // Dibujar la cabeza del hacker
        Pixmap headPixmap = null;
        if (jollyroger.direccion == Hacker.ARRIBA)
            headPixmap = Assets.hackerarriba;
        if (jollyroger.direccion == Hacker.IZQUIERDA)
            headPixmap = Assets.hackerizquierda;
        if (jollyroger.direccion == Hacker.ABAJO)
            headPixmap = Assets.hackerabajo;
        if (jollyroger.direccion == Hacker.DERECHA)
            headPixmap = Assets.hackerderecha;

        if (headPixmap != null) {
            g.drawPixmap(headPixmap, jollyroger.partes.get(0).x * 32, jollyroger.partes.get(0).y * 32);
        }
    }


    private void drawReadyUI() {
        Graficos g = juego.getGraphics();
        Pixmap preparadoPixmap = modoExtremo ? Assets.preparadoE : Assets.preparadoN;
        g.drawPixmap(preparadoPixmap, 10, 40);
        g.drawLine(0, 416, 480, 416, 0xFF00F5FF); // Cyan cyberpunk
    }

    private void drawRunningUI() {
        Graficos g = juego.getGraphics();
        g.drawPixmap(Assets.botones, 0, 1, 68, 128, 66, 66);
        g.drawLine(0, 416, 480, 416, 0xFF00F5FF); // Cyan cyberpunk
        g.drawPixmap(Assets.botones, 0, 416, 68, 64, 66, 66);
        g.drawPixmap(Assets.botones, 256, 416, 5, 64, 66, 66);
    }

    private void drawPausedUI() {
        Graficos g = juego.getGraphics();
        g.drawPixmap(Assets.menupausa, 60, 120);
        g.drawLine(0, 416, 480, 416, 0xFF39FF14); // Verde neón
    }

    private void drawGameOverUI() {
        Graficos g = juego.getGraphics();
        g.drawPixmap(Assets.finjuego, 36, 100);
        g.drawPixmap(Assets.botones, 128, 200, 5, 128, 66, 66);
        g.drawLine(0, 416, 480, 416, 0xFFFF006E); // Rosa cyberpunk
    }

    public void drawText(Graficos g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);

            if (character == ' ') {
                x += 30;
                continue;
            }

            int srcX;
            int srcWidth;

            if (character == '.') {
                srcX = 327;
                srcWidth = 15;
            } else {
                srcX = (character - '0') * 32;
                srcWidth = 32;
            }

            g.drawPixmap(Assets.numeros, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }

    @Override
    public void pause() {
        if(estado == EstadoJuego.Ejecutandose)
            estado = EstadoJuego.Pausado;

        if(mundo.finalJuego) {
            Configuraciones.addScore(mundo.puntuacion);
            Configuraciones.save(juego.getFileIO());
        }
    }

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}