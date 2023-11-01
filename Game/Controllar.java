
import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class Controllar implements Runnable {

  public Socket connectionSock;
  public gameInterface game;
  public int playerID;
  public int uid;

  public Controllar(Socket sock, gameInterface game, int playerID, int uid) {
    this.connectionSock = sock;
    this.game = game;
    this.playerID = playerID;
    this.uid = uid;
  }

  public void run() {
    try {

      BufferedReader fromPlayer = new BufferedReader(new InputStreamReader(this.connectionSock.getInputStream()));

      String s;
      while (this.game.checkWin() == 0) {
        if (this.game.pMove == this.playerID) {
          senMsgToClient("[YOUR TURN] Please enter a row (0-2): " + "\r\n");
          String row = fromPlayer.readLine().trim();
          senMsgToClient("[YOUR TURN] Please enter a column (0-2): " + "\r\n");
          String col = fromPlayer.readLine().trim();
          if (!(this.game.move(Integer.parseInt(row), Integer.parseInt(col)))) {
            senMsgToClient("[Invalid Move] - Already filled please r-enter" + "\r\n");
          } else {
            senMsgToClient("-" + "\r\n");
          }
          s = this.game.currentState();
          s = s + "\r\n";
          senMsgToClient(s);
        } else {
          senMsgToClient("[Waiting opponent] Waiting for opponent's move please wait....." + "\r\n");
          while (this.game.pMove != this.playerID) {
            Thread.sleep(500);
          }
          senMsgToClient("[] Opponent has moved." + "\r\n");
          s = this.game.currentState();
          s = s + "\r\n";
          senMsgToClient(s);
          senMsgToClient("+" + "\r\n");
        }
      }

      int checkResult = this.game.checkWin();
      senMsgToClient(Integer.toString(checkResult) + "\r\n");
      if (checkResult == this.playerID) {
        senMsgToClient("GAME OVER! YOU WIN!" + "\r\n");
      } else if (checkResult == 2) {
        senMsgToClient("GAME OVER! TIE GAME!" + "\r\n");
      } else {
        senMsgToClient("GAME OVER! YOU LOSE!" + "\r\n");
      }

    } catch (IOException e) {
      System.out.println(e.getMessage());
    } catch (InterruptedException z) {
      System.out.println(z.getMessage());
    }
  }

  private void senMsgToClient(String message) {
    try {
      DataOutputStream toPlayer = new DataOutputStream(this.connectionSock.getOutputStream());
      toPlayer.writeBytes(message);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

}
