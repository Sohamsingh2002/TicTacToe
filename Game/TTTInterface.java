/*
Tic Tac Toe Game interface

Created by Shevis Johnson
Oct 21, 2016
*/

import java.io.*;
import java.util.*;
import java.lang.*;

public class TTTInterface {

  public TTTInterface() {
    this.currentBoard = new int[3][3];
    this.playerMove = 1;
  }

  public volatile int playerMove; //X = 1, O = -1
  private int[][] currentBoard; //3x3

  public void resetGame() {
    this.currentBoard = new int[3][3];
    this.playerMove = 1;
  }

  public boolean submitMove(int i, int j) {
    if (this.currentBoard[i][j] != 0) {
      return false;
    } else {
      this.currentBoard[i][j] = this.playerMove;
      this.playerMove = -this.playerMove;
      return true;
    }
  }

  public String printState() {
    String output = "#";
    for (int i = 0; i < 3; ++i) {
      output += Integer.toString(this.currentBoard[i][0]) + "," + Integer.toString(this.currentBoard[i][1]) + "," + Integer.toString(this.currentBoard[i][2]) + ";";
    }
    //output += Integer.toString(this.playerMove);
    return output;
  }

  public int checkWin() {
    boolean cats = true;
    for (int i = 0; i < 3; ++i) {
      if ((this.currentBoard[i][0] == this.currentBoard[i][1] && this.currentBoard[i][0] == this.currentBoard[i][2]) && this.currentBoard[i][0] != 0) { return this.currentBoard[i][0]; }
    }
    for (int i = 0; i < 3; ++i) {
      if ((this.currentBoard[0][i] == this.currentBoard[1][i] && this.currentBoard[0][i] == this.currentBoard[2][i]) && this.currentBoard[0][i] != 0) { return this.currentBoard[0][i]; }
    }
    if ((this.currentBoard[0][0] == this.currentBoard[1][1] && this.currentBoard[0][0] == this.currentBoard[2][2]) && this.currentBoard[0][0] != 0) { return this.currentBoard[0][0]; }
    if ((this.currentBoard[2][0] == this.currentBoard[1][1] && this.currentBoard[2][0] == this.currentBoard[0][2]) && this.currentBoard[2][0] != 0) { return this.currentBoard[2][0]; }
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 3; ++j) {
        if (this.currentBoard[i][j] == 0) { cats = false; }
      }
    }
    if (cats) {
      return 2;
    } else {
      return 0;
    }
  }

}
