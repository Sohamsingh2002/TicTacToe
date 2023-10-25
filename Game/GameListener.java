

import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class GameListener implements Runnable {
	private Socket connectionSock = null;
	private boolean myTurn;

	GameListener(Socket sock) {
		this.connectionSock = sock;
		// this.myTurn = myTurn;
	}

	public void run() {
		// Wait for data from the server. If received, output it.
		try {
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
			while (true) {
				if (serverInput == null) {
					// Connection was lost
					System.out.println("Closing connection for socket " + connectionSock);
					connectionSock.close();
					break;
				}
				// Get data sent from the server

				// System.out.println("A");
				String serverText = serverInput.readLine();
				// System.out.println("B");
				// System.out.println(serverText);

				if (serverText.startsWith("#")) {
					printBoardFormatted(serverText.substring(1));
				} else if (serverText.startsWith("~")) {
					// wait
				} else if (serverText.startsWith("+")) {
					this.myTurn = true;
				} else if (serverText.startsWith("-")) {
					this.myTurn = false;
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
		// System.out.println(Integer.toString(lines.length));
		String[][] board = new String[3][3];
		for (int i = 0; i < 3; ++i) {
			board[i] = lines[i].split(",");
		}
		for (int k = 0; k < 3; ++k) {
			for (int j = 0; j < 3; ++j) {
				if (board[k][j].equals("1")) {
					board[k][j] = "X";
				} else if (board[k][j].equals("-1")) {
					board[k][j] = "O";
				} else {
					board[k][j] = " ";
				}
			}
		}
		System.out.println("   0   1   2");
		System.out.format("0 %2s |%2s |%2s \n", board[0][0], board[0][1], board[0][2]);
		System.out.println("  ---|---|---");
		System.out.format("1 %2s |%2s |%2s \n", board[1][0], board[1][1], board[1][2]);
		System.out.println("  ---|---|---");
		System.out.format("2 %2s |%2s |%2s \n", board[2][0], board[2][1], board[2][2]);

	}
} 
