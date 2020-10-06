package game;

public class Player {
	int id;
	String name;
	int xpos;
	int ypos;
	int point;

	String direction;

	public Player(int id, String name, int xpos, int ypos, String direction, int point) {
		this.id = id;
		this.name = name;
		this.xpos = xpos;
		this.ypos = ypos;
		this.direction = direction;
		this.point = point;

	}

	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void addPoints(int p) {
		point += p;
	}

	public int getId() {
		return id;
	}

	public void resetPoints() {
		point = 0;
	}

	public String toString() {
		return name + " " + point + " " + xpos + " " + ypos + " " + direction + "         ";
	}
}
