package game;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONString;

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
			if (recived.charAt(1) == 't') {
				System.out.println(recived);
				JSONObject jo = new JSONObject(recived);
				Client.gameState.time = jo.getDouble("tid");
			} else {
				Client.gameState = GameState.modtagGameState(recived);
			}
			Client.players = Client.gameState.getPlayers();
			Client.score = Client.gameState.getScore();
			Client.updateAllPlayers();
			Client.updateScoreTable();
			Client.updateFruitList();
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
