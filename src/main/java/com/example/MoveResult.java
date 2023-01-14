package com.example;

public class MoveResult {
  public MoveType type;
  private Piece piece;

  public MoveResult(MoveType type, Piece piece) {
    this.type = type;
    this.piece = piece;
  }

  public MoveResult(MoveType type) {
    this(type, null);
  }

  public Piece getPiece() {
    return piece;
  }

  public MoveType getType() {
    return type;
  }


}
