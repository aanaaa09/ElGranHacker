package com.ldm.elGranHacker.juego;

import java.util.Random;

import java.util.ArrayList;
import java.util.List;

public class Mundo {
    static final int MUNDO_ANCHO = 10;
    static final int MUNDO_ALTO = 13;
    //static final int INCREMENTO_PUNTUACION = 10;
    static final float TICK_INICIAL = 0.5f;
    static final float TICK_DECREMENTO = 0.05f;
    private static final float DURACION_GUSANO = 10.0f;
    private static final float DURACION_OBJETO = 10.0f;

    public Hacker jollyroger;
    public Elementos ingredientes;
    public boolean finalJuego = false;
    public int puntuacion = 0;
    private boolean gusanoComido = false;

    private List<Obstaculo> obstaculos = new ArrayList<>();
    private List<Elementos> gusanos = new ArrayList<>(); // Lista para múltiples gusanos
    private boolean modoExtremo; // Bandera para distinguir entre modos
    private Random random = new Random();

    float tiempoTick = 0;
    static float tick = TICK_INICIAL;

    public Mundo(boolean modoExtremo) {
        this.modoExtremo = modoExtremo; // Almacena si es modo extremo o no
        jollyroger = new Hacker();

        do {
            colocarIngredientes(); // Coloca el primer botín
        } while (ingredientes.x == jollyroger.partes.get(0).x && ingredientes.y == jollyroger.partes.get(0).y);
    }

    public List<Elementos> getGusanos() {
        return gusanos;
    }

    public void update(float deltaTime) {
        if (finalJuego) return;

        tiempoTick += deltaTime;

        while (tiempoTick > tick) {
            tiempoTick -= tick;
            jollyroger.avance();

            // Verificar colisiones del chef con su propia cola
            if (jollyroger.comprobarChoque()) {
                finalJuego = true;
                return;
            }

            Escudos head = jollyroger.partes.get(0);

            if (head.x == ingredientes.x && head.y == ingredientes.y) {
                manejarIngredientes();
            }

            // Manejar gusanos
            for (int i = gusanos.size() - 1; i >= 0; i--) {
                Elementos gusano = gusanos.get(i);
                if (head.x == gusano.x && head.y == gusano.y) {
                    manejarGusano(i);
                }
            }
            for (int i = obstaculos.size() - 1; i >= 0; i--) {
                Obstaculo objeto = obstaculos.get(i);
                if (head.x == objeto.x && head.y == objeto.y) {
                    // Reproducir el sonido de plato roto
                    if (Configuraciones.sonidoHabilitado && Assets.error != null) {
                        Assets.error.play(1); // Corrige el error tipográfico
                    }

                    // Terminar el juego
                    finalJuego = true;

                    // Eliminar el obstáculo (opcional)
                    obstaculos.remove(i);
                    return;
                }
            }
        }

        // Generar cosas
        if (modoExtremo) {
            if (random.nextInt(1000) < 5) { // Mayor probabilidad en modo extremo
                generarGusano();
            }
        } else {
            if (gusanos.isEmpty() && random.nextInt(1000) < 3) { // Menor probabilidad en modo normal
                generarGusano();
            }
        }

        if (modoExtremo) {
            if (random.nextInt(1000) < 3) { // Mayor probabilidad en modo extremo
                generarObstaculo();
            }
        } else {
            if (obstaculos.isEmpty() && random.nextInt(1000) < 1) { // Menor probabilidad en modo normal
                generarObstaculo();
            }
        }

        // Controlar tiempo de vida de los gusanos
        for (int i = gusanos.size() - 1; i >= 0; i--) {
            Elementos gusano = gusanos.get(i);
            gusano.tiempoVida += deltaTime;
            if (gusano.tiempoVida > DURACION_GUSANO) {
                gusanos.remove(i); // Eliminar gusano si se acaba el tiempo
            }
        }
        for (int i = obstaculos.size() - 1; i >= 0; i--) {
            Obstaculo objeto = obstaculos.get(i);
            objeto.tiempoVida += deltaTime;
            if (objeto.tiempoVida > DURACION_OBJETO) {
                obstaculos.remove(i); // Eliminar objeto si se acaba el tiempo
            }
        }
    }

