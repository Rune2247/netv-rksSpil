package game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
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

			// sendGameState();
		}

	}

// Send gameState i JSON Format til alle spillere. 
	public static void sendGameState() {
		// Pak gameState sammen

		String json;

		// send
		try {
			outPutStreamList.get(0).writeBytes("+55+5+6651554+5" + '\n');
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < outPutStreamList.size(); i++) {
			try {
				outPutStreamList.get(i).writeBytes("HVad sÅ dEr3 Mand" + '\n');
				System.out.println("Der er fiksere");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
