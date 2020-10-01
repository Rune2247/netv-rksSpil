package game;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientRecieveThread extends Thread {

	private Socket theSocket;
	private BufferedReader inFromServer;

	private Player player;

	private GameState gameState;

	public ClientRecieveThread(Socket theSocket, BufferedReader inFromServer) {
		this.theSocket = theSocket;
		this.inFromServer = inFromServer;
	}

	public void run() {
		while (theSocket.isConnected()) {
			recieveData();
		}
	}

	public void recieveData() {
		try {
			String recived = inFromServer.readLine();
			gameState = GameState.modtagGameState(recived);
			Client.players = gameState.getPlayers();
			Client.score = gameState.getScore();
			System.out.println("RUNE!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void updatePlayer(int xpos, int ypos, String direction) {
		player.direction = direction;
		player.xpos = xpos;
		player.ypos = ypos;
	}

	public GameState getGameState() {
		return this.gameState;
	}

	public void updatePlayerName(String pName) {
		player.name = pName;
	}

}
