 
// This is for the shootingx
import javax.swing.ImageIcon;

public class Bomb extends Characters implements Commons {

	private String shot = "shot.png";
	private final int H_SPACE = 6;
	private final int V_SPACE = 1;

	public Bomb() {
	}

	public Bomb(int x, int y) {

		ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
		setImage(ii.getImage());
		setX(x + H_SPACE);
		setY(y - V_SPACE);
	}
}
