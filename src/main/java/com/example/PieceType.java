package com.example;

public enum PieceType {
    RED(1), WHITE(-1);

    int moveDir;

    PieceType(int moveDir){
        this.moveDir = moveDir;
    }
}
