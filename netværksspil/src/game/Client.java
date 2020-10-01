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
	public static Image hero_right, hero_left, hero_up, hero_down;

	public static Player me;
	public static List<Player> players = new ArrayList<Player>();
	public static int score;
	public static int id;
	private static DataOutputStream outToServer;

	private static Label[][] fields;
	private TextArea scoreList;

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			Text mazeLabel = new Text("Maze:");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			Text scoreLabel = new Text("Score:");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreList = new TextArea();

			GridPane boardGrid = new GridPane();

			image_wall = new Image(getClass().getResourceAsStream("Image/wall4.png"), size, size, false, false);
			image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"), size, size, false, false);

			hero_right = new Image(getClass().getResourceAsStream("Image/heroRight.png"), size, size, false, false);
			hero_left = new Image(getClass().getResourceAsStream("Image/heroLeft.png"), size, size, false, false);
			hero_up = new Image(getClass().getResourceAsStream("Image/heroUp.png"), size, size, false, false);
			hero_down = new Image(getClass().getResourceAsStream("Image/heroDown.png"), size, size, false, false);

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
				default:
					break;
				}
			});
			
			/*
			// Setting up standard players
			pair p = getRandomFreePosition();
			me = new Player(2, "Orville", p.getX(), p.getY(), "up", 2);
			players.add(me);
			fields[p.getX()][p.getY()].setGraphic(new ImageView(hero_up));
			*/

			updateScoreTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public static void updateAllPlayers() {
		System.out.println(players.size());
		for (Player player : players) {
			updatePlayerOnScreen(player);
		}
	}

	public static void updatePlayerOnScreen(Player player) {
		System.out.println(player.toString());
		Platform.runLater(() -> {
			if (player.direction.equals("right")) {
				fields[player.xpos][player.ypos].setGraphic(new ImageView(hero_right));
			}
			;
			if (player.direction.equals("left")) {
				fields[player.xpos][player.ypos].setGraphic(new ImageView(hero_left));
			}
			;
			if (player.direction.equals("up")) {
				fields[player.xpos][player.ypos].setGraphic(new ImageView(hero_up));
			}
			;
			if (player.direction.equals("down")) {
				fields[player.xpos][player.ypos].setGraphic(new ImageView(hero_down));
			}
			;
		});
	}


	public void updateScoreTable() {
		Platform.runLater(() -> {
			scoreList.setText("Score: " + Client.score);
		});
	}

	public void playerMoved(int delta_x, int delta_y, String direction) {
		
		try {
			String move = "{id: " + Client.id + ", xpos: " + delta_x + ", ypos: " + delta_y + ", direction: \"" + direction + "\"}" + '\n';
			System.out.println(move);
			Client.outToServer.writeBytes(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos() == x && p.getYpos() == y) {
				return p;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// Making a connection
		Socket clientSocket = new Socket("10.24.2.243", 12345);
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
