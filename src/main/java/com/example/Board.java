package com.example;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class Board {

  public final static int TILE_SIZE = 75;
  public int WIDTH = 8;
  public int HEIGHT = 8;
  private final Parent content;
  private Tile[][] board = new Tile[WIDTH][HEIGHT];

  private final Group tileGroup = new Group();
  private final Group pieceGroup = new Group();

  public Board(){
      this.content = createContent();
  }

  private Parent createContent(){
    Pane root = new Pane();
    root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
    root.getChildren().addAll(tileGroup, pieceGroup);
    for (int y = 0; y < HEIGHT; y++){
      for (int x = 0; x < WIDTH; x++){
        Tile tile = new Tile((x + y) % 2 == 0, x, y);
        board[x][y] = tile;

        tileGroup.getChildren().add(tile);

        Piece piece = null;
        if(y <= 2 && (x+y)%2 != 0){
          piece = makePiece(PieceType.RED, x, y);
        }
        if(y >= 5 && (x+y)%2 != 0){
          piece = makePiece(PieceType.WHITE, x, y);
        }
        if(piece != null) {
          tile.setPiece(piece);
          pieceGroup.getChildren().add(piece);
        }
      }
    }
    return root;
  }

  public Parent getContent(){
    return this.content;
  }

  private Piece makePiece(PieceType type, int x, int y){
    return new Piece(type, x, y);
  }

}
