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

    public void makeMove(int x0,int y0,int newX,int newY,boolean send){
        Piece piece=board[x0][y0].getPiece();
        MoveResult result = tryMove(piece, newX, newY);
        switch (result.getType()) {
            case NONE:
                piece.abortMove();
                break;
            case NORMAL:
                piece.move(newX, newY);
                board[x0][y0].setPiece(null);
                board[newX][newY].setPiece(piece);
                if ((newY == 0 || newY == HEIGHT-1)) {
                    piece.change();
                }
                turn = (turn == PieceType.WHITE) ? PieceType.RED : PieceType.WHITE;
                if(send){
                    send("N"+x0+y0+newX+newY);
                    send("t");}
                break;
            case KILL:
                piece.move(newX, newY);
                board[x0][y0].setPiece(null);
                board[newX][newY].setPiece(piece);
                Piece otherPiece = result.getPiece();
                board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                pieceGroup.getChildren().remove(otherPiece);
                if(otherPiece.getType() == PieceType.WHITE){
                    firstPlayerPieces--;
                    System.out.println("Zbito bialego");
                }
                if(otherPiece.getType() == PieceType.RED){
                    secondPlayerPieces--;
                    System.out.println("Zbito czerwonego");
                }
                if (piece.isQueen()) {
                    LastQDirection = getDirection(x0, y0, newX, newY);
                }
                if(send)
                    send("K"+x0+y0+newX+newY);

                if (!canPieceKill(newX, newY, piece, board, LastQDirection)) {
                    if(Player==1) {
                        if ((newY == 0 && piece.getType() == PieceType.WHITE) || (newY == HEIGHT-1 && piece.getType() == PieceType.RED))
                            piece.change();
                    }
                    else
                    if ((newY == 0 && piece.getType() == PieceType.RED) || (newY == HEIGHT-1 && piece.getType() == PieceType.WHITE))
                        piece.change();

                    LastQDirection = 0;
                    turn = (turn == PieceType.WHITE) ? PieceType.RED : PieceType.WHITE;
                    if(send)
                        send("t");
                }
                checkIfEnd();
                break;
        }
    }
}