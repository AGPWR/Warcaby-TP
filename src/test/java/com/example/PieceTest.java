package com.example;

import org.junit.jupiter.api.Test;

import static com.example.Board.TILE_SIZE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testujaca poprawnosc metoda klasy Piece.
 */
class PieceTest {

    @Test
    void move() {
        Piece p = new Piece(PieceType.RED, 2 , 3, true);
        assert(p.getOldX() == TILE_SIZE * 2);
        assert(p.getOldY() == TILE_SIZE * 3);
    }

    @Test
    void getType() {
        Piece p = new Piece(PieceType.RED, 2 , 3, true);
        assert(p.getType() == PieceType.RED);
        assert(p.getType() != PieceType.WHITE);
    }

    @Test
    void getOldX() {
        Piece p = new Piece(PieceType.RED, 2 , 3, true);
        assert (p.getOldX() == TILE_SIZE * 2);
    }

    @Test
    void getOldY() {
        Piece p = new Piece(PieceType.RED, 2 , 3, true);
        assert (p.getOldY() == TILE_SIZE * 3);
    }

    @Test
    void change() {
        Piece p = new Piece(PieceType.RED, 2 , 3, true);
        p.change();
        assertTrue(p.isQueen());
    }

    @Test
    void isQueen() {
        Piece p = new Piece(PieceType.RED, 2 , 3, true);
        assertFalse(p.isQueen());
        p.change();
        assertTrue(p.isQueen());
    }
}