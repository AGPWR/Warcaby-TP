package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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

/**
 *
 */
public class Player extends Application implements Runnable {

  Stage stage;
  Button button;
  Socket socket = null;
  PrintWriter out = null;
  BufferedReader in = null;
  int player;
  private Board board;
  boolean game = true;
  public int gameType;
  //public int turn;


  @Override
  public void run() {
    f();
  }

  @Override
  public void start(Stage stage) {
    this.stage = stage;
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
    bp.setBackground(
        new Background(new BackgroundFill(Color.GREENYELLOW, new CornerRadii(0), Insets.EMPTY)));
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
    bp1.setBackground(
        new Background(new BackgroundFill(Color.GREENYELLOW, new CornerRadii(0), Insets.EMPTY)));
    StackPane p = new StackPane();
    p.setPrefSize(499, 499);
    p.getChildren().add(waiting);
    StackPane.setAlignment(p, Pos.CENTER);
    bp1.setCenter(p);

    Scene lookingForEnemy = new Scene(bp1, Color.GREENYELLOW);

    button = new Button("Powrót");
    button.setFocusTraversable(false);
    //button.setOnAction(event -> stage.setScene(menu));
    button.setOnAction(new EventHandler() {

      @Override
      public void handle(Event event) {
        stage.setScene(menu);
        out.println("end");
        out.println("t");
      }
    });

    b1.setOnAction(event -> {
      gameType = 1;
      game = true;
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
      if (str != null && str.equals("2")) {
        player = 2;
        board = new ClassicBoard(2, out);
      }
      if (str != null && str.equals("1")) {
        player = 1;
        board = new ClassicBoard(1, out);
      }
      startThread(board.getContent());


    });

    b2.setOnAction(event -> {
      gameType = 2;
      game = true;
      stage.setScene(lookingForEnemy);
      listenSocket();
      String str = null;
      try {
        str = in.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println(str);
      if (str != null && str.equals("2")) {

        board = new PolishBoard(2, out);
      }
      if (str != null && str.equals("1")) {
        board = new PolishBoard(1, out);
      }
      startThread(board.getContent());
    });

    b3.setOnAction(event -> {
      gameType = 3;
      game = true;
      stage.setScene(lookingForEnemy);
      listenSocket();
      String str = null;
      try {
        str = in.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println(str);
      if (str != null && str.equals("2")) {
        board = new RussianBoard(2, out);
      }
      if (str != null && str.equals("1")) {
        board = new RussianBoard(1, out);
      }
      startThread(board.getContent());
    });


  }

  private void startThread(Parent content) {
    startThread();
    BorderPane borderpane = new BorderPane();
    BorderPane.setAlignment(button, Pos.CENTER_RIGHT);
    BorderPane.setMargin(button, new Insets(1, 20, 1, 0));
    borderpane.setBottom(button);
    borderpane.setCenter(content);
    Scene scene2 = new Scene(borderpane);
    stage.setTitle("");
    stage.setResizable(false);
    stage.setScene(scene2);
  }

  public static void main(String[] args) {
    Application.launch(args);
  }


  /**
   *
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

  void f() {
    System.out.println("Thread 1 start");
    while (game) {
      synchronized (this) {
        try {
          wait(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }


        receive();
        notifyAll();
      }
    }
    System.out.println("Thread 1 end");
  }

  /**
   *
   */
  public void receive() {
    try {
      String str = in.readLine();
      System.out.println(str);

      if (str.charAt(0) == 'n' || str.charAt(0) == 'k') {
        int x0 = translate(Character.getNumericValue(str.charAt(1)), board.HEIGHT);
        int y0 = translate(Character.getNumericValue(str.charAt(2)), board.HEIGHT);
        int x1 = translate(Character.getNumericValue(str.charAt(3)), board.HEIGHT);
        int y1 = translate(Character.getNumericValue(str.charAt(4)), board.HEIGHT);

        Platform.runLater(() -> board.makeMove(x0, y0, x1, y1, false));
      }

      if (str.equals("REDWON")) {
        System.out.println("czerwony wygral");
        Platform.runLater(() -> endMenu("czerwony wygral"));
      }
      if (str.equals("WHITEWON")) {
        System.out.println("bialy wygral");
        Platform.runLater(() -> endMenu("bialy wygral"));
      }
      if (str.equals("end")) {
        out.println("end");


        game = false;
        socket.close();
        if (socket.isClosed()) {
          System.out.println("zamknieto socket");
        }
      }
    } catch (IOException e) {
      System.out.println("Read failed");
      System.exit(1);
    }
  }

  private int translate(int a, int height) {
    return height - a - 1;
  }

  private void endMenu(String message) {
    Text winner = new Text(message);
    winner.setStyle("-fx-font: 50 arial;");
    BorderPane borderpane = new BorderPane();
    borderpane.setPrefSize(499, 499);
    borderpane.setBackground(
        new Background(new BackgroundFill(Color.GREENYELLOW, new CornerRadii(0), Insets.EMPTY)));
    BorderPane.setAlignment(button, Pos.CENTER_RIGHT);
    BorderPane.setMargin(button, new Insets(1, 20, 1, 0));
    borderpane.setBottom(button);
    borderpane.setCenter(winner);
    Scene scene = new Scene(borderpane);
    stage.setScene(scene);
  }

}