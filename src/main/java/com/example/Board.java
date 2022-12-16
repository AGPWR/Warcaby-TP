package com.example;

public class Board {
  private int[][][] positions;
  
  int getColor(int x,int y){
    if((x+y)%2!=0){
        return 1;
    }
    else
      return 0;
  }

  Boolean isPone(int x, int y){
    if (this.positions[x][y][0]==1){
      return true;
    }
    else
      return false;
  }
  void addPone(int x, int y){
    this.positions[x][y][0]=1;
  }
  void removePone(int x, int y){
    this.positions[x][y][0]=0;
  }
  void movePone(int x1, int y1,int x2,int y2 ){
    removePone(x1,y1);
    addPone(x2,y2);
  }

  //Trzeba dodać wyjatek sprawdzjacy czy n jest parzyste
  Board(int n){
    this.positions=new int[n][n][1];
    for(int i=0;i<=(n-2)/2;i++){
      for(int j=0;j<n;j++){
          if(getColor(i,j)==1){
            this.positions[i][j][0]=1;
          }
          else{
            this.positions[i][j][0]=0;
          }
      }
    }
    for(int i=0;i<=(n-2)/2+3;i++){
      for(int j=0;j<n;j++){
        if(getColor(i,j)==1){
          this.positions[i][j][0]=1;
        }
        else{
          this.positions[i][j][0]=0;
        }
      }
    }

  }



}
