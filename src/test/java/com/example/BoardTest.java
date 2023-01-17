package com.example;

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