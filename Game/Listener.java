import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Listener implements Runnable {
	private Socket connectionSock = null;
	private int turn;
	pState player;
	Listener(Socket sock,int trn,pState pl) {
		this.connectionSock = sock;
		this.turn = trn;
		player=pl;
	}

	public void run() {
		try {
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
			if(turn==-1){
				System.out.println("[*] You are second player designated as 'O', you will go second.");
				player.myTurn=false;
			}
			else{
				System.out.println("[*] You are first player designated as 'X', you will go first.");
				System.out.println("[YOUR TURN] Please enter a row (0-2): ");
				player.myTurn=true;
			}
			while (true) {
				if (serverInput == null) {
					System.out.println("Closing connection for socket " + connectionSock);
					connectionSock.close();
					break;
				}

				String serverText = serverInput.readLine();

				if (serverText.startsWith("#")) {
					printBoardFormatted(serverText.substring(1));
				} else if (serverText.startsWith("+")) {
					player.myTurn=true;
				} else if (serverText.startsWith("-")) {
					player.myTurn=false;
				} else {
					System.out.println(serverText);
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}

	private void printBoardFormatted(String boardData) {
		String[] lines = boardData.split(";");
		String[][] boardmatrix = new String[3][3];
		for (int i = 0; i < 3; ++i) {
			boardmatrix[i] = lines[i].split(",");
		}
		for (int k = 0; k < 3; ++k) {
			for (int j = 0; j < 3; ++j) {
				if (boardmatrix[k][j].equals("1")) {
					boardmatrix[k][j] = "X";
				} else if (boardmatrix[k][j].equals("-1")) {
					boardmatrix[k][j] = "O";
				} else {
					boardmatrix[k][j] = " ";
				}
			}
		}
		System.out.println("   0   1   2");
		System.out.format("0 %2s |%2s |%2s \n", boardmatrix[0][0], boardmatrix[0][1], boardmatrix[0][2]);
		System.out.println("  ---|---|---");
		System.out.format("1 %2s |%2s |%2s \n", boardmatrix[1][0], boardmatrix[1][1], boardmatrix[1][2]);
		System.out.println("  ---|---|---");
		System.out.format("2 %2s |%2s |%2s \n", boardmatrix[2][0], boardmatrix[2][1], boardmatrix[2][2]);

	}
}