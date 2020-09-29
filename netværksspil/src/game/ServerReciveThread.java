package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ServerReciveThread extends Thread {

	String modifiedSentence;
	BufferedReader inFromClient;
	Socket clientSocket;

	public ServerReciveThread(BufferedReader inFromClient, Socket clientSocket) {
		this.inFromClient = inFromClient;
		this.clientSocket = clientSocket;

	}

	@Override
	public void run() {
		while (clientSocket.isConnected()) {
			modtag();
		}
	}

	private void modtag() {
		// Modtag besked
		try {
			modifiedSentence = inFromClient.readLine();
			System.out.println(modifiedSentence);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
