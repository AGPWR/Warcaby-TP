package com.example;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testujaca poprawnosc metoda klasy ConcreteBoardFactory.
 */
class ConcreteBoardFactoryTest {

    @Test
    void getBoard() {
        ConcreteBoardFactory cbf = new ConcreteBoardFactory();
        assert (cbf.getBoard("CLASSIC", 1, null) instanceof ClassicBoard);
        assert (cbf.getBoard("POLISH", 1, null) instanceof PolishBoard);
        assert (cbf.getBoard("RUSSIAN", 1, null) instanceof RussianBoard);

    }
}