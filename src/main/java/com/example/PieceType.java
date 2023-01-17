package com.example;

/**
 * Mozliwe typu pionkow.
 */
public enum PieceType {
  RED(1), WHITE(-1);
  /**
   * Pole kierunku ruchu pionka.
   */
  int moveDir;

  PieceType(int moveDir) {
    this.moveDir = moveDir;
  }
}
