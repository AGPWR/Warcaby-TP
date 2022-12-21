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

  public Board() {
    this.content = createContent();
  }

  private Parent createContent() {
    Pane root = new Pane();
    root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
    root.getChildren().addAll(tileGroup, pieceGroup);
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        Tile tile = new Tile((x + y) % 2 == 0, x, y);
        board[x][y] = tile;

        tileGroup.getChildren().add(tile);

        Piece piece = null;
        if (y <= 2 && (x + y) % 2 != 0) {
          piece = makePiece(PieceType.RED, x, y);
        }
        if (y >= 5 && (x + y) % 2 != 0) {
          piece = makePiece(PieceType.WHITE, x, y);
        }
        if (piece != null) {
          tile.setPiece(piece);
          pieceGroup.getChildren().add(piece);
        }
      }
    }
    return root;
  }


  public Parent getContent() {
    return this.content;
  }

  private Piece makePiece(PieceType type, int x, int y) {
    Piece piece = new Piece(type, x, y);
    piece.setOnMouseClicked(e -> {
      int newX = toBoard(piece.getLayoutX());
      int newY = toBoard(piece.getLayoutY());
      MoveResult result = tryMove(piece, newX, newY);
      int x0 = toBoard(piece.getOldX());
      int y0 = toBoard(piece.getOldY());

      switch (result.getType()) {
        case NONE:
          piece.abortMove();
          break;
        case NORMAL:
          piece.move(newX, newY);
          board[x0][y0].setPiece(null);
          board[newX][newY].setPiece(piece);
          if ((newY == 0 || newY == 7)) {
            piece.change();
          }

          break;
        case KILL:
          piece.move(newX, newY);
          board[x0][y0].setPiece(null);
          board[newX][newY].setPiece(piece);
          if ((newY == 0 && piece.getType() == PieceType.WHITE) || (newY == 7 && piece.getType() == PieceType.RED)) {
            piece.change();
          }
          Piece otherPiece = result.getPiece();
          board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
          pieceGroup.getChildren().remove(otherPiece);
          break;
      }
    });
    return piece;
  }


  private MoveResult tryMove(Piece piece, int newX, int newY) {

    if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
      return new MoveResult(MoveType.NONE);
    }
    int x0 = toBoard(piece.getOldX());
    int y0 = toBoard(piece.getOldY());
    if (!piece.isQueen()) {


      if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir) {
        return new MoveResult(MoveType.NORMAL);

      } else if ((Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDir * 2) || (Math.abs(newX - x0) == 2 && y0 - newY == piece.getType().moveDir * 2)) {
        int x1 = x0 + (newX - x0) / 2;
        int y1 = y0 + (newY - y0) / 2;

        if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
          return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
        }
      }
    } else {


      if (canQueenMove(x0, y0, newX, newY)) {
        return new MoveResult(MoveType.NORMAL);
      }
      if (canQueenKill(x0, y0, newX, newY)) {
        int[] x = findPiece(x0, y0, newX, newY);
        if (board[x[0]][x[1]].getPiece().getType() != piece.getType()) {
          return new MoveResult(MoveType.KILL, board[x[0]][x[1]].getPiece());
        }
      }
    }


    return new MoveResult(MoveType.NONE);


  }


  private Boolean canQueenMove(int x, int y, int newX, int newY) {
    return !isInPlus(x, y, newX, newY) && isInCross(x, y, newX, newY) && countPiecesInDiagonal(x, y, newX, newY) == 0;
  }

  private Boolean canQueenKill(int x, int y, int newX, int newY) {
    return !isInPlus(x, y, newX, newY) && countPiecesInDiagonal(x, y, newX, newY) == 1;
  }


  private Boolean isInPlus(int x0, int y0, int x1, int y1) {
    return x0 == x1 || y0 == y1;
  }

  private Boolean isInCross(int x0, int y0, int x1, int y1) {
    if (Math.abs(x0 - y0) == Math.abs(x1 - y1))
      return true;
    return x0 + y0 == x1 + y1;
  }

  public int[] findPiece(int x0, int y0, int x1, int y1) {
    int[] coordinates = new int[2];

    if (Math.abs(x0 - x1) == Math.abs(y0 - y1)) {
      int xStep = (x1 > x0) ? 1 : -1;
      int yStep = (y1 > y0) ? 1 : -1;

      int x = x0;
      int y = y0;
      while (x != x1 && y != y1) {
        if (!(x == x0 && y == y0)) {
          if (board[x][y].hasPiece()) {
            coordinates[0] = x;
            coordinates[1] = y;
            break;
          }
        }
        x += xStep;
        y += yStep;
      }
    }
    return coordinates;
  }


  public int countPiecesInDiagonal(int x0, int y0, int x1, int y1) {
    int count = 0;

    if (Math.abs(x0 - x1) == Math.abs(y0 - y1)) {
      int xStep = (x1 > x0) ? 1 : -1;
      int yStep = (y1 > y0) ? 1 : -1;

      int x = x0;
      int y = y0;
      while (x != x1 && y != y1) {
        if (!(x == x0 && y == y0)) {
          if (board[x][y].hasPiece()) {
            count++;
          }
        }
        x += xStep;
        y += yStep;
      }
    }

    return count;
  }

  private int toBoard(double pixel) {
    return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
  }
}
