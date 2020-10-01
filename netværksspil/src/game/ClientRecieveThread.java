package game;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.ArrayList;


public class ClientRecieveThread extends Thread {
	
	private Socket theSocket;
	private BufferedReader inFromServer;
	private GameState gameState;
	
	public ClientRecieveThread(Socket theSocket, BufferedReader inFromServer) {
		this.theSocket = theSocket;
		this.inFromServer = inFromServer;
	}
	
	public void run() {
		while(theSocket.isConnected()) {
			recieveData();
		}
	}
	
	public void recieveData() {
		try {
			String recived = inFromServer.readLine();
			gameState = GameState.modtagGameState(recived);
			System.out.println(gameState.toString());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	
}
