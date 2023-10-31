
import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class GameHandler implements Runnable {

  public Socket connectionSock;
  // public Socket[] socketList;
  public TTTInterface game;
  public int playerID;
  public int uid;

  public GameHandler(Socket sock, TTTInterface game, int playerID, int uid) {
    this.connectionSock = sock;
    this.game = game;
    this.playerID = playerID;
    this.uid = uid;
  }

  public void run() {
    try {

      BufferedReader playerInput = new BufferedReader(new InputStreamReader(this.connectionSock.getInputStream()));

      System.out.println("GameHndler");

      switch (this.playerID) {
        case -1:
          sendMessage("\nYou are player 'O', you will go second." + "\r\n");
          sendMessage("-" + "\r\n");
          break;
        case 1:
          System.out.println("Message has been Sent");
          sendMessage("\nYou are player 'X', you will go first." + "\r\n");
          sendMessage("+" + "\r\n");
          break;
        default:
          break;
      }

      while (this.game.checkWin() == 0) {

        if (this.game.playerMove == this.playerID) {
          // my turn
          // playerInput.readLine();
          sendMessage("Pleaae enter a row (0-2): " + "\r\n");
          String row = playerInput.readLine().trim();
          sendMessage("Pleaae enter a column (0-2): " + "\r\n");
          String col = playerInput.readLine().trim();
          if (!(this.game.submitMove(Integer.parseInt(row), Integer.parseInt(col)))) {
            sendMessage("Invalid move." + "\r\n");
          } else {
            sendMessage("-" + "\r\n");
          }
          sendMessage(this.game.printState());
        } else {
          // other player's turn
          sendMessage("Please wait for opponent's move." + "\r\n");
          // sendMessage(this.game.printState());
          while (this.game.playerMove != this.playerID) {
            Thread.sleep(500);
          }
          sendMessage("Opponent has moved." + "\r\n");
          sendMessage(this.game.printState());

          sendMessage("+" + "\r\n");
        }
      }

      int checkResult = this.game.checkWin();
      sendMessage(Integer.toString(checkResult) + "\r\n");
      if (checkResult == this.playerID) {
        sendMessage("GAME OVER! YOU WIN!" + "\r\n");
      } else if (checkResult == 2) {
        sendMessage("GAME OVER! TIE GAME!" + "\r\n");
      } else {
        sendMessage("GAME OVER! YOU LOSE!" + "\r\n");
      }

      // gameMap.remove()
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
      // System.out.println(message+"=--");
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

}
