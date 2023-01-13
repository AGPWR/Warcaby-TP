package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

    private ClassicBoard classicBoard;
    private PolishBoard polishBoard;
    private RussianBoard russianBoard;

    private final PieceType player = PieceType.WHITE;
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
    public void start(Stage stage) {

        Button b1 = new Button("Klasyczne");
        Button b2 = new Button("Polskie");
        Button b3 = new Button("Rosyjskie");
        b1.setPadding(new Insets(20));
        b2.setPadding(new Insets(20));
        b3.setPadding(new Insets(20));
        b1.setPrefWidth(75);
        b2.setPrefWidth(75);
        b3.setPrefWidth(75);
        b1.setMinWidth(100);
        b2.setMinWidth(100);
        b3.setMinWidth(100);

        BorderPane bp = new BorderPane();
        Text t = new Text("Wybierz tryb gry");
        t.setStyle("-fx-font: 24 arial;");

        bp.setTop(t);
        HBox hb = new HBox();
        hb.getChildren().addAll(b1, b2, b3);
        hb.setSpacing(50);
        HBox.setMargin(b1, new Insets(125, 0, 0, 50));
        HBox.setMargin(b2, new Insets(125, 0, 0, 0));
        HBox.setMargin(b3, new Insets(125, 0, 0, 0));
        bp.setCenter(hb);
        bp.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, new CornerRadii(0), Insets.EMPTY)));
        BorderPane.setAlignment(hb, Pos.CENTER);
        BorderPane.setAlignment(t, Pos.CENTER);
        BorderPane.setMargin(t, new Insets(50, 0, 0, 0));

        Scene menu = new Scene(bp, 500, 500);
        stage.setTitle("WARCABY");
        stage.setScene(menu);
        stage.show();

        BorderPane bp1 = new BorderPane();
        Text waiting = new Text("Szukanie przeciwnika...");
        waiting.setStyle("-fx-font: 24 arial;");
        bp1.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, new CornerRadii(0), Insets.EMPTY)));
        StackPane p = new StackPane();
        p.setPrefSize(499, 499);
        p.getChildren().add(waiting);
        StackPane.setAlignment(p, Pos.CENTER);
        bp1.setCenter(p);

        Scene lookingForEnemy = new Scene(bp1, Color.GREENYELLOW);

        Button button = new Button("Powrót");
        button.setFocusTraversable(false);
        button.setOnAction(event -> stage.setScene(menu));


        b1.setOnAction(event -> {
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
                classicBoard = new ClassicBoard(2, out);
            }
            if (str.equals("1")) {
                classicBoard = new ClassicBoard(1, out);
            }
            startThread();
            BorderPane borderpane = new BorderPane();
            BorderPane.setAlignment(button, Pos.CENTER_RIGHT);
            BorderPane.setMargin(button, new Insets(1, 20, 1, 0));
            borderpane.setBottom(button);
            borderpane.setCenter(classicBoard.getContent());
            Scene scene1 = new Scene(borderpane);
            stage.setTitle("");
            stage.setResizable(false);
            stage.setScene(scene1);


        });

        b2.setOnAction(event -> {
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
                polishBoard = new PolishBoard(2, out);
            }
            if (str.equals("1")) {
                polishBoard = new PolishBoard(1, out);
            }
            startThread();
            BorderPane borderpane = new BorderPane();
            BorderPane.setAlignment(button, Pos.CENTER_RIGHT);
            BorderPane.setMargin(button, new Insets(1, 20, 1, 0));
            borderpane.setBottom(button);
            borderpane.setCenter(polishBoard.getContent());
            Scene scene2 = new Scene(borderpane);
            stage.setTitle("");
            stage.setResizable(false);
            stage.setScene(scene2);
        });

        b3.setOnAction(event -> {
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
                russianBoard = new RussianBoard(2, out);
            }
            if (str.equals("1")) {
                russianBoard = new RussianBoard(1, out);
            }
            startThread();
            BorderPane borderpane = new BorderPane();
            BorderPane.setAlignment(button, Pos.CENTER_RIGHT);
            BorderPane.setMargin(button, new Insets(1, 20, 1, 0));
            borderpane.setBottom(button);
            borderpane.setCenter(russianBoard.getContent());
            Scene scene3 = new Scene(borderpane);
            stage.setTitle("");
            stage.setResizable(false);
            stage.setScene(scene3);
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
                        e.printStackTrace();
                    }
                }

                receive();
                if (gameType == 1)
                    actualPlayer = classicBoard.turn;
                if (gameType == 2)
                    actualPlayer = polishBoard.turn;
                if (gameType == 3)
                    actualPlayer = russianBoard.turn;
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
                        e.printStackTrace();
                    }
                }
                receive();
                if (gameType == 1)
                    actualPlayer = classicBoard.turn;
                if (gameType == 2)
                    actualPlayer = polishBoard.turn;
                if (gameType == 3)
                    actualPlayer = russianBoard.turn;
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

                Platform.runLater(() -> classicBoard.makeMove(x0, y0, x1, y1, false));
            }

            if (str.charAt(0) == 'N' || str.charAt(0) == 'K') {
                int x0 = translate(Character.getNumericValue(str.charAt(1)), 10);
                int y0 = translate(Character.getNumericValue(str.charAt(2)), 10);
                int x1 = translate(Character.getNumericValue(str.charAt(3)), 10);
                int y1 = translate(Character.getNumericValue(str.charAt(4)), 10);

                Platform.runLater(() -> polishBoard.makeMove(x0, y0, x1, y1, false));
            }

            if (str.charAt(0) == 'j' || str.charAt(0) == 'i') {
                int x0 = translate(Character.getNumericValue(str.charAt(1)), 8);
                int y0 = translate(Character.getNumericValue(str.charAt(2)), 8);
                int x1 = translate(Character.getNumericValue(str.charAt(3)), 8);
                int y1 = translate(Character.getNumericValue(str.charAt(4)), 8);

                Platform.runLater(() -> russianBoard.makeMove(x0, y0, x1, y1, false));
            }


            if (str.equals("REDWON")) {
                System.out.println("czerwony wygral");
            }
            if (str.equals("WHITEWON")) {
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
