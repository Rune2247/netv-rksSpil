package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

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
			} else {

				System.out.println(modifiedSentence);
				udPakComando(modifiedSentence);
				Server.sendGameState();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Udpakker en comando fra spillerne
	private void udPakComando(String json) {
		JSONObject jo = new JSONObject(json);
		for (int i = 0; i < Server.gameState.players.size(); i++) {

			if (Server.gameState.getPlayers().get(i).getId() == jo.getInt("id")) {
				Server.gameState.getPlayers().get(i).xpos += jo.getInt("xpos");
				Server.gameState.getPlayers().get(i).ypos += jo.getInt("ypos");
				Server.gameState.getPlayers().get(i).direction = jo.getString("direction");
			}
		}

	}
}
