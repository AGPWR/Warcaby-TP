package com.example;

/**
 * Klasa definuijaca wynik ruchu.
 */
public class MoveResult {
  /**
   * Typ ruchu.
   */
  public MoveType type;
  /**
   * Pionek.
   */
  private Piece piece;

  /**
   * Konstruktor.
   * @param type typ ruchu
   * @param piece pionek
   */
  public MoveResult(MoveType type, Piece piece) {
    this.type = type;
    this.piece = piece;
  }

  /**
   * Konstruktor
   * @param type tyb ruchu
   */
  public MoveResult(MoveType type) {
    this(type, null);
  }

  /**
   * Metoda zwracajaca pionka.
   * @return pionek
   */
  public Piece getPiece() {
    return piece;
  }

  /**
   * Metoda zwracajaca typ ruchu.
   * @return typ ruchu.
   */
  public MoveType getType() {
    return type;
  }

}
