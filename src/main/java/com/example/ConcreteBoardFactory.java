package com.example;
import java.io.PrintWriter;

/**
 * Fabryka tworzaca plasze danego trybu.
 */
public class ConcreteBoardFactory implements BoardFactory{

    /**
     * Metoda zwracajaca plansze danego typu.
     * @param boardType tryb gry
     * @param Player gracz
     * @param out stumien wyjsciowy
     * @return plansza danego typu
     */
    @Override
    public Board getBoard(String boardType, int Player, PrintWriter out) {
        if(boardType.equalsIgnoreCase("CLASSIC")){
            return new ClassicBoard(Player, out);
        }
        if(boardType.equalsIgnoreCase("POLISH")){
            return new PolishBoard(Player, out);
        }
        if(boardType.equalsIgnoreCase("RUSSIAN")){
            return new RussianBoard(Player, out);
        }
        return null;
    }
}
