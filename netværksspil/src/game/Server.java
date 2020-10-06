package game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.util.Pair;

public class Server {

	/**
	 * @param args
	 */

	private static ArrayList<DataOutputStream> outPutStreamList = new ArrayList<DataOutputStream>();
	// private GameState gameState = new GameState();
	public static GameState gameState = new GameState(0, new ArrayList<Player>(), new ArrayList<Fruit>(), 0);

	public static void main(String[] args) throws Exception {

		ServerSocket welcomeSocket = new ServerSocket(12345);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();

			DataOutputStream outToClient = (new DataOutputStream(connectionSocket.getOutputStream()));

			outPutStreamList.add(outToClient);

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			String newP = inFromClient.readLine();
			outToClient.writeBytes(newPlayer(gameState, newP) + "\n");

			ServerReciveThread serverRecive = new ServerReciveThread(inFromClient, connectionSocket);

			serverRecive.start();

			sendGameState();

		}

	}

// Send gameState i JSON Format til alle spillere. 
	public static void sendGameState() {
		// Pak gameState sammen

		String json = gameState.sendGameState();

		// send

		for (int i = 0; i < outPutStreamList.size(); i++) {
			try {
				outPutStreamList.get(i).writeBytes(json + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static int newPlayer(GameState gameState, String name) {
		// Denne metode finder en fri plads i spawn og opretter en ny spiller med "Navn"
		System.out.println(name + " Has joined the game");
		GameState tempState = gameState;
		pair friplads = getFreeSpawn(gameState.getPlayers());

		Player newPlayer = new Player(gameState.players.size(), name, friplads.x, friplads.y, "down", 0);

		gameState.players.add(newPlayer);

		return newPlayer.id;
	}

	public static pair getFreeSpawn(List<Player> players)
	// denne metode finder en fri plads i spawn området
	{

		// Finder tom plads i spawn
		ArrayList<pair> spawnPairs = new ArrayList<>();
		ArrayList<pair> spawned = new ArrayList<>();
		spawnPairs.add(new pair(8, 8));
		spawnPairs.add(new pair(8, 9));
		spawnPairs.add(new pair(8, 10));
		spawnPairs.add(new pair(8, 11));
		spawnPairs.add(new pair(9, 8));
		spawnPairs.add(new pair(9, 11));
		spawnPairs.add(new pair(10, 8));
		spawnPairs.add(new pair(10, 11));
		spawnPairs.add(new pair(11, 8));
		spawnPairs.add(new pair(11, 9));
		spawnPairs.add(new pair(11, 10));
		spawnPairs.add(new pair(11, 11));

		if (players.size() == 0) {
			return spawnPairs.get(0);
		}

		for (int i = 0; i < players.size(); i++) {
			spawned.add(spawnPairs.get(i));
		}

		for (int i = 0; i < spawnPairs.size(); i++) {
			if (!spawned.contains(spawnPairs.get(i))) {
				return spawnPairs.get(i);
			}

		}

		return new pair(2, 2);

		/*
		 * if (pairs.size() == 0) { return spawnPairs.get(0); } else if (pairs.size() ==
		 * 1) { return spawnPairs.get(1); } else if (pairs.size() == 2) { return
		 * spawnPairs.get(2); } else if (pairs.size() == 3) { return spawnPairs.get(3);
		 * } else if (pairs.size() == 4) { return spawnPairs.get(4); } else if
		 * (pairs.size() == 5) { return spawnPairs.get(5); } else if (pairs.size() == 6)
		 * { return spawnPairs.get(6); } else if (pairs.size() == 7) { return
		 * spawnPairs.get(7); } else if (pairs.size() == 8) { return spawnPairs.get(8);
		 * } else if (pairs.size() == 9) { return spawnPairs.get(9); } else if
		 * (pairs.size() == 10) { return spawnPairs.get(10); } else if (pairs.size() ==
		 * 11) { return spawnPairs.get(11); } else return pairs.get(12);
		 */

	}

}