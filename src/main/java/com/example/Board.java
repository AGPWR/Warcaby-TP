package com.example;

public class Board {
  private int[][] positions;
  
  int getColor(int x,int y){
    if((x+y)%2!=0){
        return 1;
    }
    else
      return 0;
  }

  Board(int n){
    this.positions=new int[n][n];
  }



}
