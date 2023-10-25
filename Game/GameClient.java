
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class GameClient {

	public static void main(String[] args) {
		try {
			String hostname = "localhost";
			int port = 7654;
			boolean myTurn = true;
			System.out.println("Connecting to game server on port " + port);
			Socket connectionSock = new Socket(hostname, port);

			DataOutputStream serverOutput = new DataOutputStream(connectionSock.getOutputStream());
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));

			System.out.println("Connection made.");

			System.out.println("nnn");

			Scanner keyboard = new Scanner(System.in);

			System.out.println("Enter N/R/C");
			String userInput = keyboard.nextLine().toLowerCase();
			

			if (userInput.equals("n")) {
				serverOutput.writeBytes("n\n");
				String nn = serverInput.readLine();
				if (nn.equals("-1")) {
					System.out.println("Error in game creation please rejoin");
					connectionSock.close();
					return;

				}
				System.out.println("Game Id:" + nn);
				GameListener listener = new GameListener(connectionSock);
				Thread theThread = new Thread(listener);
				theThread.start();

			} else if (userInput.equals("r")) {
				serverOutput.writeBytes("r\n");
				String nn = serverInput.readLine();
				if(nn.equals("-1")){
					System.out.println("No Game Available");
					connectionSock.close();
					return;
				}
				else {
					System.out.println("Game Id:" + nn);
				}
				GameListener listener = new GameListener(connectionSock);
				Thread theThread = new Thread(listener);
				theThread.start();

			} else if (userInput.equals("c")) {
				serverOutput.writeBytes("c\n");
				String nn = serverInput.readLine();
				System.out.println(nn);
				if(nn.equals("-1")) {
					System.out.println("Try again");
					connectionSock.close();
					return;

				}
				System.out.println("Give Game Id");
				String id = keyboard.nextLine();
				id = id + '\n';
				serverOutput.writeBytes(id);

				nn=serverInput.readLine();
				if (nn.toLowerCase().equals("-1")) {
					System.out.println("GameId doesn't exist");
					connectionSock.close();
					return;
				}

				GameListener listener = new GameListener(connectionSock);
				Thread theThread = new Thread(listener);
				theThread.start();
			}

			while (serverOutput != null) { // changes
				String data = keyboard.nextLine();
				if (!myTurn) {
					System.out.println("Please wait for your turn.");
				} else if ((data.equals("0") || data.equals("1")) || data.equals("2")) {
					serverOutput.writeBytes(data + "\n");
				} else if (data.equals("quit")) {
					serverOutput.close();
					serverOutput = null;
				} else {
					System.out.println("Invalid input, pleas try again.");
				}
			}
			System.out.println("Connection lost.");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
} 