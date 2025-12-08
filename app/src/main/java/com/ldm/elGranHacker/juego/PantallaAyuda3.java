package com.ldm.elGranHacker.juego;

import java.util.List;

import com.ldm.elGranHacker.Juego;
import com.ldm.elGranHacker.Graficos;
import com.ldm.elGranHacker.Input.TouchEvent;
import com.ldm.elGranHacker.Pantalla;

public class PantallaAyuda3 extends Pantalla {
    public PantallaAyuda3(Juego juego) {
        super(juego);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = juego.getInput().getTouchEvents();
        juego.getInput().getKeyEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 256 && event.y > 416 ) {
                    juego.setScreen(new MainMenuScreen(juego));
                    if(Configuraciones.sonidoHabilitado)
                        Assets.clic.play(1);
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graficos g = juego.getGraphics();
        g.drawPixmap(Assets.fondo, 0, 0);
        g.drawPixmap(Assets.ayuda3, 40, 10);
        g.drawPixmap(Assets.botones, 252, 420, 5, 128, 66, 66);
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