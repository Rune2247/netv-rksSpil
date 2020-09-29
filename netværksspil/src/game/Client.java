package game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		Player player = new Player("john", 5, 5, "down");

		// Making a connection
		Socket clientSocket = new Socket("10.24.3.83", 12345);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		// Making a Thread
		ClientRecieveThread recieveThread = new ClientRecieveThread(clientSocket, inFromServer, player);
		recieveThread.start();
		

		outToServer.writeBytes("Hej Rune" + '\n');
		outToServer.writeBytes("Nisser" + '\n');

	}

}
