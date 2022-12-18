package com.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    private Piece piece;

    public Tile(boolean isWhite, int x, int y){
        setWidth(Board.TILE_SIZE);
        setHeight(Board.TILE_SIZE);

        relocate(x * Board.TILE_SIZE, y * Board.TILE_SIZE);

        setFill(isWhite ? Color.valueOf("#feb") : Color.valueOf("582"));
    }

    public boolean hasPiece(){
        return piece != null;
    }
    public Piece getPiece(){
        return piece;
    }
    public void setPiece(Piece piece){
        this.piece = piece;
    }
}
