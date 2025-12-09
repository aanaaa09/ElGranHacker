package com.ldm.elGranHacker.juego;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.ldm.elGranHacker.FileIO;

public class Configuraciones {
    public static boolean sonidoHabilitado = true;
    public static int[] maxPuntuaciones = new int[] { 100, 80, 50, 30, 10 };

    public static void cargar(FileIO files) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(files.leerArchivo(".configHacker")))) {
            sonidoHabilitado = Boolean.parseBoolean(in.readLine());
            for (int i = 0; i < 5; i++) {
                maxPuntuaciones[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de configuración: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear una puntuación: " + e.getMessage());
        }
    }

    public static void save(FileIO files) {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(files.escribirArchivo(".configHacker")))) {
            out.write(Boolean.toString(sonidoHabilitado));
            out.write("\n");
            for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(maxPuntuaciones[i]));
                out.write("\n");
            }

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de configuración: " + e.getMessage());
        }
    }

    public static void addScore(int score) {
        for (int i = 0; i < 5; i++) {
            if (maxPuntuaciones[i] < score) {
                for (int j = 4; j > i; j--)
                    maxPuntuaciones[j] = maxPuntuaciones[j - 1];
                maxPuntuaciones[i] = score;
                break;
            }
        }
    }
}