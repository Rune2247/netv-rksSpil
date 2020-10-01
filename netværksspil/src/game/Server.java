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

	public static void main(String[] args) throws Exception {
		GameState gameState = new GameState(0, new ArrayList<Player>());

		ServerSocket welcomeSocket = new ServerSocket(12345);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			outPutStreamList.add(new DataOutputStream(connectionSocket.getOutputStream()));

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			String newP = inFromClient.readLine();
			newPlayer(gameState, newP);
			ServerReciveThread serverRecive = new ServerReciveThread(inFromClient, connectionSocket);

			serverRecive.start();

			Player a = new Player(1, "Henrik", 2, 2, "up", 1);
			Player b = new Player(2, "Peter", 2, 3, "down", 2);
			Player c = new Player(3, "Spiller 1", 2, 4, "left", 2);
			gameState.players.add(a);
			gameState.players.add(b);
			gameState.players.add(c);

			sendGameState(gameState);

		}

	}

// Send gameState i JSON Format til alle spillere. 
	public static void sendGameState(GameState gameState) {
		// Pak gameState sammen

		String json = gameState.sendGameState();

		// send

		for (int i = 0; i < outPutStreamList.size(); i++) {
			try {
				outPutStreamList.get(i).writeBytes(json + "\n");
				System.out.println("Der er fiksere");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static GameState newPlayer(GameState gameState, String name) {
		// Denne metode finder en fri plads i spawn og opretter en ny spiller med "Navn"
		System.out.println(name + " Has joined the game");
		GameState tempState = gameState;
		pair friplads = getFreeSpawn(gameState.getPlayers());

		Player newPlayer = new Player(gameState.players.size(), name, friplads.x, friplads.y, "down", 0);

		tempState.players.add(newPlayer);
		return tempState;
	}

	public static pair getFreeSpawn(List<Player> players)
	// denne metode finder en fri plads i spawn området
	{
		int x = 1;
		int y = 1;
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

		for (int i = 0; i < spawnPairs.size(); i++) {
			if (pairs.contains(spawnPairs.get(i))) {
				spawnPairs.remove(i);
			}
		}

		pair p = spawnPairs.get(0);

		return p;
	}

}