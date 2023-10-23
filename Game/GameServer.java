/*
Tic Tac Toe Game Server

created by Shevsi Johnson
Oct 21, 2016

*/

import java.net.*;
import java.io.*;
import java.util.*;

public class GameServer {
	// Maintain list of all client sockets for broadcast
	public HashMap<Integer, Socket> clientSock=new HashMap<>();
	public HashMap<Integer, TTTInterface> gameMap=new HashMap<>();
	public HashMap<Integer, Vector<Integer>> gamePlayerId=new HashMap<>();
	public HashMap<Integer, Socket[]> gameSockList=new HashMap<>();
	public Set<Integer> gameList=new HashSet<>();
	// public HashMap<Integer,


	public void getConnection(ServerSocket serverSock) {
		// Wait for a connection from the client
		try {
			// This is an infinite loop, the user will have to shut it down

			int pi=0;
			int k=0;
			while(true){
				Socket connectionSock = serverSock.accept();
				clientSock.put(pi,connectionSock);

				BufferedReader playerInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
				DataOutputStream clientOutput = new DataOutputStream(connectionSock.getOutputStream());


				System.out.println("connection is made");

				boolean tt=true;
				while(tt){
					boolean b=false;
					String gameType = playerInput.readLine().trim();
					TTTInterface game = new TTTInterface();
					Socket[] sockList=new Socket[2];
					String res="yes";
					int pId=1;
					
					System.out.println(gameType);

					if(gameType.equals("n")){
						game = new TTTInterface();
						Vector<Integer> v1 = new Vector<>();
						v1.add(pi);
						sockList=new Socket[2];
						sockList[0]=connectionSock;
						
						gameList.add(k);
						gameMap.put(k,game);
						gameSockList.put(k,sockList);
						gamePlayerId.put(k,v1);
						
						res="";
						res=res+k;

						try{
							clientOutput.writeBytes(res);
						}
						catch (IOException e) {
							System.out.println(e.getMessage());
						}

						b=true;
						System.out.println(k);
						k++;
					}
					else if(gameType.equals("r")){
						if(gameList.size()>0){
							int randomIndex = new Random().nextInt(gameList.size());
							Iterator<Integer> iterator = gameList.iterator();
							int randomElement = 0;
							for (int i = 0; i <= randomIndex; i++) {
								randomElement = iterator.next();
							}
							gamePlayerId.get(randomElement).add(pi);
							gameSockList.get(randomElement)[1]=connectionSock;
							game=gameMap.get(randomElement);
							sockList=gameSockList.get(randomElement);
							gameList.remove(randomElement);
							
							try{
								clientOutput.writeBytes(res);
							}
							catch (IOException e) {
								System.out.println(e.getMessage());
							}

							pId = -1;
							b=true;
						}
					}
					else if(gameType.equals("c")){
						try{
							clientOutput.writeBytes(res);
						}
						catch (IOException e) {
							System.out.println(e.getMessage());
						}
						
						String gi=playerInput.readLine();
						System.out.println("here");
						int gid=Integer.parseInt(gi);
						if(gameMap.get(gid)!=null){

							gamePlayerId.get(gid).add(pi);
							gameSockList.get(gid)[1]=connectionSock;
							game=gameMap.get(gid);
							sockList=gameSockList.get(gid);
							pId=-1;
							b=true;
						}
					}
					
					if(b){
						GameHandler handler = new GameHandler(connectionSock, sockList , game, pId,pi);
						Thread theThread = new Thread(handler);
						theThread.start();
						break;
					}
					else{
						res="-1";
						try{
							clientOutput.writeBytes(res);
						}
						catch (IOException e) {
							System.out.println(e.getMessage());
						}
					}
				}


				pi++;
			}
			
			
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
			System.out.println("i");
		}
	}
	
	
	
	
	public static void main(String[] args) {
		
		System.out.println("Waiting for player connections on port 7654.");
		try{
			ServerSocket serverSock = new ServerSocket(7654);

			GameServer server = new GameServer();
			server.getConnection(serverSock);
			
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("if");
		}

	}
} // MTServer





// int playerID = 1;

// for (int i = 0; i < 2; ++i) {
// 	// Add this socket to the list
// 	// Send to ClientHandler the socket and arraylist of all sockets
	
// 	System.out.println("Player " + Integer.toString(i + 1) + " connected successfully.");
	
// 	GameHandler handler = new GameHandler(connectionSock, this.socketList, game, playerID);
// 	Thread theThread = new Thread(handler);
// 	theThread.start();
// 	playerID -= 2;
// }


// System.out.println("Game running...");

// // Socket connectionSock = serverSock.accept();

// for (int i = 0; i < this.socketList.length; ++i) {
// 	socketList[i].close();
// }

// Will never get here, but if the above loop is given
// an exit condition then we'll go ahead and close the socket
// serverSock.close();