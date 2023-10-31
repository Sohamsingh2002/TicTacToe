

import java.net.*;
import java.io.*;
import java.util.*;

public class GameServer {

	public HashMap<Integer, Socket> clientSock=new HashMap<>();
	public HashMap<Integer, TTTInterface> gameMap=new HashMap<>();
	public HashMap<Integer, Vector<Integer>> gamePlayerId=new HashMap<>();
	public Set<Integer> gameList=new HashSet<>();




	public void getConnection(ServerSocket serverSock) {

		try {

			int uid=0;
			int gid=0;
			while(true){

				System.out.println("Waiting for new users to join ...");
				Socket connectionSock = serverSock.accept();
				clientSock.put(uid,connectionSock);

				BufferedReader playerInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
				DataOutputStream clientOutput = new DataOutputStream(connectionSock.getOutputStream());

				System.out.println("connection is made");

				boolean b=false;
				String gameType = playerInput.readLine().trim();
				TTTInterface game = null;
				String res="-1\n";
				int pId=1;
				
				System.out.println("Game type - "+gameType);

				if(gameType.equals("n")){
					game = new TTTInterface();
					Vector<Integer> v1 = new Vector<>();
					v1.add(uid);

					gameList.add(gid);
					gameMap.put(gid,game);
					gamePlayerId.put(gid,v1);

					res="";
					res=res+gid+'\n';
					sendMes(clientOutput, res);
					b=true;
					gid++;

					System.out.println("Inside");
				}


				else if(gameType.equals("r")){
					if(gameList.size()>0){
						int randomIndex = new Random().nextInt(gameList.size());
						Iterator<Integer> iterator = gameList.iterator();
						int randomElement = 0;
						for (int i = 0; i <= randomIndex; i++) {
							randomElement = iterator.next();
						}
						gamePlayerId.get(randomElement).add(uid);
						game=gameMap.get(randomElement);

						gameList.remove(randomElement);
						res=""+randomElement+'\n';
						sendMes(clientOutput, res);
						pId = -1;
						b=true;
					}
				}

				else if(gameType.equals("c")){

					res="yes\n";
					sendMes(clientOutput, res);

					boolean bb=true;
					if(bb){
						String gi=playerInput.readLine();
						int gd=Integer.parseInt(gi);
						if(gameMap.get(gd)!=null){
							gamePlayerId.get(gd).add(uid);
							game=gameMap.get(gd);
							gameList.remove(gd);
							pId=-1;
							b=true;
							bb=false;
							res="yes\n";
							sendMes(clientOutput, res);
						}
					}
				}
				
				if(b){
					GameHandler handler = new GameHandler(connectionSock, game, pId,uid);
					Thread theThread = new Thread(handler);
					theThread.start();

				}
				else{
					res="-1\n";
					try{
						clientOutput.writeBytes(res);
					}
					catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
				uid++;
			}
			
			
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
			System.out.println("i");
		}
	}
	
	
	public void sendMes(DataOutputStream clientOutput,String res){
		try{
			clientOutput.writeBytes(res);
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		final int PORT = 7654;
		try{
			ServerSocket serverSock = new ServerSocket(PORT);
			InetAddress serverAddress = InetAddress.getLocalHost();
			System.out.println("Server Started ");
            System.out.println("Server IP: " + serverAddress.getHostAddress());
            System.out.println("Server Port: " + PORT);
			GameServer server = new GameServer();
			server.getConnection(serverSock);
			
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("if");
		}

	}
} 