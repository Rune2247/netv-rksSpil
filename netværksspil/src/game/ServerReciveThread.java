package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

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
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
