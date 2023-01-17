package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testujaca poprawnosc metoda klasy Tile.
 */
class TileTest {

    @Test
    void hasPiece() {
        Tile t1 = new Tile(false, 1, 2);
        Piece p = new Piece(PieceType.RED, 1, 1 ,false);
        t1.setPiece(p);
        assertTrue(t1.hasPiece());

        Tile t2 =new Tile(false, 1 , 2);
        assertFalse(t2.hasPiece());
    }

    @Test
    void getPiece() {
        Tile t1 = new Tile(false, 1, 2);
        Piece p = new Piece(PieceType.RED, 1, 1 ,false);
        t1.setPiece(p);
        assert(t1.getPiece() != null);

        Tile t2 = new Tile(false, 1, 2);
        assert(t2.getPiece() == null);
    }

    @Test
    void setPiece() {
        Tile t1 = new Tile(false, 1, 2);
        Piece p = new Piece(PieceType.RED, 1, 1 ,false);
        t1.setPiece(p);
        assert(t1.getPiece() != null);
    }
}