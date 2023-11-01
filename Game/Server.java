
import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

	public HashMap<Integer, Socket> clientSock=new HashMap<>();
	public HashMap<Integer, gameInterface> gameMap=new HashMap<>();
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

				BufferedReader fromClient = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
				DataOutputStream toClient = new DataOutputStream(connectionSock.getOutputStream());

				System.out.println("Connection is made");

				boolean b=false;
				String gameType = fromClient.readLine().trim();
				gameInterface game = null;
				String res="-1\r\n";
				int pId=1;

				if(gameType.equals("n")){
					game = new gameInterface();
					Vector<Integer> v1 = new Vector<>();
					v1.add(uid);

					gameList.add(gid);
					gameMap.put(gid,game);
					gamePlayerId.put(gid,v1);

					res="";
					res=res+gid+"\r\n";
					sendMes(toClient, res);
					b=true;
					gid++;
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
						res=""+randomElement+"\r\n";
						sendMes(toClient, res);
						pId = -1;
						b=true;
					}
				}

				else if(gameType.equals("c")){

					res="yes\r\n";
					sendMes(toClient, res);

					boolean bb=true;
					if(bb){
						String gi=fromClient.readLine();
						int gd=Integer.parseInt(gi);
						if(gameMap.get(gd)!=null){
							gamePlayerId.get(gd).add(uid);
							game=gameMap.get(gd);
							gameList.remove(gd);
							pId=-1;
							b=true;
							bb=false;
							res="yes\r\n";
							sendMes(toClient, res);
						}
					}
				}
				
				if(b){
					Controllar handler = new Controllar(connectionSock, game, pId,uid);
					Thread theThread = new Thread(handler);
					theThread.start();

				}
				else{
					res="-1\r\n";
					try{
						toClient.writeBytes(res);
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
	
	
	public void sendMes(DataOutputStream toClient,String res){
		try{
			toClient.writeBytes(res);
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		final int PORT = 10000;
		try{
			ServerSocket serverSock = new ServerSocket(PORT);
			InetAddress serverAddress = InetAddress.getLocalHost();
			System.out.println("Server Started ");
            System.out.println("Server IP: " + serverAddress.getHostAddress());
            System.out.println("Server Port: " + PORT);
			Server server = new Server();
			server.getConnection(serverSock);
			
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("if");
		}

	}
} 