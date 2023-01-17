package com.example;

import java.io.PrintWriter;

/**
 * Fabryka plansz.
 */
public interface BoardFactory {
    /**
     * Metoda zwracaja startowa plansze danego trybu gry.
     * @param boardType tryb gry
     * @param Player gracz
     * @param out stumien wyjsciowy
     * @return plansza danego typu
     */
    Board getBoard(String boardType, int Player, PrintWriter out);
}
