package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Gboard gboard=new Gboard(8);
    Scene scene=new Scene(gboard.gridPane);
    stage.setScene(scene);
    stage.setTitle("");
    stage.setResizable(false);
    stage.show();
  }

  public static void main(String[] args) {
    Application.launch(args);
  }
}