package com.example;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import java.io.PrintWriter;

public class PolishBoard extends Board{

    public PolishBoard(int Player,PrintWriter out) {
        super(10,10);
        this.Player=Player;
        this.out=out;
        if(Player==1)
            this.content = createContent(PieceType.RED,PieceType.WHITE);
        else
            this.content=createContent(PieceType.WHITE,PieceType.RED);

    }

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
                if (y <= 3 && (x + y) % 2 != 0) {
                    player1.moveDir=1;
                    piece = makePiece(player1, x, y);
                    firstPlayerPieces++;
                }
                if (y >= 6 && (x + y) % 2 != 0) {
                    player2.moveDir=-1;
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




}