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
			Player a = new Player("Henrik", 12, 4, "Den vej", 1);
			Player b = new Player("Peter", 120, 400, "Den vej", 2);
			Player c = new Player("Spiller 1", 12000, 4000, "Den vej", 2);
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

		return tempState;
	}

	public pair getRandomFreePosition(List<Player> players)
	// finds a random new position which is not wall
	// and not occupied by other players
	{
		int x = 1;
		int y = 1;
		boolean found = false;
		while (!found) {
			Random r = new Random();
			x = Math.abs(r.nextInt() % 18) + 1;
			y = Math.abs(r.nextInt() % 18) + 1;
			if (Generel.board[y].charAt(x) == ' ') {
				found = true;
				for (Player p : players) {
					if (p.xpos == x && p.ypos == y)
						found = false;
				}

			}
		}
		pair p = new pair(x, y);
		return p;
	}

}