    private void manejarIngredientes() {
        switch (ingredientes.tipo) {
            case Elementos.TIPO_1: // Tomate
                puntuacion += 5;
                break;
            case Elementos.TIPO_2: // Lechuga
                puntuacion += 7;
                break;
            case Elementos.TIPO_3: // Queso
                puntuacion += 10;
                // Aumentar la velocidad del juego al tocar el queso
                if (tick > 0.1f) {
                    tick -= 0.05f;
                    break;
                }
            case Elementos.TIPO_4: // Campana dorada
                puntuacion += 20;
                jollyroger.anadirEscudo();
                break;
        }

        jollyroger.anadirEscudo();
        colocarIngredientes();

        // Incremento de velocidad por puntuación
        if (modoExtremo) {
            if (puntuacion % 20 == 0 && puntuacion > 0 && tick - TICK_DECREMENTO > 0) {
                tick -= TICK_DECREMENTO;
            }
        } else if (puntuacion % 40 == 0 && puntuacion > 0 && tick - TICK_DECREMENTO > 0) {
            tick -= TICK_DECREMENTO;
        }
    }

    private void manejarGusano(int index) {
        puntuacion -= 10;
        if (puntuacion < 0) puntuacion = 0;

        jollyroger.anadirEscudo();
        gusanos.remove(index); // Elimina el gusano de la lista
        gusanoComido = true;

        // Reproducir sonido de gusano
        if (Configuraciones.sonidoHabilitado) {
            Assets.virus.play(1);
        }
    }

    public boolean isGusanoActivo() {
        return !gusanos.isEmpty(); // Devuelve true si hay al menos un gusano en la lista
    }


    private void generarGusano() {
        int gusanoX, gusanoY;
        do {
            gusanoX = random.nextInt(MUNDO_ANCHO);
            gusanoY = random.nextInt(MUNDO_ALTO);
        } while (camposOcupados(gusanoX, gusanoY)); // Evita colocar sobre otros objetos

        Elementos nuevoGusano = new Elementos(gusanoX, gusanoY, Elementos.TIPO_GUSANO);
        gusanos.add(nuevoGusano);
    }

    public boolean gusanoFueComido() {
        boolean fueComido = gusanoComido;
        gusanoComido = false; // Resetea el estado después de leerlo
        return fueComido;
    }


    private void generarObstaculo() {
        int obstaculoX, obstaculoY;
        int intentos = 0; // Limitar los intentos para evitar bucles infinitos

        do {
            obstaculoX = random.nextInt(MUNDO_ANCHO);
            obstaculoY = random.nextInt(MUNDO_ALTO);
            intentos++;
        } while (camposOcupados(obstaculoX, obstaculoY) && intentos < 100);

        // Si no se encontró un espacio libre después de 100 intentos, no se genera el obstáculo
        if (intentos >= 100) {
            return;
        }

        // Generar un tipo aleatorio (1 = cuchillo, 2 = tenedor)
        int tipo = random.nextInt(2) + 1;

        // Crear el obstáculo con las coordenadas y el tipo asignado
        Obstaculo nuevoObstaculo = new Obstaculo(obstaculoX, obstaculoY, tipo);
        obstaculos.add(nuevoObstaculo);
    }



    public List<Obstaculo> getObstaculos() {
        return obstaculos;
    }

    private boolean camposOcupados(int x, int y) {

        if (ingredientes != null && ingredientes.x == x && ingredientes.y == y) {
            return true;
        }

        for (Escudos parte : jollyroger.partes) {
            if (parte.x == x && parte.y == y) {
                return true;
            }
        }

        for (Elementos gusano : gusanos) {
            if (gusano.x == x && gusano.y == y) {
                return true;
            }
        }

        for (Obstaculo obstaculo : obstaculos) {
            if (obstaculo.x == x && obstaculo.y == y) {
                return true;
            }
        }

        return false;
    }

    private void colocarIngredientes() {
        int ingredienteX, ingredienteY;
        do {
            ingredienteX = random.nextInt(MUNDO_ANCHO);
            ingredienteY = random.nextInt(MUNDO_ALTO);
        } while (camposOcupados(ingredienteX, ingredienteY));

        int probabilidad = random.nextInt(100);
        int tipoIngrediente;

        if (probabilidad < 40) {
            tipoIngrediente = Elementos.TIPO_1; // Tomate (40%)
        } else if (probabilidad < 70) {
            tipoIngrediente = Elementos.TIPO_2; // Lechuga (30%)
        } else if (probabilidad < 90) {
            tipoIngrediente = Elementos.TIPO_3; // Queso (20%)
        } else {
            tipoIngrediente = Elementos.TIPO_4; // Campana dorada (10%)
        }

        ingredientes = new Elementos(ingredienteX, ingredienteY, tipoIngrediente);
    }

}
