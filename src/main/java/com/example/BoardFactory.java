package com.example;

import java.io.PrintWriter;

public interface BoardFactory {
    Board getBoard(String boardType, int Player, PrintWriter out);
}
