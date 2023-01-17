package com.example;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import java.io.PrintWriter;

public class PolishBoard extends Board {

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