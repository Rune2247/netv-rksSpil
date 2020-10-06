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
	public static GameState gameState = new GameState(0, new ArrayList<Player>());

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
		ArrayList<pair> pairs = new ArrayList<>();
		ArrayList<pair> spawnPairs = new ArrayList<>();
		pair p1 = new pair(9, 9);
		pair p2 = new pair(9, 10);
		pair p3 = new pair(9, 11);
		pair p4 = new pair(9, 12);
		pair p5 = new pair(10, 9);
		pair p6 = new pair(10, 12);
		pair p7 = new pair(11, 9);
		pair p8 = new pair(11, 12);
		pair p9 = new pair(12, 9);
		pair p10 = new pair(12, 10);
		pair p11 = new pair(12, 11);
		pair p12 = new pair(12, 12);
		spawnPairs.add(p1);
		spawnPairs.add(p2);
		spawnPairs.add(p3);
		spawnPairs.add(p4);
		spawnPairs.add(p5);
		spawnPairs.add(p6);
		spawnPairs.add(p7);
		spawnPairs.add(p8);
		spawnPairs.add(p9);
		spawnPairs.add(p10);
		spawnPairs.add(p11);
		spawnPairs.add(p12);

		for (int i = 0; i < players.size(); i++) {
			pair temp = new pair(players.get(i).getXpos(), players.get(i).getYpos());
			pairs.add(temp);

		}

		if (pairs.size() == 0) {
			return spawnPairs.get(0);
		} else if (pairs.size() == 1) {
			return spawnPairs.get(1);
		} else if (pairs.size() == 2) {
			return spawnPairs.get(2);
		} else if (pairs.size() == 3) {
			return spawnPairs.get(3);
		} else if (pairs.size() == 4) {
			return spawnPairs.get(4);
		} else if (pairs.size() == 5) {
			return spawnPairs.get(5);
		} else if (pairs.size() == 6) {
			return spawnPairs.get(6);
		} else if (pairs.size() == 7) {
			return spawnPairs.get(7);
		} else if (pairs.size() == 8) {
			return spawnPairs.get(8);
		} else if (pairs.size() == 9) {
			return spawnPairs.get(9);
		} else if (pairs.size() == 10) {
			return spawnPairs.get(10);
		} else if (pairs.size() == 11) {
			return spawnPairs.get(11);
		} else
			return pairs.get(12);

	}

}