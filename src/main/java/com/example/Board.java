package com.example;

import java.io.PrintWriter;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 * Abstrakcyjna klasa implementujaca plansze do gry z podstawowymi zasadami.
 */
abstract class Board {
  /**
   * Pole przechowujace kto ma aktualny ruch.
   */
  public PieceType turn = PieceType.WHITE;
  /**
   * Szerokosc planszy.
   */
  protected int WIDTH;
  /**
   * Wysokosc planszy.
   */
  protected int HEIGHT;
  /**
   * Tablica skladająca się z pól planszy.
   */
  protected Tile[][] board;
  /**
   * Cała plansza z pionkami.
   */
  protected Parent content;
  /**
   * Pole przechowujace czy czerwony wygral.
   */
  public boolean redWon = false;
  /**
   * Pole przechowujace czy bialy wygral.
   */
  public boolean whiteWon = false;
  /**
   * Strumien wyjsciowy do serwera.
   */
  protected PrintWriter out;
  /**
   * Pole zawierające ostatni kierunek poruszania sie damy.
   */
  protected int LastQDirection = 0;
  /**
   * Gracz.
   */
  public int Player;
  /**
   * Stala wielkosc pola w pikselach.
   */
  protected final static int TILE_SIZE = 75;

  /**
   * Konstruktor planszy.
   * @param width wysokosc
   * @param height szerokosc
   */
  protected Board(int width, int height) {
    this.WIDTH = width;
    this.HEIGHT = height;
    this.board = new Tile[this.WIDTH][this.HEIGHT];
  }

  /**
   * Ilosc pionkow pierszego gracza.
   */
  protected int firstPlayerPieces = 0;
  /**
   * Ilosc pionkow drugiego gracza.
   */
  protected int secondPlayerPieces = 0;
  /**
   * Grupa pól.
   */
  protected final Group tileGroup = new Group();
  /**
   * Grupa pionkow.
   */
  protected final Group pieceGroup = new Group();

