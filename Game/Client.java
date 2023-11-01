import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;



public class Client {

	public static void main(String[] args) {
		try {

			String hostname = args[0];
			int port = 10000;
			System.out.println("Connecting to the game server on this port " + port);
			Socket connectionSock = new Socket(hostname, port);
			DataOutputStream toserver = new DataOutputStream(connectionSock.getOutputStream());
			BufferedReader fromserver = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));

			System.out.println("Connection has been  made.");


			Scanner keyboard = new Scanner(System.in);

			System.out.println("Enter N/R/C");
			String userInput = keyboard.nextLine().toLowerCase();
			
			pState player = new pState();

			if (userInput.equals("n")) {
				toserver.writeBytes("n\n");
				String nn = fromserver.readLine();
				if (nn.equals("-1")) {
					System.out.println("Error in game creation please rejoin");
					connectionSock.close();
					return;

				}
				System.out.println("Game Id:" + nn);
				player.myTurn=true;
				Listener listener = new Listener(connectionSock,1,player);
				Thread theThread = new Thread(listener);
				theThread.start();

			} else if (userInput.equals("r")) {
				toserver.writeBytes("r\n");
				String nn = fromserver.readLine();
				if(nn.equals("-1")){
					System.out.println("No Game Available");
					connectionSock.close();
					return;
				}
				else {
					System.out.println("Game Id:" + nn);
				}
				player.myTurn=false;
				Listener listener = new Listener(connectionSock,-1,player);
				Thread theThread = new Thread(listener);
				theThread.start();

			} else if (userInput.equals("c")) {
				toserver.writeBytes("c\n");
				String nn = fromserver.readLine();
				System.out.println(nn);
				if(nn.equals("-1")) {
					System.out.println("Try again");
					connectionSock.close();
					return;

				}
				System.out.println("Give Game Id");
				String id = keyboard.nextLine();
				id = id + '\n';
				toserver.writeBytes(id);

				nn=fromserver.readLine();
				if (nn.toLowerCase().equals("-1")) {
					System.out.println("GameId doesn't exist");
					connectionSock.close();
					return;
				}
				player.myTurn=false;
				Listener listener = new Listener(connectionSock,-1,player);
				Thread theThread = new Thread(listener);
				theThread.start();
			}

			while (toserver != null) { 
				String data = keyboard.nextLine();
				if (!player.myTurn) {
					System.out.println("Opponent's move !Please wait ");
				} else if ((data.equals("0") || data.equals("1")) || data.equals("2")) {
					toserver.writeBytes(data + "\n");
				} else if (data.equals("quit")) {
					toserver.close();
					toserver = null;
				} else {
					System.out.println("Invalid input, please try again.");
				}
			}
			System.out.println("Connection lost.");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}