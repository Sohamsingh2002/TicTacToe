/*
Tic Tac Toe Game Handler

created by Shevis Johnson
Oct 21, 2016
*/

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class GameHandler implements Runnable {

  public Socket connectionSock;
  public Socket[] socketList;
  public TTTInterface game;
  public int playerID;
  public int pId;

  public GameHandler(Socket sock, Socket[] socketList, TTTInterface game, int playerID,int pId) {
    this.connectionSock = sock;
    this.socketList = socketList; // Keep reference to master list
    this.game = game;
    this.playerID = playerID;
    this.pId=pId;
  }

  public void run() {
    try {
      // TTTInterface game = new TTTInterface();

      BufferedReader playerInput = new BufferedReader(new InputStreamReader(this.connectionSock.getInputStream()));

      switch (this.playerID) {
        case -1:
          sendMessage("\nYou are player 'O', you will go second." + "\r\n");
          sendMessage("-" + "\r\n");
          break;
        case 1:
          sendMessage("\nYou are player 'X', you will go first." + "\r\n");
          sendMessage("+" + "\r\n");
          break;
        default:
          break;
      }

      while (this.game.checkWin() == 0) {
        sendMessage(this.game.printState() + "\r\n");
        String playerSym = "";
        int playerIndex = 1;
        int indexInverse = 0;
        if (this.game.playerMove == this.playerID) {
          // my turn
          sendMessage("Pleaae enter a row (0-2): " + "\r\n");
          String row = playerInput.readLine().trim();
          sendMessage("Pleaae enter a column (0-2): " + "\r\n");
          String col = playerInput.readLine().trim();
          if (!(this.game.submitMove(Integer.parseInt(row), Integer.parseInt(col)))) {
            sendMessage("Invalid move." + "\r\n");
          } else {
            sendMessage("-" + "\r\n");
          }
        } else {
          // other player's turn
          sendMessage("Please wait for opponent's move." + "\r\n");
          while (this.game.playerMove != this.playerID) {
            Thread.sleep(500);
          }
          sendMessage("+" + "\r\n");
        }
      }

      sendMessage(this.game.printState());

      int checkResult = this.game.checkWin();
      sendMessage(Integer.toString(checkResult) + "\r\n");
      if (checkResult == this.playerID) {
        sendMessage("GAME OVER! YOU WIN!" + "\r\n");
      } else if (checkResult == 2) {
        sendMessage("GAME OVER! TIE GAME!" + "\r\n");
      } else {
        sendMessage("GAME OVER! YOU LOSE!" + "\r\n");
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } catch (InterruptedException z) {
      System.out.println(z.getMessage());
    }
  }

  private void sendMessage(String message) { // 0 = O, 1 = X, 2 = both
    try {
      DataOutputStream clientOutput = new DataOutputStream(this.connectionSock.getOutputStream());
      clientOutput.writeBytes(message);
      // System.out.println(message);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

}
