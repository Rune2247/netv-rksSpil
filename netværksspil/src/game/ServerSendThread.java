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
		startTid = startTid - System.currentTimeMillis();
		System.out.println(startTid);
	}
}
