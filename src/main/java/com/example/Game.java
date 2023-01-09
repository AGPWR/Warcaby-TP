package com.example;

import java.io.*;
import java.net.Socket;

public class Game implements Runnable {

    private Socket firstPlayer;
    private Socket secondPlayer;


    private final static int FIRST = 1;
    private final static int SECOND = 2;
    private static int turn = FIRST;


    public Game(Socket firstPlayer, Socket secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;


    }

    @Override
    public void run() {

        try {
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
            do {
                if (turn == SECOND) {
                    line = inS.readLine();
                    System.out.println(line);
                    outF.println( line );
                    if(line.equals("t"))
                        turn = FIRST;
                }

                if (turn == FIRST) {
                    line = inF.readLine();
                    System.out.println(line);
                    outS.println(line);
                    if(line.equals("t"))
                        turn = SECOND;
                }
            } while (true);

        } catch (IOException ex) {
            System.err.println("ex");
        }
    }


}