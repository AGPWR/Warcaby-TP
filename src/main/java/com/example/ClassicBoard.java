package com.example;

import java.io.PrintWriter;

/**
 * Klasa klasycznej planszy warcab.
 */
public class ClassicBoard extends Board {
  /**
   * Konstruktor klasycznej planszy warcab.
   * @param Player gracz
   * @param out stumien wyjsciowy
   */
  public ClassicBoard(int Player, PrintWriter out) {
    super(8, 8);
    this.Player = Player;
    this.out = out;
    if (Player == 1) {
      this.content = createContent(PieceType.RED, PieceType.WHITE);
    } else {
      this.content = createContent(PieceType.WHITE, PieceType.RED);
    }
  }
}