package game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
<<<<<<< HEAD

		// Making a connection
		Socket clientSocket = new Socket("10.24.67.112", 12345);

		// Stuff used to send messages
=======
		
		//Making a connection
		Socket clientSocket= new Socket("10.24.3.83",12345);
				
		//Stuff used to send messages
>>>>>>> branch 'master' of https://github.com/Rune2247/netv-rksSpil.git
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		// BufferedReader inFromUser = new BufferedReader(new
		// InputStreamReader(System.in));

		outToServer.writeBytes("Hej Rune" + '\n');
<<<<<<< HEAD
		outToServer.writeBytes("Nisser" + '\n');
//		clientSocket.close();
		while (true) {

		}

=======
		Thread.sleep(100000);
		
		clientSocket.close();
>>>>>>> branch 'master' of https://github.com/Rune2247/netv-rksSpil.git
	}

}
