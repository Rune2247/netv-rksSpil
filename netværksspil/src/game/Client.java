package game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

public class Client extends Application {

	public static GameState gameState;
	public static final int size = 30;
	public static final int scene_height = size * 20 + 50;
	public static final int scene_width = size * 20 + 200;

	public static Image image_floor;
	public static Image image_wall;
	public static Image image_banana;
	public static Image image_peach;
	public static Image hero_right, hero_left, hero_up, hero_down;
	public static Image g_1, g_2, g_3, g_4;

	public static Player me;
	public static List<Player> players = new ArrayList<Player>();
	public static int score;
	public static int id;
	private static DataOutputStream outToServer;

	public static Text scoreLabel, mazeLabel;
	private static Label[][] fields;
	private static TextArea scoreList;

	// ---- SETS UP THE GAME ----
	// Sets up GUI and adds listners to the keys
	// Also updates the scoreboard once
	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			mazeLabel = new Text("FRUITLIST");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreLabel = new Text("TID");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreList = new TextArea();

			GridPane boardGrid = new GridPane();

			image_wall = new Image(getClass().getResourceAsStream("Image/wall4.png"), size, size, false, false);
			image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"), size, size, false, false);
			image_banana = new Image(getClass().getResourceAsStream("Image/f_banana.png"), size, size, false, false);
			image_peach = new Image(getClass().getResourceAsStream("Image/f_peach.png"), size, size, false, false);

			hero_right = new Image(getClass().getResourceAsStream("Image/heroRight.png"), size, size, false, false);
			hero_left = new Image(getClass().getResourceAsStream("Image/heroLeft.png"), size, size, false, false);
			hero_up = new Image(getClass().getResourceAsStream("Image/heroUp.png"), size, size, false, false);
			hero_down = new Image(getClass().getResourceAsStream("Image/heroDown.png"), size, size, false, false);

			g_1 = new Image(getClass().getResourceAsStream("Image/Pot_Up_Left.png"), size, size, false, false);
			g_2 = new Image(getClass().getResourceAsStream("Image/Pot_Up_Right.png"), size, size, false, false);
			g_3 = new Image(getClass().getResourceAsStream("Image/Pot_Down_Left.png"), size, size, false, false);
			g_4 = new Image(getClass().getResourceAsStream("Image/Pot_Down_Right.png"), size, size, false, false);

			fields = new Label[20][20];
			for (int j = 0; j < 20; j++) {
				for (int i = 0; i < 20; i++) {
					switch (Generel.board[j].charAt(i)) {
					case 'w':
						fields[i][j] = new Label("", new ImageView(image_wall));
						break;
					case ' ':
						fields[i][j] = new Label("", new ImageView(image_floor));
						break;
					case 'b':
						fields[i][j] = new Label("", new ImageView(image_banana));
						break;
					case 'p':
						fields[i][j] = new Label("", new ImageView(image_peach));
						break;

					case '1':
						fields[i][j] = new Label("", new ImageView(g_1));
						break;
					case '2':
						fields[i][j] = new Label("", new ImageView(g_2));
						break;
					case '3':
						fields[i][j] = new Label("", new ImageView(g_3));
						break;
					case '4':
						fields[i][j] = new Label("", new ImageView(g_4));
						break;
					default:
						throw new Exception("Illegal field value: " + Generel.board[j].charAt(i));
					}
					boardGrid.add(fields[i][j], i, j);
				}
			}
			scoreList.setEditable(false);

			grid.add(mazeLabel, 0, 0);
			grid.add(scoreLabel, 1, 0);
			grid.add(boardGrid, 0, 1);
			grid.add(scoreList, 1, 1);

