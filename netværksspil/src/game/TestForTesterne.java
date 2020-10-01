package game;

import java.util.ArrayList;

public class TestForTesterne {
	public static void main(String[] args) {
		ArrayList<Player> players = new ArrayList<>();
		Player a = new Player(1, "Henrik", 12, 4, "Den vej", 1);
		Player b = new Player(2, "Peter", 120, 400, "Den vej", 2);
		Player c = new Player(3, "Spiller 1", 12000, 4000, "Den vej", 2);
		players.add(a);
		players.add(b);
		players.add(c);
		GameState state = new GameState(100, players);
		// System.out.println(state.sendGameState());
		System.out.println(state.modtagGameState(state.sendGameState()));
	}

}
