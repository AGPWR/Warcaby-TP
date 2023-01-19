package com.example;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testujaca poprawnosc metoda klasy Board.
 */
class BoardTest {

    @Test
    void createContent() {
        ConcreteBoardFactory cbf = new ConcreteBoardFactory();
        Board b = cbf.getBoard("RUSSIAN", 1, null);
        assert(b.firstPlayerPieces == 24);
        assert(b.secondPlayerPieces == 24);
    }

    @Test
    void makePiece() {
        ConcreteBoardFactory cbf = new ConcreteBoardFactory();
        Board b = cbf.getBoard("RUSSIAN", 1, null);
        Piece p1 = b.makePiece(PieceType.RED, 1, 1);
        assert(p1.getType() == PieceType.RED);

        Piece p2 = b.makePiece(PieceType.WHITE, 1, 1);
        assert(p2.getType() == PieceType.WHITE);
    }

    @Test
    void toBoard() {
        ConcreteBoardFactory cbf = new ConcreteBoardFactory();
        Board b = cbf.getBoard("RUSSIAN", 1, null);
        assert(b.toBoard(23) == (23 + Board.TILE_SIZE / 2) / Board.TILE_SIZE );
        assert(b.toBoard(1) == (1 + Board.TILE_SIZE / 2) / Board.TILE_SIZE );
        assert(b.toBoard(7) == (7 + Board.TILE_SIZE / 2) / Board.TILE_SIZE );
    }

    @Test
    void getContent() {
        ConcreteBoardFactory cbf = new ConcreteBoardFactory();
        Board b = cbf.getBoard("RUSSIAN", 1, null);
        assert(b.getContent() != null);
    }

    @Test
    void makeMove() {
        ConcreteBoardFactory cbf = new ConcreteBoardFactory();
        Board b = cbf.getBoard("RUSSIAN", 1, null);
        //Piece p1 = b.makeMove();
    }

    @Test
    void tryMove() {
         class Test extends Board{
             protected Test() {
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

                         if (piece != null) {
                             tile.setPiece(piece);
                             pieceGroup.getChildren().add(piece);
                         }
                     }
                 }
                 return root;
             }

             @Override
             public MoveResult tryMove(Piece piece, int newX, int newY) {
                 return super.tryMove(piece, newX, newY);
             }

         }
        Test test=new Test();
         Tile[][] board= test.board;
        assert(test.tryMove(board[7][4].getPiece(),6,3).type==MoveType.NONE);
        assert(test.tryMove(board[7][4].getPiece(),5,2).type==MoveType.KILL);
    }

    @Test
    void canQueenMove() {
    }

    @Test
    void canQueenKill() {
    }

    @Test
    void isInPlus() {
    }

    @Test
    void isInCross() {
    }

    @Test
    void findPiece() {
    }

    @Test
    void countPiecesInDiagonal() {
    }

    @Test
    void isForcedKill() {
    }

    @Test
    void canPieceKill() {
    }

    @Test
    void getLongestPawnKill() {
    }

    @Test
    void getLongestPossibleKill() {
    }

    @Test
    void isThisLongestKill() {
    }

    @Test
    void makeBoardClone() {
    }

    @Test
    void getDirection() {
    }

    @Test
    void checkIfEnd() {
    }
}