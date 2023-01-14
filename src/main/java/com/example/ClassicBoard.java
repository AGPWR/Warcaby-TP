package com.example;

import java.io.PrintWriter;

public class ClassicBoard extends Board {

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