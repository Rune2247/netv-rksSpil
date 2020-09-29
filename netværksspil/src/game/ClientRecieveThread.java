package game;

import java.io.BufferedReader;
import java.net.Socket;

public class ClientRecieveThread extends Thread {
	
	private Socket theSocket;
	private BufferedReader inFromServer;
	private Player player;
	
	public ClientRecieveThread(Socket theSocket, BufferedReader inFromUser, Player player) {
		this.theSocket = theSocket;
		this.inFromServer = inFromUser;
		this.player = player;
	}
	
	public void recieveData() {
		
	}
	
	public void updatePlayer(String pName) {
		player.name = pName;
	}
}
