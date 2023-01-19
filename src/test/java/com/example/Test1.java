package com.example;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class Test1 extends Board{
  protected Test1() {
    super(8, 8);
    this.Player=1;
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

        if ((x==6 && y==3) || (x==4 && y==3)) {
          piece = makePiece(player1, x, y);
          firstPlayerPieces++;
        }
        if (x==7 && y==4){
          piece = makePiece(player2, x, y);
          secondPlayerPieces++;
        }
        if((x==1 && y==4)||(x==2 && y==5)){
          piece= makePiece(player2,x,y);
          secondPlayerPieces++;
          piece.change();
        }

        if (piece != null) {
          tile.setPiece(piece);
          pieceGroup.getChildren().add(piece);
        }
      }
    }
    return root;
  }


}