			Scene scene = new Scene(grid, scene_width, scene_height);
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				switch (event.getCode()) {
				case UP:
					playerMoved(0, -1, "up");
					break;
				case DOWN:
					playerMoved(0, +1, "down");
					break;
				case LEFT:
					playerMoved(-1, 0, "left");
					break;
				case RIGHT:
					playerMoved(+1, 0, "right");
					break;
				case R:
					resetGame();
					break;
				default:
					break;
				}
			});

			updateScoreTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Runs through all the players and calls updatePlayerOnScreen.
	// See updatePlayerOnScreen for description
	public static void updateAllPlayers() {
		for (Player player : players) {
			updatePlayerOnScreen(player);
		}
	}

	// Checks if a postion contains a player.
	// The method is used to stop writing floor image where a player is standing.
	public static boolean checkIfFieldIsPlayer(int x, int y) {
		for (Player player : players) {
			if (player.xpos == x && player.ypos == y) {
				return false;
			}
		}
		return true;
	}

	// Takes in a player and writes it image on the screen.
	// The method uses "checkIfFieldIsPlayer" to check if the last position is a
	// player.
	// If a player stands on anothers players last position, it dosent write floor
	// on this position.
	public static void updatePlayerOnScreen(Player player) {
		System.out.println(player.toString());
		Platform.runLater(() -> {
			if (player.direction.equals("right")) {
				if (checkIfFieldIsPlayer(player.xpos - 1, player.ypos)) {
					fields[player.xpos - 1][player.ypos].setGraphic(new ImageView(image_floor));
				}
				fields[player.xpos][player.ypos].setGraphic(new ImageView(hero_right));
			}
			;
			if (player.direction.equals("left")) {
				if (checkIfFieldIsPlayer(player.xpos + 1, player.ypos)) {
					fields[player.xpos + 1][player.ypos].setGraphic(new ImageView(image_floor));
				}
				fields[player.xpos][player.ypos].setGraphic(new ImageView(hero_left));
			}
			;
			if (player.direction.equals("up")) {
				if (checkIfFieldIsPlayer(player.xpos, player.ypos + 1)) {
					fields[player.xpos][player.ypos + 1].setGraphic(new ImageView(image_floor));
				}
				fields[player.xpos][player.ypos].setGraphic(new ImageView(hero_up));
			}
			;
			if (player.direction.equals("down")) {
				if (checkIfFieldIsPlayer(player.xpos, player.ypos - 1)) {
					fields[player.xpos][player.ypos - 1].setGraphic(new ImageView(image_floor));
				}
				fields[player.xpos][player.ypos].setGraphic(new ImageView(hero_down));
			}
			;
		});
	}

	// Updates the scoreboard
	public static void updateScoreTable() {
		StringBuffer b = new StringBuffer(100);
		b.append("Score: " + Client.score + "\r\n");
		for (Player player : players) {
			if(player.fruit != null) {
				b.append(player.name + " : " + player.fruit.name + "\r\n");
			} else {
				b.append(player.name + " : " + "......" + "\r\n");
			}
			
		}
		Platform.runLater(() -> {
			
			scoreList.setText(b.toString());
		});
	}
	
	//Updates fruitlist
	public static void updateFruitList() {
		String list;
		if(Client.gameState.frugtList != null) {
			list = Client.gameState.frugtList.toString();
		} else {
			list = "";
		}
		Platform.runLater(() -> {
			System.out.println("Fruitlist update");
				mazeLabel.setText(list);
			
		
		});
	}

	// Sends the information of a player whenever a player hits a move key.
	public void playerMoved(int delta_x, int delta_y, String direction) {

		try {
			String move = "{id: " + Client.id + ", xpos: " + delta_x + ", ypos: " + delta_y + ", direction: \""
					+ direction + "\"}" + '\n';
			System.out.println(move);
			Client.outToServer.writeBytes(move);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Sends a reset command to the server.
	public void resetGame() {
		try {
			Client.outToServer.writeBytes("r" + '\n');
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ---- THE CLIENTS MAIN METHOD ----
	// When the client starts it waits for the players input of a name.
	// When entered, joins a server.
	public static void main(String[] args) throws Exception {

		// Making a connection - 10.24.3.237
		Socket clientSocket = new Socket("localhost", 12345);
		Client.outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		String playerName = inFromUser.readLine();
		Client.outToServer.writeBytes(playerName + '\n');
		Client.id = Integer.parseInt(inFromServer.readLine());
		System.out.println(Client.id);

		// Making a Thread
		ClientRecieveThread recieveThread = new ClientRecieveThread(clientSocket, inFromServer);
		recieveThread.start();

		launch(args);

	}

}
