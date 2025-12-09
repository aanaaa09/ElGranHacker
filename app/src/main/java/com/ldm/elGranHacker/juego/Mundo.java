package com.ldm.elGranHacker.juego;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Mundo {
    static final int MUNDO_ANCHO = 10;
    static final int MUNDO_ALTO = 13;
    static final float TICK_INICIAL = 0.5f;
    static final float TICK_DECREMENTO = 0.05f;
    private static final float DURACION_MALWARE = 10.0f;
    private static final float DURACION_CALAVERA = 10.0f;

    public Hacker hacker;
    public Elementos virus;
    public boolean finalJuego = false;
    public int puntuacion = 0;
    private boolean malwareComido = false;

    private List<Obstaculo> calaveras = new ArrayList<>();
    private List<Elementos> malwares = new ArrayList<>();
    private boolean modoExtremo;
    private Random random = new Random();

    float tiempoTick = 0;
    static float tick = TICK_INICIAL;

    public Mundo(boolean modoExtremo) {
        this.modoExtremo = modoExtremo;
        hacker = new Hacker();

        do {
            colocarVirus();
        } while (virus.x == hacker.partes.get(0).x && virus.y == hacker.partes.get(0).y);
    }

    public List<Elementos> getMalwares() {
        return malwares;
    }

    public void update(float deltaTime) {
        if (finalJuego) return;

        tiempoTick += deltaTime;

        while (tiempoTick > tick) {
            tiempoTick -= tick;
            hacker.avance();

            // Verificar colisiones del hacker con su propia cola
            if (hacker.comprobarChoque()) {
                finalJuego = true;
                return;
            }

            Escudos head = hacker.partes.get(0);

            if (head.x == virus.x && head.y == virus.y) {
                manejarVirus();
            }

            // Manejar malwares
            for (int i = malwares.size() - 1; i >= 0; i--) {
                Elementos malware = malwares.get(i);
                if (head.x == malware.x && head.y == malware.y) {
                    manejarMalware(i);
                }
            }

            // Manejar calaveras
            for (int i = calaveras.size() - 1; i >= 0; i--) {
                Obstaculo calavera = calaveras.get(i);
                if (head.x == calavera.x && head.y == calavera.y) {
                    if (Configuraciones.sonidoHabilitado && Assets.error != null) {
                        Assets.error.play(1);
                    }
                    finalJuego = true;
                    calaveras.remove(i);
                    return;
                }
            }
        }

        // Generar malwares
        if (modoExtremo) {
            if (random.nextInt(1000) < 5) {
                generarMalware();
            }
        } else {
            if (malwares.isEmpty() && random.nextInt(1000) < 3) {
                generarMalware();
            }
        }

        // Generar calaveras
        if (modoExtremo) {
            if (random.nextInt(1000) < 3) {
                generarCalavera();
            }
        } else {
            if (calaveras.isEmpty() && random.nextInt(1000) < 1) {
                generarCalavera();
            }
        }

        // Controlar tiempo de vida de los malwares
        for (int i = malwares.size() - 1; i >= 0; i--) {
            Elementos malware = malwares.get(i);
            malware.tiempoVida += deltaTime;
            if (malware.tiempoVida > DURACION_MALWARE) {
                malwares.remove(i);
            }
        }

        // Controlar tiempo de vida de las calaveras
        for (int i = calaveras.size() - 1; i >= 0; i--) {
            Obstaculo calavera = calaveras.get(i);
            calavera.tiempoVida += deltaTime;
            if (calavera.tiempoVida > DURACION_CALAVERA) {
                calaveras.remove(i);
            }
        }
    }

    private void manejarVirus() {
        switch (virus.tipo) {
            case Elementos.TIPO_1: // virus_5ptos
                puntuacion += 5;
                break;
            case Elementos.TIPO_2: // virus_7ptos
                puntuacion += 7;
                break;
            case Elementos.TIPO_3: // virus_10ptos
                puntuacion += 10;
                // Aumentar la velocidad del juego
                if (tick > 0.1f) {
                    tick -= 0.05f;
                }
                break;
            case Elementos.TIPO_4: // escudoDorado
                puntuacion += 20;
                hacker.anadirEscudo();
                break;
        }

        hacker.anadirEscudo();
        colocarVirus();

        // Incremento de velocidad por puntuación
        if (modoExtremo) {
            if (puntuacion % 20 == 0 && puntuacion > 0 && tick - TICK_DECREMENTO > 0) {
                tick -= TICK_DECREMENTO;
            }
        } else if (puntuacion % 40 == 0 && puntuacion > 0 && tick - TICK_DECREMENTO > 0) {
            tick -= TICK_DECREMENTO;
        }
    }

    private void manejarMalware(int index) {
        puntuacion -= 10;
        if (puntuacion < 0) puntuacion = 0;

        hacker.anadirEscudo();
        malwares.remove(index);
        malwareComido = true;

        if (Configuraciones.sonidoHabilitado) {
            Assets.virus.play(1);
        }
    }

    public boolean isMalwareActivo() {
        return !malwares.isEmpty();
    }

    private void generarMalware() {
        int malwareX, malwareY;
        do {
            malwareX = random.nextInt(MUNDO_ANCHO);
            malwareY = random.nextInt(MUNDO_ALTO);
        } while (camposOcupados(malwareX, malwareY));

        Elementos nuevoMalware = new Elementos(malwareX, malwareY, Elementos.TIPO_GUSANO);
        malwares.add(nuevoMalware);
    }

    public boolean malwareFueComido() {
        boolean fueComido = malwareComido;
        malwareComido = false;
        return fueComido;
    }

    private void generarCalavera() {
        int calaveraX, calaveraY;
        int intentos = 0;

        do {
            calaveraX = random.nextInt(MUNDO_ANCHO);
            calaveraY = random.nextInt(MUNDO_ALTO);
            intentos++;
        } while (camposOcupados(calaveraX, calaveraY) && intentos < 100);

        if (intentos >= 100) {
            return;
        }

        // Generar un tipo aleatorio (1 = calavera1, 2 = calavera2)
        int tipo = random.nextInt(2) + 1;

        Obstaculo nuevaCalavera = new Obstaculo(calaveraX, calaveraY, tipo);
        calaveras.add(nuevaCalavera);
    }

    public List<Obstaculo> getCalaveras() {
        return calaveras;
    }

    private boolean camposOcupados(int x, int y) {
        if (virus != null && virus.x == x && virus.y == y) {
            return true;
        }

        for (Escudos parte : hacker.partes) {
            if (parte.x == x && parte.y == y) {
                return true;
            }
        }

        for (Elementos malware : malwares) {
            if (malware.x == x && malware.y == y) {
                return true;
            }
        }

        for (Obstaculo calavera : calaveras) {
            if (calavera.x == x && calavera.y == y) {
                return true;
            }
        }

        return false;
    }

    private void colocarVirus() {
        int virusX, virusY;
        do {
            virusX = random.nextInt(MUNDO_ANCHO);
            virusY = random.nextInt(MUNDO_ALTO);
        } while (camposOcupados(virusX, virusY));

        int probabilidad = random.nextInt(100);
        int tipoVirus;

        if (probabilidad < 40) {
            tipoVirus = Elementos.TIPO_1; // virus_5ptos (40%)
        } else if (probabilidad < 70) {
            tipoVirus = Elementos.TIPO_2; // virus_7ptos (30%)
        } else if (probabilidad < 90) {
            tipoVirus = Elementos.TIPO_3; // virus_10ptos (20%)
        } else {
            tipoVirus = Elementos.TIPO_4; // escudoDorado (10%)
        }

        virus = new Elementos(virusX, virusY, tipoVirus);
    }
}