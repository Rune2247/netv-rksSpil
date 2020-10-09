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
				Server.sendReset();
				
				// Break
				return;
			}
			// Hvis ikke "reset" så pak json ud :)
			System.out.println(modifiedSentence);
			udPakComando(modifiedSentence);

			// SEND___________________________________________
			Server.sendGameState();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reset() {
		ArrayList<Player> newPlayers = new ArrayList<>();
		// Setter spillere op

		pair nSpawn;
		for (int i = 0; i < Server.gameState.getPlayers().size(); i++) {
			nSpawn = Server.getFreeSpawn(newPlayers);
			System.out.println("New pos: " + nSpawn.x + ", " + nSpawn.y);

			Server.gameState.players.get(i).setXpos(nSpawn.getX());
			Server.gameState.players.get(i).setYpos(nSpawn.getY());
			Server.gameState.players.get(i).resetPoints();
			Server.gameState.players.get(i).setFruit("ingen");

			newPlayers.add(Server.gameState.players.get(i));
		}
		Server.gameState.score = 0;
		// Opret nyt gameState med de nye spiller pos
		// GameState newGameState = new GameState(0, newPlayers);
		System.out.println("Game Reset");
		Server.generateTaskList();
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
			collectFruit(Server.gameState.getPlayers().get(i));
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

		// kigger efter gryde
		if (fields[nY].charAt(nX) == '1' || fields[nY].charAt(nX) == '2' || fields[nY].charAt(nX) == '3'
				|| fields[nY].charAt(nX) == '4') {
			check = false;
			if (Server.gameState.getPlayers().get(id).getFruit() != null) {

				if (Server.gameState.getPlayers().get(id).fruit.equals(Server.gameState.frugtList.get(0).name)) {
					Server.taskComplete();

				}
			}
			Server.gameState.getPlayers().get(id).setFruit("ingen");
			// score på frugt list tjekkes her

			System.out.println("Du går ind i gryden " + id + " Ovi har taget din frugt");
		}

		return check;

	}

	private void collectFruit(Player player) {
		String[] fields = Generel.board;
		int id = player.getId();
		int nY = Server.gameState.getPlayers().get(id).getYpos();
		int nX = Server.gameState.getPlayers().get(id).getXpos();
		if (fields[nY].charAt(nX) == 'p') {
			player.setFruit("peach");
		} else if (fields[nY].charAt(nX) == 'b') {
			player.setFruit("banana");
		}
	}

}
