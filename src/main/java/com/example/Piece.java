package com.example;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static com.example.ClassicBoard.TILE_SIZE;

/**
 * Klasa pionka.
 */
public class Piece extends StackPane {
  /**
   * Typ pionka.
   */
  private PieceType type;
  /**
   * Czy jest dama.
   */
  private boolean queen;
  /**
   * Wspolrzedne myszki.
   */
  private double mouseX, mouseY;
  /**
   * Wczesniejsze wspolrzedne pionka.
   */
  private double oldX, oldY;

  /**
   * Konstruktor pionka.
   * @param type typ pionka
   * @param x wspolrzedna x pionka
   * @param y wspolrzedna x pionka
   * @param canMove czy pionek moze sie ruszac
   */
  public Piece(PieceType type, int x, int y, boolean canMove) {
    this.type = type;
    queen = false;
    move(x, y);

    Ellipse bg = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
    bg.setFill(Color.BLACK);

    bg.setStroke(Color.BLACK);
    bg.setStrokeWidth(TILE_SIZE * 0.03);

    bg.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
    bg.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 + TILE_SIZE * 0.07);

    Ellipse ellipse = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
    ellipse.setFill(type == PieceType.RED ? Color.valueOf("#c40003") : Color.valueOf("fff9f4"));

    ellipse.setStroke(Color.BLACK);
    ellipse.setStrokeWidth(TILE_SIZE * 0.03);

    ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
    ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);

    getChildren().addAll(bg, ellipse);
    if (canMove) {
      setOnMousePressed(e -> {
        toFront();
        mouseX = e.getSceneX();
        mouseY = e.getSceneY();
      });
      setOnMouseDragged(e -> {
        if (getScene().getHeight() > e.getSceneY() && e.getSceneY() > 0 &&
            getScene().getWidth() > e.getSceneX() && e.getSceneX() > 0) {
          relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        }
      });
    }
  }

  /**
   * Metoda ruszajaca pionka
   * @param x docelowa wspolrzedna x ruchu pionka
   * @param y docelowa wspolrzedna y ruchu pionka
   */
  public void move(int x, int y) {
    oldX = x * TILE_SIZE;
    oldY = y * TILE_SIZE;
    relocate(oldX, oldY);
  }

  /**
   * Metoda zwracajaca typ pionka.
   * @return typ pionka
   */
  public PieceType getType() {
    return this.type;
  }

  /**
   * Metoda pobiera wczesniejsza wspolrzedna x.
   * @return wczesniejsza wspolrzedna x.
   */
  public double getOldX() {
    return oldX;
  }
  /**
   * Metoda pobiera wczesniejsza wspolrzedna y.
   * @return wczesniejsza wspolrzedna y.
   */
  public double getOldY() {
    return oldY;
  }

  /**
   * Metoda porzucajaca ruch.
   */
  public void abortMove() {
    relocate(oldX, oldY);
  }

  /**
   * Metoda awansowania pionka na dame.
   */
  public void change() {
    this.queen = true;
    Ellipse ellipse = new Ellipse(TILE_SIZE * 0.20, TILE_SIZE * 0.1664);
    ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
    ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
    ellipse.setFill(Color.BLACK);
    getChildren().addAll(ellipse);

  }

  /**
   * Metoda sprawdzajaca czy pionek jest dama.
   * @return true of false
   */
  public boolean isQueen() {
    return queen;
  }
}