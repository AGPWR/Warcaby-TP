package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testujaca poprawnosc metoda klasy MoveResult.
 */
class MoveResultTest {

    @Test
    void getPiece() {
        Piece p = new Piece(PieceType.RED, 1, 1, true);
        MoveResult mr = new MoveResult(MoveType.KILL, p);
        assert(mr.getPiece() != null);
    }

    @Test
    void getType() {
        Piece p = new Piece(PieceType.RED, 1, 1, true);

        MoveResult mr = new MoveResult(MoveType.KILL, p);
        assert(mr.getType() == MoveType.KILL);

        MoveResult mr1 = new MoveResult(MoveType.NORMAL, p);
        assert(mr1.getType() == MoveType.NORMAL);

        MoveResult mr2 = new MoveResult(MoveType.NONE, p);
        assert(mr2.getType() == MoveType.NONE);
    }
}