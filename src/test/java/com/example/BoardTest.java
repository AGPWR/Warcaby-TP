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
        assert(b.firstPlayerPieces == 12);
        assert(b.secondPlayerPieces == 12);
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
        Test1 test=new com.example.Test1();
         Tile[][] board= test.board;
        assert(test.tryMove(board[7][4].getPiece(),6,3).type==MoveType.NONE);
        assert(test.tryMove(board[7][4].getPiece(),5,2).type==MoveType.KILL);
    }

    @Test
    void canQueenMove() {
        Test1 test =new Test1();
        assert (test.canQueenMove(1,4,3,2));
        assert (!(test.canQueenMove(1,4,1,6)));

    }

    @Test
    void canQueenKill() {
        Test1 test =new Test1();
        assert (test.canQueenKill(2,5,5,2,test.board));
        assert (test.canQueenKill(2,5,6,1,test.board));
        assert (test.canQueenKill(2,5,7,0, test.board));
        assert (!test.canQueenKill(2,5,0,3, test.board));
    }

    @Test
    void findPiece() {
        Test1 test =new Test1();
        Piece piece1=test.board[4][3].getPiece();
        int[] t =test.findPiece(2,5,5,2, test.board);
        assert (test.board[t[0]][t[1]].getPiece()==piece1);
        t=test.findPiece(2,5,6,1, test.board);
        assert (test.board[t[0]][t[1]].getPiece()==piece1);
    }

    @Test
    void isForcedKill() {
        Test1 test =new Test1();
        assert (test.isForcedKill(PieceType.WHITE));
    }

    @Test
    void canPieceKill() {
        Test1 test =new Test1();
        Piece piece=test.board[2][5].getPiece();
        assert (test.canPieceKill(2,5,piece, test.board,test.LastQDirection ));
        piece=test.board[7][4].getPiece();
        assert (test.canPieceKill(7,4,piece, test.board,test.LastQDirection ));
        piece=test.board[1][4].getPiece();
        assert (!test.canPieceKill(1,4,piece, test.board,test.LastQDirection ));

    }

    @Test
    void getLongestPawnKill() {
        Test1 test =new Test1();
        assert (test.getLongestPawnKill(2,5, test.board,test.LastQDirection )==1);
        assert (test.getLongestPawnKill(7,4,test.board,test.LastQDirection )==2);
        assert (test.getLongestPawnKill(1,4,test.board,test.LastQDirection )==0);

    }

    @Test
    void getLongestPossibleKill() {
        Test1 test =new Test1();
        assert (test.getLongestPossibleKill()==2);
    }

    @Test
    void isThisLongestKill() {
        Test1 test =new Test1();
        assert (test.isThisLongestKill(7,4,5,2));
        assert (!test.isThisLongestKill(2,5,5,2));

    }

    @Test
    void getDirection() {
        Test1 test =new Test1();
        assert (test.getDirection(2,5,3,4)==4);
        assert (test.getDirection(2,5,1,4)==2);
        assert (test.getDirection(2,5,1,6)==1);
        assert (test.getDirection(2,5,3,6)==3);


    }

    @Test
    void checkIfEnd() {
        Test2 test =new Test2(true);
        test.checkIfEnd();
        assert (test.redWon);
        assert (!test.whiteWon);
        test =new Test2(false);
        test.checkIfEnd();
        assert (test.redWon);
        assert (!test.whiteWon);

        //Bez wywolania metody checkIfEnd()
        test =new Test2(false);
        assert (!test.redWon);

        //Sprawdzenie braku ruchu dla damy
        Test3 test3 =new Test3();
        test3.checkIfEnd();
        assert (test3.redWon);

    }
}