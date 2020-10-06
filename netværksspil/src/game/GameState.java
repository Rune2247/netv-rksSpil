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
	ArrayList<Fruit> frugtList = new ArrayList<>();
	double time;

	public GameState(int score, ArrayList<Player> players, ArrayList<Fruit> fruitList, double time) {
		this.score = score;
		this.players = players;
		this.frugtList = fruitList;
		this.time = time;
	}

	public String sendGameState() {
		String array = "players:[";
		String fruitArray = "fruitList:[";
		// array = "players: ";
		for (Player player : players) {
			array += "{id: " + player.id + ", name: \"" + player.name + "\", xpos: " + player.xpos + ", ypos: "
					+ player.ypos + ", direction: \"" + player.direction + "\", point: " + player.point + "},";

		}
		for (Fruit fruit : frugtList) {
			fruitArray += "{name: \"" + fruit.name + "\"},";

		}
		array += "]";

		String json = ("{score: " + this.score + ", " + array + ", fruitList: " + fruitArray + "}");
		System.out.println("send json: " + json);
		return json;
	}

	public static GameState modtagGameState(String json) {
		JSONObject jo = new JSONObject(json);
		JSONArray ja = jo.getJSONArray("players");
		JSONArray jf = jo.getJSONArray("fruitList");

		ArrayList<Player> tempList = new ArrayList<>();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jp = ja.getJSONObject(i);

			Player p = new Player(jp.getInt("id"), jp.getString("name"), jp.getInt("xpos"), jp.getInt("ypos"),
					jp.getString("direction"), jp.getInt("point"));
			tempList.add(p);

		}
		ArrayList<Fruit> tempFruit = new ArrayList<>();
		for (int i = 0; i < jf.length(); i++) {
			JSONObject jp = jf.getJSONObject(i);
			Fruit f = new Fruit(jp.getString("name"));
		}

		GameState tempState = new GameState(0, tempList, tempFruit, 0);
		System.out.println(tempFruit);
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
