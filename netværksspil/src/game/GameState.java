package game;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class GameState {
	// GameState Format
	// Score - Int - viser teamets samlet score
	// Tasks - ArrayList Af frugt
	// PlayerData - ArrayList Af player objektter
	int score;
	ArrayList<Player> players = new ArrayList<>();

	public GameState(int score, ArrayList<Player> players) {
		this.score = score;
		this.players = players;
	}

	public String sendGameState() {
		String array = "players:[";
		// array = "players: ";
		for (Player player : players) {
			array += "{name: \"" + player.name + "\", xpos: " + player.xpos + ", ypos: " + player.ypos
					+ ", direction: \"" + player.direction + "\", point: " + player.point + "},";

		}
		array += "]";

		String json = ("{score: " + this.score + ", " + array + "}");
		return json;
	}

	public static GameState modtagGameState(String json) {
		JSONObject jo = new JSONObject(json);
		JSONArray ja = jo.getJSONArray("players");
		ArrayList<Player> tempList = new ArrayList<>();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jp = ja.getJSONObject(i);

			Player p = new Player(jp.getString("name"), jp.getInt("xpos"), jp.getInt("ypos"), jp.getString("direction"),
					jp.getInt("point"));
			tempList.add(p);

		}
		GameState tempState = new GameState(0, tempList);
		return tempState;
	}

	@Override
	public String toString() {

		return score + ": " + players.toString();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public int getScore() {
		return score;
	}
}
