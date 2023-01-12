package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Player extends Application implements Runnable {

    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    private Board board;
    private PolishBoard polishBoard;
    private RussianBoard russianBoard;

    private PieceType player = PieceType.WHITE;
    public int gameType;

    public final static PieceType PLAYER1 = PieceType.WHITE;
    public final static PieceType PLAYER2 = PieceType.RED;


    private static PieceType actualPlayer = PLAYER1;

    @Override
    public void run() {

        if (player == PLAYER1) {
            f1();
        } else {
            f2();
        }

    }
    @Override
    public void start(Stage stage) throws Exception {

        Button b1 = new Button("Klasyczne");
        Button b2 = new Button("Polskie");
        Button b3 = new Button("Rosyjskie");
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.add(b1,2,2);
        gp.add(b2,3,2);
        gp.add(b3,4,2);
        gp.setMinSize(500,500);
        Scene menu = new Scene(gp);
        stage.setTitle("WARCABY");
        stage.setScene(menu);
        stage.show();

        GridPane gpWait = new GridPane();

        Text waiting = new Text("Szukanie przeciwnika...");
        gpWait.add(waiting, 1,1);
        Scene lookingForEnemy = new Scene(gpWait);


        b1.setOnAction( event -> {
            gameType = 1;
            stage.setScene(lookingForEnemy);
            stage.show();

            listenSocket();
            String str = null;
            try {
                str = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(str);
            if (str.equals("2")) {
                board = new Board(2,out);
            }
            if (str.equals("1")) {
                board = new Board(1,out);
            }
            startThread();
            Scene scene = new Scene(board.getContent());
            stage.setTitle("");
            stage.setResizable(false);
            stage.setScene(scene);
        });

        b2.setOnAction( event -> {
            gameType = 2;
            stage.setScene(lookingForEnemy);
            listenSocket();
            String str = null;
            try {
                str = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(str);
            if (str.equals("2")) {
                polishBoard = new PolishBoard(2,out);
            }
            if (str.equals("1")) {
                polishBoard = new PolishBoard(1,out);
            }
            startThread();
            Scene scene = new Scene(polishBoard.getContent());
            stage.setTitle("");
            stage.setResizable(false);
            stage.setScene(scene);
        });

        b3.setOnAction( event -> {
            gameType = 3;
            stage.setScene(lookingForEnemy);
            listenSocket();
            String str = null;
            try {
                str = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(str);
            if (str.equals("2")) {
                russianBoard = new RussianBoard(2,out);
            }
            if (str.equals("1")) {
                russianBoard = new RussianBoard(1,out);
            }
            startThread();
            Scene scene = new Scene(russianBoard.getContent());
            stage.setTitle("");
            stage.setResizable(false);
            stage.setScene(scene);
        });


    }

    public static void main(String[] args) {
        Application.launch(args);
    }


    /*
    Połaczenie z socketem
     */
    public void listenSocket() {
        try {
            socket = new Socket("localhost", 4444);
            // Inicjalizacja wysylania do serwera
            out = new PrintWriter(socket.getOutputStream(), true);
            // Inicjalizacja odbierania z serwera
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }

  /*
      Poczatkowe ustawienia klienta. Ustalenie ktory socket jest ktorym kliente
  */

    private void startThread() {
        Thread gTh = new Thread(this);
        gTh.start();
    }

    void f1() {
        while (true) {
            synchronized (this) {
                if (actualPlayer == PLAYER1) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                    }
                }

                receive();
                if(gameType == 1)
                    actualPlayer=board.turn;
                if(gameType == 2)
                    actualPlayer=polishBoard.turn;
                if(gameType == 3)
                    actualPlayer=russianBoard.turn;

                notifyAll();
            }
        }
    }
    void f2() {
        while (true) {
            synchronized (this) {
                if (actualPlayer == PLAYER2) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                    }
                }
                receive();
                if(gameType == 1)
                    actualPlayer=board.turn;
                if(gameType == 2)
                    actualPlayer=polishBoard.turn;
                if(gameType == 3)
                    actualPlayer=russianBoard.turn;
                notifyAll();
            }
        }
    }
    public void receive() {
        try {
            String str = in.readLine();
            System.out.println(str);

            if (str.charAt(0) == 'n' || str.charAt(0) == 'k') {
                int x0 = translate(Character.getNumericValue(str.charAt(1)), 8);
                int y0 = translate(Character.getNumericValue(str.charAt(2)), 8);
                int x1 = translate(Character.getNumericValue(str.charAt(3)), 8);
                int y1 = translate(Character.getNumericValue(str.charAt(4)), 8);

                Platform.runLater(()->board.makeMove(x0, y0, x1, y1, false));
            }

            if (str.charAt(0) == 'N' || str.charAt(0) == 'K') {
                int x0 = translate(Character.getNumericValue(str.charAt(1)), 10);
                int y0 = translate(Character.getNumericValue(str.charAt(2)), 10);
                int x1 = translate(Character.getNumericValue(str.charAt(3)), 10);
                int y1 = translate(Character.getNumericValue(str.charAt(4)), 10);

                Platform.runLater(()->polishBoard.makeMove(x0, y0, x1, y1, false));
            }

            if (str.charAt(0) == 'j' || str.charAt(0) == 'i') {
                int x0 = translate(Character.getNumericValue(str.charAt(1)), 8);
                int y0 = translate(Character.getNumericValue(str.charAt(2)), 8);
                int x1 = translate(Character.getNumericValue(str.charAt(3)), 8);
                int y1 = translate(Character.getNumericValue(str.charAt(4)), 8);

                Platform.runLater(()->russianBoard.makeMove(x0, y0, x1, y1, false));
            }

            if (str.equals("REDWON")){
                System.out.println("czerwony wygral");
            }
            if (str.equals("WHITEWON")){
                System.out.println("bialy wygral");
            }
        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
    }
    private int translate(int a, int height) {
        return height - a - 1;
    }
}
