package game;

public class Fruit {
	String name;
	int yPos;
	int xPos;
	// måske x og y kord

	public Fruit(String name, int xPos, int yPos) {
		this.name = name;
		this.xPos = xPos;
		this.yPos = yPos;
	}

	@Override
	public String toString() {
		return name + "(" + xPos + "," + +yPos + ")";
	}

}
