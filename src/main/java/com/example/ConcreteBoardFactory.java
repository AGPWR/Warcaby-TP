package com.example;
import java.io.PrintWriter;

public class ConcreteBoardFactory implements BoardFactory{


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
