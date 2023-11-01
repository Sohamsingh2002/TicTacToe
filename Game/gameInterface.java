
import java.io.*;
import java.util.*;
import java.lang.*;

public class gameInterface {

  public gameInterface() {
    this.board = new int[3][3];
    this.pMove = 1;
  }

  public volatile int pMove; //X = 1, O = -1
  private int[][] board; //3x3


  public boolean move(int i, int j) {
    if (this.board[i][j] != 0) {
      return false;
    } else {
      this.board[i][j] = this.pMove;
      this.pMove = -this.pMove;
      return true;
    }
  }

  public String currentState() {
    String output = "#";
    for (int i = 0; i < 3; ++i) {
      output += Integer.toString(this.board[i][0]) + "," + Integer.toString(this.board[i][1]) + "," + Integer.toString(this.board[i][2]) + ";";
    }
    return output;
  }

  public int checkWin() {
    boolean cats = true;
    for (int i = 0; i < 3; ++i) {
      if ((this.board[i][0] == this.board[i][1] && this.board[i][0] == this.board[i][2]) && this.board[i][0] != 0) { return this.board[i][0]; }
    }
    for (int i = 0; i < 3; ++i) {
      if ((this.board[0][i] == this.board[1][i] && this.board[0][i] == this.board[2][i]) && this.board[0][i] != 0) { return this.board[0][i]; }
    }
    if ((this.board[0][0] == this.board[1][1] && this.board[0][0] == this.board[2][2]) && this.board[0][0] != 0) { return this.board[0][0]; }
    if ((this.board[2][0] == this.board[1][1] && this.board[2][0] == this.board[0][2]) && this.board[2][0] != 0) { return this.board[2][0]; }
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 3; ++j) {
        if (this.board[i][j] == 0) { cats = false; }
      }
    }
    if (cats) {
      return 2;
    } else {
      return 0;
    }
  }

}
