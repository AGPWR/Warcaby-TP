package com.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
  private Piece piece;

  public Tile(boolean isWhite, int x, int y) {
    setWidth(ClassicBoard.TILE_SIZE);
    setHeight(ClassicBoard.TILE_SIZE);

    relocate(x * ClassicBoard.TILE_SIZE, y * ClassicBoard.TILE_SIZE);

    setFill(isWhite ? Color.valueOf("#feb") : Color.valueOf("582"));
  }

  public boolean hasPiece() {
    return piece != null;
  }

  public Piece getPiece() {
    return piece;
  }

  public void setPiece(Piece piece) {
    this.piece = piece;
  }
}
