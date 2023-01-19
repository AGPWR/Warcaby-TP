package com.example;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class Test2 extends Board{
  boolean isPone;
  protected Test2(boolean x) {
    super(8, 8);
    this.Player=1;
    this.isPone=x;
    this.content = createContent(PieceType.RED, PieceType.WHITE);
  }

  @Override
  protected Parent createContent(PieceType player1, PieceType player2) {
    Pane root = new Pane();
    root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
    root.getChildren().addAll(tileGroup, pieceGroup);
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        Tile tile = new Tile((x + y) % 2 == 0, x, y);
        board[x][y] = tile;
        tileGroup.getChildren().add(tile);
        Piece piece = null;

        if ((x==0 && y==3) && isPone) {
          piece = makePiece(player2, x, y);
          secondPlayerPieces++;
        }
        if ((x==1 && y==2) || (x==2 && y==1)){
          piece = makePiece(player1, x, y);
          firstPlayerPieces++;
        }

        if (piece != null) {
          tile.setPiece(piece);
          pieceGroup.getChildren().add(piece);
        }
      }
    }
    return root;
  }

  @Override
  public void checkIfEnd() {
    if (!canPlayerMove(1)) {
      firstPlayerPieces = 0;
    } else {
      PieceType turnClone = turn;
      turn = (turn == PieceType.WHITE) ? PieceType.RED : PieceType.WHITE;
      if (!canPlayerMove(1)) {
        firstPlayerPieces = 0;
      }
      turn = turnClone;
    }
    if (!canPlayerMove(2)) {
      secondPlayerPieces = 0;
    } else {
      PieceType turnClone = turn;
      turn = (turn == PieceType.WHITE) ? PieceType.RED : PieceType.WHITE;
      if (!canPlayerMove(2)) {
        secondPlayerPieces = 0;
      }
      turn = turnClone;
    }
    if (firstPlayerPieces == 0) {
      //if(Player==1)
      PieceType p = (Player == 1) ? PieceType.WHITE : PieceType.RED;
      //send("REDWON");
      redWon = true;
      //send("t");
    } else if (secondPlayerPieces == 0) {
      PieceType p = (Player == 1) ? PieceType.WHITE : PieceType.RED;
      //send("WHITEWON");
      whiteWon = true;
      //send("t");
    }
  }
}
