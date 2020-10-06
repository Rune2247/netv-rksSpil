package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;

public class ServerReciveThread extends Thread {

	String modifiedSentence;
	BufferedReader inFromClient;
	Socket clientSocket;
	private boolean luk = true;

	public ServerReciveThread(BufferedReader inFromClient, Socket clientSocket) {
		this.inFromClient = inFromClient;
		this.clientSocket = clientSocket;

	}

	@Override
	public void run() {
		while (luk) {
			modtag();

		}

	}

	private void modtag() {
		// Modtag besked

		try {
			modifiedSentence = inFromClient.readLine();
			if (modifiedSentence == null) {
				luk = false;
				System.out.println("FUUUCk");
				// Break
				return;
			}
			// Hvis "reset" genstart spillet
			if (modifiedSentence.equals("r")) {
				// Clear playerlisten i nu værende gameState
				reset();
				Server.sendGameState();
				// Break
				return;
			}
			// Hvis ikke "reset" så pak json ud :)
			System.out.println(modifiedSentence);
			udPakComando(modifiedSentence);

			Server.sendGameState();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reset() {
		// ArrayList<Player> newPlayers = Server.gameState.getPlayers();
		// Setter spillere op

		pair nSpawn;
		for (int i = 0; i < Server.gameState.getPlayers().size(); i++) {
			nSpawn = Server.getFreeSpawn(Server.gameState.getPlayers());
			System.out.println("New pos: " + nSpawn.x + ", " + nSpawn.y);

			Server.gameState.getPlayers().get(i).setXpos(nSpawn.getX());
			Server.gameState.getPlayers().get(i).setYpos(nSpawn.getY());
			Server.gameState.getPlayers().get(i).resetPoints();
		}
		Server.gameState.score = 0;
		// Opret nyt gameState med de nye spiller pos
		// GameState newGameState = new GameState(0, newPlayers);
		System.out.println("Game Reset");
	}

	// Udpakker en comando fra spillerne
	private void udPakComando(String json) {

		JSONObject jo = new JSONObject(json);
		for (int i = 0; i < Server.gameState.players.size(); i++) {
			if (collision(jo) == true) {
				if (Server.gameState.getPlayers().get(i).getId() == jo.getInt("id")) {
					Server.gameState.getPlayers().get(i).xpos += jo.getInt("xpos");
					Server.gameState.getPlayers().get(i).ypos += jo.getInt("ypos");
					Server.gameState.getPlayers().get(i).direction = jo.getString("direction");
				}
			} else {
				System.out.println(jo.getInt("id") + " Må ikke rykke!");
			}
		}

	}

	private boolean collision(JSONObject joo) {
		boolean check = true;
		String[] fields = Generel.board;
		int id = joo.getInt("id");
		int nY = Server.gameState.getPlayers().get(id).getYpos() + joo.getInt("ypos");
		int nX = Server.gameState.getPlayers().get(id).getXpos() + joo.getInt("xpos");

		// Kigger efter mur
		if (fields[nY].charAt(nX) == 'w') {
			check = false;
			System.out.println("False at W");

		}

		// Kigger efter spiller
		for (int i = 0; i < Server.gameState.getPlayers().size(); i++) {
			if (Server.gameState.getPlayers().get(i).getXpos() == nX
					&& Server.gameState.getPlayers().get(i).getYpos() == nY) {
				check = false;
				System.out.println("FALSE AT player col");
			}
		}

		return check;

	}
}
