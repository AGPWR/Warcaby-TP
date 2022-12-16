package com.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square {
  Rectangle square;
  Square(){
    square=new Rectangle(50,50);
  }

  public void setColor(int c) {
    if(c==1)
      this.square.setFill(Color.MEDIUMSEAGREEN);
    else
      this.square.setFill(Color.HONEYDEW);
  }
  public void getColor(){
    this.square.getFill();
  }

}
