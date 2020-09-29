package game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {

	/**
	 * @param args
	 */

	private static ArrayList<OutputStream> outPutStreamList = new ArrayList<OutputStream>();
	private GameState gameState = new GameState("Vi tester");

	public static void main(String[] args) throws Exception {

		ServerSocket welcomeSocket = new ServerSocket(12345);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			outPutStreamList.add(connectionSocket.getOutputStream());

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			ServerReciveThread serverRecive = new ServerReciveThread(inFromClient, connectionSocket);

			serverRecive.start();

		}

	}

	public void sendGameState() {

	}

	public static List<Player> players = new ArrayList<Player>();

	public pair getRandomFreePosition()
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
