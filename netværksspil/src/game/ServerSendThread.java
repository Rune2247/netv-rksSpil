package game;

public class ServerSendThread extends Thread {

	public double startTid;

	public ServerSendThread(double startTid) {
		this.startTid = startTid;

	}

	@Override
	public void run() {

		while (true) {
			tid();
		}
	}

	private void tid() {
		try {
			Thread.sleep(50);
			startTid = startTid - 0.5;
			System.out.println(startTid);
			Server.gameState.time = startTid;
			Server.sendGameState();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
