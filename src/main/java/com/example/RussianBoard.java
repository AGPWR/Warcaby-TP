package com.example;

import java.io.PrintWriter;

public class RussianBoard extends Board{

    public RussianBoard(int Player,PrintWriter out) {
        super(8,8);
        this.Player=Player;
        this.out=out;
        if(Player==1)
            this.content = createContent(PieceType.RED,PieceType.WHITE);
        else
            this.content=createContent(PieceType.WHITE,PieceType.RED);
    }

    public MoveResult tryMove(Piece piece, int newX, int newY) {
        if (newX < 0 || newX >= HEIGHT || newY < 0 || newY >= HEIGHT)
            return new MoveResult(MoveType.NONE);
        if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0 || turn != piece.getType()) {
            return new MoveResult(MoveType.NONE);
        }
        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());
        if (!piece.isQueen()) {


            if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir && !isForcedKill(piece.getType())) {
                return new MoveResult(MoveType.NORMAL);

            } else if ((Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDir * 2) || (Math.abs(newX - x0) == 2 && y0 - newY == piece.getType().moveDir * 2)) {
                int x1 = x0 + (newX - x0) / 2;
                int y1 = y0 + (newY - y0) / 2;

                if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
                    return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
                }
            }
        } else {

            if (canQueenMove(x0, y0, newX, newY) && !isForcedKill(piece.getType())) {
                return new MoveResult(MoveType.NORMAL);
            }
            if (canQueenKill(x0, y0, newX, newY, board)) {
                int[] x = findPiece(x0, y0, newX, newY, board);
                return new MoveResult(MoveType.KILL, board[x[0]][x[1]].getPiece());
            }
        }
        return new MoveResult(MoveType.NONE);
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
                    send("n"+x0+y0+newX+newY);
                    send("t");}
                checkIfEnd();
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
                    send("k"+x0+y0+newX+newY);

                if(Player == 1) {
                    if ((newY == 0 && piece.getType() == PieceType.WHITE) || (newY == HEIGHT-1 && piece.getType() == PieceType.RED)){
                        piece.change();
                    }
                }
                else
                if ((newY == 0 && piece.getType() == PieceType.RED) || (newY == HEIGHT-1 && piece.getType() == PieceType.WHITE))
                    piece.change();
                if (!canPieceKill(newX, newY, piece, board, LastQDirection)) {


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