package game;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.ArrayList;


public class ClientRecieveThread extends Thread {
	
	private Socket theSocket;
	private BufferedReader inFromServer;
	private Player player;
	
	public ClientRecieveThread(Socket theSocket, BufferedReader inFromServer, Player player) {
		this.theSocket = theSocket;
		this.inFromServer = inFromServer;
		this.player = player;
	}
	
	public void run() {
		while(theSocket.isConnected()) {
			recieveData();
		}
	}
	
	public void recieveData() {
		try {
			String recived = inFromServer.readLine();
			System.out.println(recived);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void updatePlayer(int xpos, int ypos,  String direction) {
		player.direction = direction;
		player.xpos = xpos;
		player.ypos = ypos;
	}
	
	public void updatePlayerName(String pName) {
		player.name = pName;
	}
}
