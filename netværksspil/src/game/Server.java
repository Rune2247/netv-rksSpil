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

		ServerSocket welcomeSocket = new ServerSocket(12345);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			outPutStreamList.add(new DataOutputStream(connectionSocket.getOutputStream()));

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			ServerReciveThread serverRecive = new ServerReciveThread(inFromClient, connectionSocket);

			serverRecive.start();

			ArrayList<Player> players = new ArrayList<>();
			Player a = new Player(1, "Henrik", 12, 4, "Den vej", 1);
			Player b = new Player(2, "Peter", 120, 400, "Den vej", 2);
			Player c = new Player(3, "Spiller 1", 12000, 4000, "Den vej", 2);
			players.add(a);
			players.add(b);
			players.add(c);
			GameState state = new GameState(100, players);

			sendGameState(state);
		}

	}

	// private int score = 2;
//	private ArrayList<Player> players = new ArrayList<>();

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

	public GameState newPlayer(GameState gameState, String name) {
		GameState tempState = gameState;

		Player newPlayer = newPlayer(gameState.players.size(), name);

		return tempState;
	}

	public pair getFreeSpawn(List<Player> players)
	// finds a random new position which is not wall
	// and not occupied by other players
	{
		int x = 1;
		int y = 1;
		boolean found = false;
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

		}
		pair p = new pair(2, 1);

		return p;
	}

}