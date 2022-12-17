package com.example;

import javafx.scene.layout.GridPane;

public class Gboard {

  private Board board;
  private final int rowsNumber;
  GridPane gridPane;

  private void insert() {
    for (int i = 0; i < rowsNumber; i++) {
      for (int j = 0; j < rowsNumber; j++) {
        Square square = new Square();
        square.setColor(board.getColor(i, j));
        gridPane.add(square.square, j, i, 1, 1);
      }
    }
  }


  Gboard(int n) {
    this.gridPane = new GridPane();
    this.rowsNumber = n;
    this.board = new Board(n);
    insert();

  }


}
