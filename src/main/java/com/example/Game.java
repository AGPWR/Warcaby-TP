package com.example;

import java.io.*;
import java.net.Socket;

/**
 * Klasa tworzaca gre.
 */
public class Game implements Runnable {
  /**
   * Socket pierwszego gracza.
   */
  private final Socket firstPlayer;
  /**
   * Socket drugiego gracza.
   */
  private final Socket secondPlayer;
  /**
   * Statyczne pole first.
   */
  private final static int FIRST = 1;
  /**
   * Statyczne pole second.
   */
  private final static int SECOND = 2;

  /**
   * Konstruktor gry
   * @param firstPlayer gracz 1
   * @param secondPlayer gracz 2
   */
  public Game(Socket firstPlayer, Socket secondPlayer) {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
  }

  /**
   * Metoda startujaca gre.
   */
  @Override
  public void run() {
    try {
      int turn = FIRST;
      System.out.println("game starts");
      InputStream inputF = firstPlayer.getInputStream();
      BufferedReader inF = new BufferedReader(new InputStreamReader(inputF));

      InputStream inputS = secondPlayer.getInputStream();
      BufferedReader inS = new BufferedReader(new InputStreamReader(inputS));

      OutputStream outputF = firstPlayer.getOutputStream();
      PrintWriter outF = new PrintWriter(outputF, true);

      OutputStream outputS = secondPlayer.getOutputStream();
      PrintWriter outS = new PrintWriter(outputS, true);

      outF.println("1");
      outS.println("2");

      String line;
      boolean x = false;

      do {
        if (turn == SECOND) {
          line = inS.readLine();
          System.out.println(line);
          outF.println(line);
          if (line.equals("end")) {
            System.out.println(line);
            outF.println(line);
            if (x) {
              break;
            }
            x = true;
          }
          if (line.equals("t")) {
            turn = FIRST;
          }
        }

        if (turn == FIRST) {
          line = inF.readLine();
          System.out.println(line);
          outS.println(line);
          if (line.equals("end")) {
            System.out.println(line);
            outS.println(line);
            if (x) {
              break;
            }
            x = true;
          }
          if (line.equals("t")) {
            turn = SECOND;
          }
        }
      } while (true);
      System.out.println("game Thread ends");
    } catch (IOException ex) {
      System.err.println("ex");
    }
  }
}