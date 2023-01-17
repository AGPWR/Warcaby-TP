package com.example;

import java.io.PrintWriter;

/**
 * Klasa trybu polskiego warcab.
 */
public class PolishBoard extends Board {
    /**
     * KOnstruktor planszy trybu polskiego.
     * @param Player gracz
     * @param out strumien wyjsciowy
     */
    public PolishBoard(int Player, PrintWriter out) {
        super(10, 10);
        this.Player = Player;
        this.out = out;
        if (Player == 1) {
            this.content = createContent(PieceType.RED, PieceType.WHITE);
        } else {
            this.content = createContent(PieceType.WHITE, PieceType.RED);
        }
  }
}