  /**
   * Metoda tworzaca plansze z pionkami.
   * @param player1 typ pionkow pierwszego gracza
   * @param player2 typ pionkow drugiego gracza
   * @return plansza startowa
   */
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
        if (y <= (HEIGHT-2)/2 -1 && (x + y) % 2 != 0) {
          player1.moveDir = 1;
          piece = makePiece(player1, x, y);
          firstPlayerPieces++;
        }
        if (y >= (HEIGHT+2)/2  && (x + y) % 2 != 0) {
          player2.moveDir = -1;
          piece = makePiece(player2, x, y);
          secondPlayerPieces++;
        }
        if (piece != null) {
          tile.setPiece(piece);
          pieceGroup.getChildren().add(piece);
        }
      }
    }
    return root;
  }

  /**
   * Metoda tworzaca pionka na danym polu.
   * @param type typ pionka
   * @param x wspolrzedna x pola
   * @param y wspolrzedna y pola
   * @return nowy pionek
   */
  protected Piece makePiece(PieceType type, int x, int y) {
    PieceType p = (Player == 1) ? PieceType.WHITE : PieceType.RED;

    Piece piece = new Piece(type, x, y, type == p);

    piece.setOnMouseClicked(e -> {
      int newX = toBoard(piece.getLayoutX());
      int newY = toBoard(piece.getLayoutY());
      int x0 = toBoard(piece.getOldX());
      int y0 = toBoard(piece.getOldY());
      makeMove(x0, y0, newX, newY, true);
    });

    return piece;
  }

  /**
   * Metoda przesuwajaca pionki.
   * @param x0 stara wspolrzedna x
   * @param y0 stara wspolrzedna y
   * @param newX nowa wspolrzedna x
   * @param newY nowa wspolrzedna x
   * @param send wyslanie ruchu do serwera
   */
  public void makeMove(int x0, int y0, int newX, int newY, boolean send) {
    Piece piece = board[x0][y0].getPiece();
    MoveResult result = tryMove(piece, newX, newY);
    switch (result.getType()) {
      case NONE:
        piece.abortMove();
        break;
      case NORMAL:
        piece.move(newX, newY);
        board[x0][y0].setPiece(null);
        board[newX][newY].setPiece(piece);
        if ((newY == 0 || newY == HEIGHT - 1)) {
          piece.change();
        }
        turn = (turn == PieceType.WHITE) ? PieceType.RED : PieceType.WHITE;
        if (send) {
          send("n" + x0 + y0 + newX + newY);
          send("t");
        }
        checkIfEnd();
        break;
      case KILL:
        piece.move(newX, newY);
        board[x0][y0].setPiece(null);
        board[newX][newY].setPiece(piece);
        Piece otherPiece = result.getPiece();
        board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
        pieceGroup.getChildren().remove(otherPiece);
        if (otherPiece.getType() == PieceType.WHITE) {
          firstPlayerPieces--;
          System.out.println("Zbito bialego");
        }
        if (otherPiece.getType() == PieceType.RED) {
          secondPlayerPieces--;
          System.out.println("Zbito czerwonego");
        }
        if (piece.isQueen()) {
          LastQDirection = getDirection(x0, y0, newX, newY);
        }
        if (send) {
          send("k" + x0 + y0 + newX + newY);
        }

        if (!canPieceKill(newX, newY, piece, board, LastQDirection)) {
          if (Player == 1) {
            if ((newY == 0 && piece.getType() == PieceType.WHITE)
                || (newY == HEIGHT - 1 && piece.getType() == PieceType.RED)) {
              piece.change();
            }
          } else if ((newY == 0 && piece.getType() == PieceType.RED) ||
              (newY == HEIGHT - 1 && piece.getType() == PieceType.WHITE)) {
            piece.change();
          }

          LastQDirection = 0;
          turn = (turn == PieceType.WHITE) ? PieceType.RED : PieceType.WHITE;
          if (send) {
            send("t");
          }
        }
        checkIfEnd();
        break;
    }
  }

  /**
   * Metoda wysylajaca informacje do serwera.
   * @param message informacja
   */
  public void send(String message) {
    out.println(message);
  }

  /**
   * Metoda skalujaca piksel do planszy.
   * @param pixel piskel
   * @return przesklowany piksel
   */
  protected int toBoard(double pixel) {
    return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
  }

  /**
   * Metoda zwracajaca aktualna plansze.
   * @return plansza
   */
  public Parent getContent() {
    return this.content;
  }

  /**
   * Metoda zwracajaca typ ruchu.
   * @param piece ruszany pionek
   * @param newX nowa wspolrzedna x ruszanego pionka
   * @param newY nowa wspolrzedna y ruszanego pionka
   * @return typ ruchu (NONE, NORMAL, KILL)
   */
  protected MoveResult tryMove(Piece piece, int newX, int newY) {
    if (newX < 0 || newX >= HEIGHT || newY < 0 || newY >= HEIGHT) {
      return new MoveResult(MoveType.NONE);
    }
    if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0 || turn != piece.getType()) {
      return new MoveResult(MoveType.NONE);
    }
    int x0 = toBoard(piece.getOldX());
    int y0 = toBoard(piece.getOldY());
    if (!piece.isQueen()) {


      if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir &&
          !isForcedKill(piece.getType())) {
        return new MoveResult(MoveType.NORMAL);

      } else if ((Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDir * 2) ||
          (Math.abs(newX - x0) == 2 && y0 - newY == piece.getType().moveDir * 2)) {
        int x1 = x0 + (newX - x0) / 2;
        int y1 = y0 + (newY - y0) / 2;

        if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
          if (isThisLongestKill(x0, y0, newX, newY)) {
            return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
          }
        }
      }
    } else {

      if (canQueenMove(x0, y0, newX, newY) && !isForcedKill(piece.getType())) {
        return new MoveResult(MoveType.NORMAL);
      }
      if (canQueenKill(x0, y0, newX, newY, board)) {
        int[] x = findPiece(x0, y0, newX, newY, board);
        if (isThisLongestKill(x0, y0, newX, newY)) {
          return new MoveResult(MoveType.KILL, board[x[0]][x[1]].getPiece());
        }
      }
    }
    return new MoveResult(MoveType.NONE);
  }

  /**
   * Metoda zwracajaca czy dama moze sie ruszyc.
   * @param x stara wspolrzedna x damy
   * @param y stara wspolrzedna y damy
   * @param newX nowa wspolrzedna x damy
   * @param newY nowa wspolrzedna y damy
   * @return true or false
   */
  protected Boolean canQueenMove(int x, int y, int newX, int newY) {
    return !isInPlus(x, y, newX, newY) && isInCross(x, y, newX, newY) &&
        countPiecesInDiagonal(x, y, newX, newY, board) == 0 && !board[newX][newY].hasPiece();
  }

  /**
   * Metoda zwracajaca czy dama moze bic.
   * @param x stara wspolrzedna x damy
   * @param y stara wspolrzedna y damy
   * @param newX nowa wspolrzedna x damy
   * @param newY nowa wspolrzedna y damy
   * @param board plansza
   * @return true or false
   */
  protected Boolean canQueenKill(int x, int y, int newX, int newY, Tile[][] board) {
    Piece piece = board[x][y].getPiece();
    if (!isInPlus(x, y, newX, newY) && isInCross(x, y, newX, newY) &&
        countPiecesInDiagonal(x, y, newX, newY, board) == 1 && !board[newX][newY].hasPiece()) {
      int[] p = findPiece(x, y, newX, newY, board);
      if (piece.getType() != board[p[0]][p[1]].getPiece().getType()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Metoda sprawdza czy ktoras ze wspolrzednych jest taka sama.
   * @param x0 pierwsza wspolrzedna x
   * @param y0 pierwsza wspolrzedna y
   * @param x1 druga wspolrzedna x
   * @param y1 druga wspolrzedna y
   * @return true or false
   */
  protected Boolean isInPlus(int x0, int y0, int x1, int y1) {
    return x0 == x1 || y0 == y1;
  }
  /**
   * Metoda sprawdza czy suma poszczegolnych wspolrzednych jest taka sama.
   * @param x0 pierwsza wspolrzedna x
   * @param y0 pierwsza wspolrzedna y
   * @param x1 druga wspolrzedna x
   * @param y1 druga wspolrzedna y
   * @return true or false
   */
  protected Boolean isInCross(int x0, int y0, int x1, int y1) {
    if (Math.abs(x0 - y0) == Math.abs(x1 - y1)) {
      return true;
    }
    return x0 + y0 == x1 + y1;
  }

  /**
   * Metoda szukajca wspolrzednych pionka.
   * @param x0 pierwsza wspolrzedna x
   * @param y0 pierwsza wspolrzedna y
   * @param x1 druga wspolrzedna x
   * @param y1 druga wspolrzedna y
   * @param board plansza
   * @return wspolrzedne
   */
  protected int[] findPiece(int x0, int y0, int x1, int y1, Tile[][] board) {
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

  /**
   * Metoda liczy pionki w skosie.
   * @param x0 pierwsza wspolrzedna x
   * @param y0 pierwsza wspolrzedna y
   * @param x1 druga wspolrzedna x
   * @param y1 druga wspolrzedna y
   * @param board plansza
   * @return ilosc pionkow w skosie
   */
  protected int countPiecesInDiagonal(int x0, int y0, int x1, int y1, Tile[][] board) {
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

  /**
   * Metoda sprawdzajaca czy bicie jest wymuszone.
   * @param player gracz
   * @return true or false
   */
  protected boolean isForcedKill(PieceType player) {
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        if (board[x][y].hasPiece() && board[x][y].getPiece().getType() == player) {
          if (canPieceKill(x, y, board[x][y].getPiece(), board, LastQDirection)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Metoda sprawdza czy pionek moze bic.
   * @param x wspolrzedna x pionka
   * @param y wspolrzedna y pionka
   * @param piece pionek
   * @param board plansza
   * @param lastQDirection ostatni kierunek chodzenia damy
   * @return true or false
   */
  protected boolean canPieceKill(int x, int y, Piece piece, Tile[][] board, int lastQDirection) {
    PieceType type = piece.getType();
    int[][] directions = {{-1, 1}, {-1, -1}, {1, 1}, {1, -1}};
    if (!piece.isQueen()) {
      for (int[] direction : directions) {
        int dX = direction[0];
        int dY = direction[1];
        if (x + 2 * dX >= 0 && y + 2 * dY < HEIGHT && x + 2 * dX < WIDTH && y + 2 * dY >= 0) {
          if (board[x + dX][y + dY].hasPiece()) {
            if (board[x + dX][y + dY].getPiece().getType() != type) {
              if (!board[x + 2 * dX][y + 2 * dY].hasPiece()) {
                return true;
              }
            }
          }
        }
      }
    } else {
      int xClone;
      int yClone;
      int wrongDirection = 4;
      for (int[] direction : directions) {
        int dX = direction[0];
        int dY = direction[1];
        xClone = x;
        yClone = y;
        while (xClone >= 0 && yClone < HEIGHT && xClone < WIDTH && yClone >= 0 &&
            lastQDirection != wrongDirection) {
          if (canQueenKill(x, y, xClone, yClone, board)) {
            return true;
          }
          xClone += dX;
          yClone += dY;
        }
        wrongDirection--;
      }
    }
    return false;
  }

  /**
   * Pobiera ilosc najdluzszego bicia pionka.
   * @param x wspolrzedna x pionka
   * @param y wspolrzedna y pionka
   * @param board plansza
   * @param lastQDirection ostatni kierunek chodzenia damy
   * @return ilosc najdluzszego bicia pionka
   */
  protected int getLongestPawnKill(int x, int y, Tile[][] board, int lastQDirection) {
    int road1 = 0;
    int road2 = 0;
    int road3 = 0;
    int road4 = 0;
    Tile[][] boardClone = makeBoardClone(board);
    int[][] directions = {{-1, 1}, {-1, -1}, {1, 1}, {1, -1}};
    if (board[x][y].hasPiece()) {
      Piece piece = board[x][y].getPiece();
      if (!piece.isQueen()) {
        int i = 0;
        for (int[] direction : directions) {
          int dX = direction[0];
          int dY = direction[1];
          if (x + 2 * dX >= 0 && y + 2 * dY < HEIGHT && x + 2 * dX < WIDTH && y + 2 * dY >= 0) {
            if (!board[x + 2 * dX][y + 2 * dY].hasPiece() && board[x + dX][y + dY].hasPiece()) {
              if (board[x + dX][y + dY].getPiece().getType() != piece.getType()) {
                boardClone[x + 2 * dX][y + 2 * dY].setPiece(piece);
                boardClone[x + dX][y + dY].setPiece(null);
                boardClone[x][y].setPiece(null);
                if (i == 0) {
                  road1++;
                  road1 += getLongestPawnKill(x + 2 * dX, y + 2 * dY, boardClone,
                      lastQDirection);
                }
                if (i == 1) {
                  road2++;
                  road2 += getLongestPawnKill(x + 2 * dX, y + 2 * dY, boardClone,
                      lastQDirection);
                }
                if (i == 2) {
                  road3++;
                  road3 += getLongestPawnKill(x + 2 * dX, y + 2 * dY, boardClone,
                      lastQDirection);
                }
                if (i == 3) {
                  road4++;
                  road4 += getLongestPawnKill(x + 2 * dX, y + 2 * dY, boardClone,
                      lastQDirection);
                }
              }
            }
          }
          i++;
          boardClone = makeBoardClone(board);
        }
      }
      if (piece.isQueen()) {
        int xClone = x;
        int yClone = y;
        int[] p;
        int i = 0;
        for (int[] direction : directions) {
          int dX = direction[0];
          int dY = direction[1];
          while (xClone >= 0 && yClone < HEIGHT && xClone < WIDTH && yClone >= 0 &&
              lastQDirection != 4 - i) {
            int roadClone = 0;
            boardClone = makeBoardClone(board);
            p = findPiece(x, y, xClone, yClone, board);
            if (canQueenKill(x, y, xClone, yClone, board)) {
              lastQDirection = getDirection(x, y, xClone, yClone);
              boardClone[x][y].setPiece(null);
              boardClone[p[0]][p[1]].setPiece(null);
              boardClone[xClone][yClone].setPiece(piece);
              roadClone++;
              roadClone += getLongestPawnKill(xClone, yClone, boardClone, lastQDirection);
            }
            if (roadClone > road1 && i == 0) {
              road1 = roadClone;
            }
            if (roadClone > road2 && i == 1) {
              road2 = roadClone;
            }
            if (roadClone > road3 && i == 2) {
              road3 = roadClone;
            }
            if (roadClone > road4 && i == 3) {
              road4 = roadClone;
            }
            xClone += dX;
            yClone += dY;
          }
          i++;
          xClone = x;
          yClone = y;
        }
      }
    }
    return Math.max(Math.max(road1, road2), Math.max(road3, road4));
  }

  /**
   * Metoda zwracajaca ilosc bic najdluzeszego ruchu.
   * @return ilosc bic najdluzeszego ruchu
   */
  protected int getLongestPossibleKill() {
    int count = 0;
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        if (board[x][y].hasPiece() && board[x][y].getPiece().getType() == turn) {
          if (count < getLongestPawnKill(x, y, board, LastQDirection)) {
            count = getLongestPawnKill(x, y, board, LastQDirection);
          }
        }
      }
    }
    return count;
  }

  /**
   * Meotda sprawdzajaca czy to najdluzsze mozliwe biecie
   * @param x0 pierwsza wspolrzedna x
   * @param y0 pierwsza wspolrzedna y
   * @param x1 druga wspolrzedna x
   * @param y1 druga wspolrzedna y
   * @return true of false
   */
  protected boolean isThisLongestKill(int x, int y, int x1, int y1) {
    Tile[][] clone = makeBoardClone(board);
    int[] p = findPiece(x, y, x1, y1, board);
    clone[x1][y1].setPiece(clone[x][y].getPiece());
    clone[x][y].setPiece(null);
    clone[p[0]][p[1]].setPiece(null);
    return getLongestPossibleKill() ==
        (getLongestPawnKill(x1, y1, clone, getDirection(x, y, x1, y1)) + 1);
  }

  /**
   * Metoda klonuje plansze.
   * @param board plansza
   * @return kopia planszy
   */
  protected Tile[][] makeBoardClone(Tile[][] board) {
    Tile[][] boardClone = new Tile[HEIGHT][WIDTH];
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        boardClone[x][y] = new Tile((x + y) % 2 != 0, x, y);
        if (board[x][y].hasPiece()) {
          boardClone[x][y].setPiece(
              new Piece(board[x][y].getPiece().getType(), x, y, true));
          if (board[x][y].getPiece().isQueen()) {
            boardClone[x][y].getPiece().change();
          }
        } else {
          boardClone[x][y].setPiece(null);
        }
      }
    }
    return boardClone;
  }

  /**
   * Metoda pobierajaca kierunek poruszania sie damy.
   * @param x pierwsza wspolrzedna x
   * @param y pierwsza wspolrzedna y
   * @param x1 druga wspolrzedna x
   * @param y1 druga wspolrzedna y
   * @return kierunek poruszanie sie damy
   */
  protected int getDirection(int x, int y, int x1, int y1) {
    int xClone = x;
    int yClone = y;
    int[][] directions = {{-1, 1}, {-1, -1}, {1, 1}, {1, -1}};
    int qDirection = 1;
    for (int[] direction : directions) {
      while (x >= 0 && y < HEIGHT && x < WIDTH && y >= 0) {
        if (x == x1 && y == y1) {
          return qDirection;
        }
        x += direction[0];
        y += direction[1];
      }
      qDirection++;
      x = xClone;
      y = yClone;
    }
    return 0;
  }

  /**
   * Metoda sprawdzajaca czy to koniec gry.
   */
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
      send("REDWON");
      redWon = true;
      send("t");
    } else if (secondPlayerPieces == 0) {
      PieceType p = (Player == 1) ? PieceType.WHITE : PieceType.RED;
      send("WHITEWON");
      whiteWon = true;
      send("t");
    }

  }

  /**
   * Metoda sprawdzajaca czy gracz moze sie ruszyc.
   * @param Player gracz
   * @return true or false
   */
  protected boolean canPlayerMove(int Player) {
    PieceType p = (Player == 1) ? PieceType.WHITE : PieceType.RED;
    if (p != turn) {
      return true;
    }
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {

        if (board[x][y].hasPiece() && board[x][y].getPiece().getType() == p) {
          Piece piece = board[x][y].getPiece();
          if (!piece.isQueen()) {
            int i = 0;
            if (tryMove(piece, x + 1, y + 1).type == MoveType.NONE) {
              i++;
            }
            if (tryMove(piece, x + 1, y - 1).type == MoveType.NONE) {
              i++;
            }
            if (tryMove(piece, x - 1, y + 1).type == MoveType.NONE) {
              i++;
            }
            if (tryMove(piece, x - 1, y - 1).type == MoveType.NONE) {
              i++;
            }
            if (tryMove(piece, x - 2, y - 2).type == MoveType.NONE) {
              i++;
            }
            if (tryMove(piece, x - 2, y + 2).type == MoveType.NONE) {
              i++;
            }
            if (tryMove(piece, x + 2, y + 2).type == MoveType.NONE) {
              i++;
            }
            if (tryMove(piece, x + 2, y - 2).type == MoveType.NONE) {
              i++;
            }
            if (i != 8) {
              return true;
            }
          } else {
            return true;
          }
        }
      }
    }
    return false;
  }

}
