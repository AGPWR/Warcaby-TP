package com.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Klasa pola planszy.
 */
public class Tile extends Rectangle {
  /**
   * Pole pionka w polu.
   */
  private Piece piece;

  /**
   * Konstruktor pola.
   * @param isWhite czy pole biale
   * @param x wspolrzedna x pola
   * @param y wspolrzedna y pola
   */
  public Tile(boolean isWhite, int x, int y) {
    setWidth(ClassicBoard.TILE_SIZE);
    setHeight(ClassicBoard.TILE_SIZE);

    relocate(x * ClassicBoard.TILE_SIZE, y * ClassicBoard.TILE_SIZE);

    setFill(isWhite ? Color.valueOf("#feb") : Color.valueOf("582"));
  }

  /**
   * Meotda sprawdzajaca czy pionek jest na polu.
   * @return true or false
   */
  public boolean hasPiece() {
    return piece != null;
  }

  /**
   * Metoda pobierajaca pionka na polu.
   * @return
   */
  public Piece getPiece() {
    return piece;
  }

  /**
   * Metoda ustwia pionka na polu.
   * @param piece
   */
  public void setPiece(Piece piece) {
    this.piece = piece;
  }
}
